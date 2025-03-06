package dev.datpl.trainingservice.repository;

import dev.datpl.trainingservice.pojo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, String> {
}
