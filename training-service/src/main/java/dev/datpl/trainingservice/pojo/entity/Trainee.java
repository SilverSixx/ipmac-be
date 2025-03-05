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
@Table(name = "trainees")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Trainee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainee_id")
    Long id;

    String name;
    String email;
    String phone;
    String address;
    String description;

    @ManyToMany(mappedBy = "trainees")
    @JsonIgnoreProperties("trainees")
    List<Course> courses;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    Partner partner;

}
