package dev.datpl.trainingservice.pojo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
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

    @Override
    public int hashCode() {
        return Objects.hash(getUserId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Trainee trainee = (Trainee) obj;
        return Objects.equals(getUserId(), trainee.getUserId());
    }

}
