package dev.datpl.commonservice.pojo.request;

import dev.datpl.commonservice.validator.ValidRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
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

    @ValidRole
    String role;

    public static UserRepresentation toUserRepresentation(UserCreationRequest userCreationRequest) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userCreationRequest.getUsername());
        user.setEmail(userCreationRequest.getEmail());
        user.setFirstName(userCreationRequest.getFirstName());
        user.setLastName(userCreationRequest.getLastName());
        user.setEnabled(true);
        user.setRealmRoles(Collections.singletonList(userCreationRequest.getRole()));
        user.setEmailVerified(false);

        List<CredentialRepresentation> credentials = new ArrayList<>();
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setValue(userCreationRequest.getPassword());
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setTemporary(false);
        credentials.add(credential);
        user.setCredentials(credentials);

        return user;
    }
}
