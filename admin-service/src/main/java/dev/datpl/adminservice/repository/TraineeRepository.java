package dev.datpl.adminservice.repository;

import dev.datpl.adminservice.pojo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraineeRepository extends JpaRepository<Course, Long> {
}
