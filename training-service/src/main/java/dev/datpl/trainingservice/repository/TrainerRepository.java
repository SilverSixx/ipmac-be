package dev.datpl.trainingservice.repository;

import dev.datpl.trainingservice.pojo.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}
