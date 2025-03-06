package dev.datpl.trainingservice.repository;

import dev.datpl.trainingservice.pojo.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, String>, UserRepository {
    Optional<Trainer> findByUserId(String userId);
}
