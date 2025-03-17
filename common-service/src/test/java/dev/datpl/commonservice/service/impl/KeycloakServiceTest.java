package dev.datpl.commonservice.service.impl;

import dev.datpl.commonservice.config.KafkaConfigProperties;
import dev.datpl.commonservice.config.Oauth2ConfigProperties;
import dev.datpl.commonservice.exception.UserCreationException;
import dev.datpl.commonservice.exception.UserDeletionException;
import dev.datpl.commonservice.exception.UserRetrievalException;
import dev.datpl.commonservice.pojo.dto.UserCreationEvent;
import dev.datpl.commonservice.pojo.request.UserCreationRequest;
import dev.datpl.commonservice.pojo.response.UserResponse;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.kafka.core.KafkaTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class KeycloakServiceTest {
    @Mock
    private Oauth2ConfigProperties oauth2ConfigProperties;
    @Mock
    private KafkaTemplate<String, UserCreationEvent> kafkaTemplate;
    @Mock
    private KafkaConfigProperties kafkaConfigProperties;

    @InjectMocks
    private KeycloakService keycloakService;

    private MockedStatic<Keycloak> keycloakStaticMock;
    private Keycloak keycloakMock;
    private RealmResource realmResourceMock;
    private UsersResource usersResourceMock;
    private GroupsResource groupsResourceMock;
    private UserResource userResourceMock;
    private Response responseMock;
    private UserRepresentation keycloakUserRepresentation;
    private UserCreationRequest userCreationRequest;
    private GroupRepresentation groupRepresentation;

    @BeforeEach
    void setUp() {
        // Initialize common mocks and data
        keycloakStaticMock = mockStatic(Keycloak.class);
        realmResourceMock = mock(RealmResource.class);
        usersResourceMock = mock(UsersResource.class);
        groupsResourceMock = mock(GroupsResource.class);
        userResourceMock = mock(UserResource.class);
        responseMock = mock(Response.class);
        keycloakUserRepresentation = new UserRepresentation();
        userCreationRequest = UserCreationRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .role("user")
                .build();
        groupRepresentation = new GroupRepresentation();
        groupRepresentation.setId("group-id");
        groupRepresentation.setName("user");
        keycloakMock = mock(Keycloak.class);

        // Mock Keycloak static method to return the mock instance
        given(Keycloak.getInstance(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString()
        )).willReturn(keycloakMock);

        given(keycloakMock.realm(anyString())).willReturn(realmResourceMock);
        given(realmResourceMock.users()).willReturn(usersResourceMock);
        given(realmResourceMock.groups()).willReturn(groupsResourceMock);
        given(oauth2ConfigProperties.getServerUrl()).willReturn("http://localhost:8080");
        given(oauth2ConfigProperties.getRealm()).willReturn("test-realm");
        given(oauth2ConfigProperties.getClientId()).willReturn("test-client");
        given(oauth2ConfigProperties.getClientSecret()).willReturn("secret");
        given(kafkaConfigProperties.getUserCreationTopic()).willReturn("user.creation.topic");
    }

    @AfterEach
    void tearDown() {
        if (keycloakStaticMock != null) {
            keycloakStaticMock.close();
        }
    }

    @Test
    void givenValidUserCreationRequest_whenCreateUser_thenUserCreatedAndEventSent() throws UserCreationException {
        // Given
        given(usersResourceMock.create(any(UserRepresentation.class))).willReturn(responseMock);
        given(responseMock.getStatus()).willReturn(201);
        given(responseMock.getLocation()).willReturn(URI.create("http://localhost:8080/auth/admin/realms/test-realm/users/user-id"));
        given(usersResourceMock.get("user-id")).willReturn(userResourceMock);
        given(groupsResourceMock.groups()).willReturn(List.of(groupRepresentation));
        given(kafkaTemplate.send(anyString(), any(UserCreationEvent.class))).willReturn(CompletableFuture.completedFuture(null));

        // When
        UserResponse result = keycloakService.createUser(userCreationRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");

        then(kafkaTemplate).should().send(eq(kafkaConfigProperties.getUserCreationTopic()), argThat(event ->
                Objects.equals(event.getUserId(), "user-id") &&
                        Objects.equals(event.getUsername(), "testuser") &&
                        Objects.equals(event.getEmail(), "test@example.com") &&
                        Objects.equals(event.getFirstName(), "Test") &&
                        Objects.equals(event.getLastName(), "User") &&
                        Objects.equals(event.getRole(), "user")
        ));
    }

    @Test
    void givenUserCreationRequest_whenKeycloakReturnsError_thenThrowUserCreationException() {
        // Given
        given(usersResourceMock.create(any(UserRepresentation.class))).willReturn(responseMock);
        given(responseMock.getStatus()).willReturn(400);
        given(responseMock.readEntity(String.class)).willReturn("Error message from Keycloak");

        // When
        UserCreationException exception = assertThrows(UserCreationException.class, () -> keycloakService.createUser(userCreationRequest));

        // Then
        // return 500 because of the controller advice
        assertThat(exception.getStatusCode()).isEqualTo(500);
        then(kafkaTemplate).should(never()).send(anyString(), any());
    }

    @Test
    void givenUserCreationRequest_whenKeycloakThrowsWebApplicationException_thenThrowUserCreationException() {
        // Given
        given(usersResourceMock.create(any(UserRepresentation.class))).willThrow(new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR));

        // When
        UserCreationException exception = assertThrows(UserCreationException.class, () -> keycloakService.createUser(userCreationRequest));

        // Then
        assertThat(exception.getStatusCode()).isEqualTo(500);
        then(kafkaTemplate).should(never()).send(anyString(), any());
    }

    @Test
    void givenUserCreationRequest_whenKafkaSendFails_thenLogError() throws UserCreationException {
        // Given
        given(usersResourceMock.create(any(UserRepresentation.class))).willReturn(responseMock);
        given(responseMock.getStatus()).willReturn(201);
        given(responseMock.getLocation()).willReturn(URI.create("http://localhost:8080/auth/admin/realms/test-realm/users/user-id"));
        given(usersResourceMock.get("user-id")).willReturn(userResourceMock);
        given(groupsResourceMock.groups()).willReturn(List.of(groupRepresentation));
        given(kafkaTemplate.send(anyString(), any(UserCreationEvent.class))).willReturn(CompletableFuture.failedFuture(new RuntimeException("Kafka send failed")));
        when(responseMock.getHeaderString("Location")).thenReturn("http://localhost:8080/auth/admin/realms/test-realm/users/user-id");

        // When
        UserResponse result = keycloakService.createUser(userCreationRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    void givenUserCreationRequest_whenUnexpectedErrorOccurs_thenThrowUserCreationException() {
        // Given
        given(usersResourceMock.create(any(UserRepresentation.class))).willThrow(new RuntimeException("Something went wrong"));

        // When
        UserCreationException exception = assertThrows(UserCreationException.class, () -> keycloakService.createUser(userCreationRequest));

        // Then
        assertThat(exception.getStatusCode()).isEqualTo(500);
        then(kafkaTemplate).should(never()).send(anyString(), any());
    }

    @Test
    void givenNoUsersInKeycloak_whenGetAllUsers_thenReturnEmptyList() {
        // Given
        given(usersResourceMock.list()).willReturn(Collections.emptyList());

        // When
        List<UserResponse> result = keycloakService.getAllUsers();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void givenUsersInKeycloakWithSpecificRoles_whenGetAllUsers_thenReturnFilteredUserList() {
        // Given
        UserRepresentation user1 = new UserRepresentation();
        user1.setId("user-id-1");
        user1.setUsername("testuser1");
        user1.setEmail("test1@example.com");

        UserRepresentation user2 = new UserRepresentation();
        user2.setId("user-id-2");
        user2.setUsername("testuser2");
        user2.setEmail("test2@example.com");

        given(usersResourceMock.list()).willReturn(List.of(user1, user2));

        UserResource userResource1Mock = mock(UserResource.class);
        given(usersResourceMock.get("user-id-1")).willReturn(userResource1Mock);
        given(userResource1Mock.roles()).willReturn(mock(RoleMappingResource.class));
        given(userResource1Mock.roles().realmLevel()).willReturn(mock(RoleScopeResource.class));
        RoleRepresentation traineeRole = new RoleRepresentation();
        traineeRole.setName("trainee");
        given(userResource1Mock.roles().realmLevel().listEffective()).willReturn(List.of(traineeRole));

        UserResource userResource2Mock = mock(UserResource.class);
        given(usersResourceMock.get("user-id-2")).willReturn(userResource2Mock);
        given(userResource2Mock.roles()).willReturn(mock(RoleMappingResource.class));
        given(userResource2Mock.roles().realmLevel()).willReturn(mock(RoleScopeResource.class));
        RoleRepresentation trainerRole = new RoleRepresentation();
        trainerRole.setName("trainer");
        given(userResource2Mock.roles().realmLevel().listEffective()).willReturn(List.of(trainerRole));

        // When
        List<UserResponse> result = keycloakService.getAllUsers();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo("testuser1");
        assertThat(result.get(0).getRoles()).containsExactly("trainee");
        assertThat(result.get(1).getUsername()).isEqualTo("testuser2");
        assertThat(result.get(1).getRoles()).containsExactly("trainer");
    }

    @Test
    void givenKeycloakThrowsException_whenGetAllUsers_thenThrowUserRetrievalException() {
        // Given
        given(usersResourceMock.list()).willThrow(new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR));

        // When
        UserRetrievalException exception = assertThrows(UserRetrievalException.class, () -> keycloakService.getAllUsers());

        // Then
        assertThat(exception.getMessage()).isEqualTo("Failed to retrieve users from Keycloak");
    }

    @Test
    void givenValidUserId_whenDeleteUser_thenUserDeletedSuccessfully() {
        // Given
        String userIdToDelete = "user-to-delete";

        // When
        keycloakService.deleteUser(userIdToDelete);

        // Then
        then(usersResourceMock).should().delete(userIdToDelete);
    }

    @Test
    void givenInvalidUserId_whenDeleteUser_thenKeycloakThrowsExceptionAndUserDeletionExceptionThrown() {
        // Given
        String userIdToDelete = "invalid-user-id";
        WebApplicationException exceptionToDelete = new WebApplicationException(Response.Status.NOT_FOUND);
        given(usersResourceMock.delete(userIdToDelete)).willThrow(exceptionToDelete);

        // When
        UserDeletionException exception = assertThrows(UserDeletionException.class, () -> keycloakService.deleteUser(userIdToDelete));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Failed to delete user from Keycloak");
        assertThat(exception.getUserId()).isEqualTo("invalid-user-id");
    }

}
