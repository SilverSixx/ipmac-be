package dev.datpl.commonservice.service.impl;

import dev.datpl.commonservice.config.KafkaConfigProperties;
import dev.datpl.commonservice.config.Oauth2ConfigProperties;
import dev.datpl.commonservice.exception.UserCreationException;
import dev.datpl.commonservice.exception.UserDeletionException;
import dev.datpl.commonservice.exception.UserRetrievalException;
import dev.datpl.commonservice.pojo.dto.UserCreationEvent;
import dev.datpl.commonservice.pojo.request.UserCreationRequest;
import dev.datpl.commonservice.pojo.response.UserResponse;
import dev.datpl.commonservice.service.IOauth2Service;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService implements IOauth2Service {
    private final Oauth2ConfigProperties oauth2ConfigProperties;
    private final KafkaTemplate<String, UserCreationEvent> kafkaTemplate;

    private final KafkaConfigProperties kafkaConfigProperties;

    private RealmResource getRealmResource() {
        return Keycloak.getInstance(
                        oauth2ConfigProperties.getServerUrl(),
                        oauth2ConfigProperties.getRealm(),
                        "admin",
                        "admin",
                        oauth2ConfigProperties.getClientId(),
                        oauth2ConfigProperties.getClientSecret()
                )
                .realm(oauth2ConfigProperties.getRealm());

    }

    @Override
    public UserResponse createUser(UserCreationRequest userRepresentation) throws UserCreationException {
        try {
            UserRepresentation user = UserCreationRequest.toUserRepresentation(userRepresentation);
            UsersResource userResource = getRealmResource().users();
            Response response = userResource.create(user);
            if (response.getStatus() != 201) {
                log.error("User creation failed. Status code: {}", response.getStatus());
                String errorMessage = response.readEntity(String.class);
                log.error("Error details: {}", errorMessage);
                throw new UserCreationException(
                        "Failed to create user in Keycloak",
                        response.getStatus()
                );
            }

            String createdUserId = extractUserIdFromResponse(response);
            List<String> assignedGroups = new ArrayList<>();
            if (userRepresentation.getRole() != null) {
                String groupName = userRepresentation.getRole();
                GroupsResource groupsResource = getRealmResource().groups();
                List<GroupRepresentation> groups = groupsResource.groups();
                GroupRepresentation targetGroup = groups.stream()
                        .filter(group -> group.getName().equals(groupName))
                        .findFirst()
                        .orElseThrow(() -> new UserCreationException(
                                "Group not found: " + groupName,
                                404
                        ));
                UserResource userRes = userResource.get(createdUserId);
                userRes.joinGroup(targetGroup.getId());
                assignedGroups.add(groupName);
            }

            if (!Objects.equals(userRepresentation.getRole(), "admin")) {
                // not sending user creation event for admin users
                UserCreationEvent event = UserCreationEvent.builder()
                        .userId(createdUserId)
                        .username(userRepresentation.getUsername())
                        .email(userRepresentation.getEmail())
                        .firstName(userRepresentation.getFirstName())
                        .lastName(userRepresentation.getLastName())
                        .role(userRepresentation.getRole())
                        .build();
                try {
                    kafkaTemplate.send(kafkaConfigProperties.getUserCreationTopic(), event)
                            .whenComplete((result, ex) -> {
                                if (ex == null) {
                                    log.info("User creation event sent successfully for user: {}",
                                            userRepresentation.getUsername());
                                } else {
                                    log.error("Failed to send user creation event", ex);
                                }
                            });
                } catch (Exception eventPublishEx) {
                    log.error("Error publishing user creation event", eventPublishEx);
                    // Consider implementing a fallback mechanism or retry logic
                }
            }

            log.info("User created and added to group successfully: {}", user.getUsername());
            return UserResponse.fromUserRepresentation(user, assignedGroups);
        } catch (WebApplicationException e) {
            log.error("Keycloak API error during user creation", e);
            throw new UserCreationException(
                    "Error interacting with Keycloak",
                    e.getResponse().getStatus()
            );
        } catch (Exception e) {
            log.error("Unexpected error during user creation", e);
            throw new UserCreationException(
                    "Unexpected error during user creation",
                    500
            );
        }
    }

    private String extractUserIdFromResponse(Response response) {
        URI location = response.getLocation();
        if (location != null) {
            String path = location.getPath();
            return path.substring(path.lastIndexOf('/') + 1);
        }
        throw new UserCreationException("Could not extract user ID from response", 500);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        try {
            List<UserRepresentation> users = getRealmResource().users().list();
            if (users == null || users.isEmpty()) {
                log.warn("No users found in Keycloak realm");
                return Collections.emptyList();
            }

            List<UserResponse> userResponses = new ArrayList<>();
            for (UserRepresentation user : users) {
                UserResource userResource = getRealmResource().users().get(user.getId());
                List<RoleRepresentation> userRoles = userResource.roles().realmLevel().listEffective();
                // check only add roles with name 'trainee', 'trainer', 'admin', 'partner
                List<String> roles = new ArrayList<>();
                for (RoleRepresentation role : userRoles) {
                    if (role.getName().equals("trainee") || role.getName().equals("trainer") || role.getName().equals("admin") || role.getName().equals("partner")) {
                        roles.add(role.getName());
                    }
                }
                UserResponse userResponse = UserResponse.fromUserRepresentation(user, roles);
                userResponses.add(userResponse);
            }

            log.info("Successfully retrieved {} users", userResponses.size());
            return userResponses;
        } catch (Exception e) {
            log.error("Failed to retrieve users from Keycloak", e);
            throw new UserRetrievalException(
                    "Failed to retrieve users from Keycloak"
            );
        }

    }

    @Override
    public void deleteUser(String userId) {
        try {
            getRealmResource().users().delete(userId);
            log.info("Successfully deleted user with ID: {}", userId);
        } catch (Exception e) {
            log.error("Failed to delete user with ID: {}", userId, e);
            throw new UserDeletionException(
                    "Failed to delete user from Keycloak",
                    userId
            );
        }
    }
}