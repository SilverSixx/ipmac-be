package dev.datpl.trainingservice.pojo.entity;

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
@Table(name = "partners")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partner_id")
    Long id;

    String name;
    String email;
    String phone;
    String address;
    String description;

    // 1 partner can have many courses, 1 course can belong to many partners
    @ManyToMany(mappedBy = "partners")
    @JsonIgnoreProperties("partners")
    List<Course> courses;

    @OneToMany(mappedBy = "partner")
    @JsonIgnoreProperties("partner")
    List<Trainee> trainees;

}
