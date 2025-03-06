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
@Table(name = "partners")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Partner extends User {
    String phone;
    String address;

    // 1 partner can have many courses, 1 course can belong to many partners
    @ManyToMany(mappedBy = "partners")
    @JsonIgnoreProperties("partners")
    List<Course> courses;

    @OneToMany(mappedBy = "partner")
    @JsonIgnoreProperties("partner")
    List<Trainee> trainees;

}
