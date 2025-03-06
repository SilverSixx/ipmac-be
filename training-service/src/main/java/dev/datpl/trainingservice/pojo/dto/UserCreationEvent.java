package dev.datpl.trainingservice.pojo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationEvent {
    String userId;
    String username;
    String email;
    String firstName;
    String lastName;
    String role;
}