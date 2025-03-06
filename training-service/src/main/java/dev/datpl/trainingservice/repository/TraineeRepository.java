package dev.datpl.trainingservice.repository;

import dev.datpl.trainingservice.pojo.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, String>, UserRepository {
    Optional<Trainee> findByUserId(String userId);
}
