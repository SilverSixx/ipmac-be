package dev.datpl.adminservice.repository;

import dev.datpl.adminservice.pojo.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}
