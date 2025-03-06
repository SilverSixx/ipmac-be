package dev.datpl.trainingservice.pojo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "trainers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Trainer extends User {
    String phone;
    String address;
    String description;

    @OneToMany(mappedBy = "trainer")
    @JsonIgnoreProperties("trainer")
    List<Course> courses;
}
