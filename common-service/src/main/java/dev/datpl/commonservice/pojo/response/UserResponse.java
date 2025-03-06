package dev.datpl.commonservice.pojo.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String email;
    String firstName;
    String lastName;
    List<String> roles;

    public static UserResponse fromUserRepresentation(UserRepresentation userRepresentation, List<String> roles) {
        return UserResponse.builder()
                .id(userRepresentation.getId())
                .username(userRepresentation.getUsername())
                .email(userRepresentation.getEmail())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .roles(roles)
                .build();
    }

}
