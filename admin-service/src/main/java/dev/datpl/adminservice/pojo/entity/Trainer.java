package dev.datpl.adminservice.pojo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "trainers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainer_id")
    Long id;

    String name;
    String email;
    String phone;
    String address;
    String description;

    @OneToMany(mappedBy = "trainer")
    @JsonIgnoreProperties("trainer")
    List<Course> courses;
}
