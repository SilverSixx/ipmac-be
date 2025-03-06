package dev.datpl.trainingservice.pojo.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class User {
    @Id
    String userId;

    String username;
    String email;
    String firstName;
    String lastName;
    String role;
}
