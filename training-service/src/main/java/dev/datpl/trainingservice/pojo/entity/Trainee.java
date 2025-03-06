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
@Table(name = "trainees")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Trainee extends User {
    @ManyToMany(mappedBy = "trainees")
    @JsonIgnoreProperties("trainees")
    List<Course> courses;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    Partner partner;

}
