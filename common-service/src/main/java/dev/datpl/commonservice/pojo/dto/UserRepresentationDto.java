package dev.datpl.commonservice.pojo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Collections;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRepresentationDto {
    @NotBlank(message = "Username is required")
    String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    String email;

    @NotBlank(message = "First name is required")
    String firstName;

    @NotBlank(message = "Last name is required")
    String lastName;

    @NotBlank(message = "Password is required")
    String password;

    @NotBlank(message = "Role is required")
    String role;

    public static UserRepresentation toUserRepresentation(UserRepresentationDto userRepresentationDto) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userRepresentationDto.getUsername());
        user.setEmail(userRepresentationDto.getEmail());
        user.setFirstName(userRepresentationDto.getFirstName());
        user.setLastName(userRepresentationDto.getLastName());
        user.setEnabled(true);
        user.setRealmRoles(Collections.singletonList(userRepresentationDto.getRole()));
        user.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setValue(userRepresentationDto.getPassword());
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setTemporary(false);

        List<CredentialRepresentation> credentials = user.getCredentials();
        credentials.add(credential);

        user.setCredentials(credentials);
        return user;
    }
}
