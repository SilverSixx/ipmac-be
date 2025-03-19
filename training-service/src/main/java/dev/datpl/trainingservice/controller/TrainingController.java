package dev.datpl.trainingservice.controller;

import dev.datpl.trainingservice.pojo.entity.Course;
import dev.datpl.trainingservice.pojo.entity.Trainee;
import dev.datpl.trainingservice.pojo.request.AssignTraineeToPartnerDto;
import dev.datpl.trainingservice.pojo.request.CourseCreationRequest;
import dev.datpl.trainingservice.service.ICourseService;
import dev.datpl.trainingservice.service.ITraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/training")
public class TrainingController {
    private final ITraineeService traineeService;
    private final ICourseService courseService;

    @GetMapping
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok("Training service is running");
    }

    @PostMapping("/trainees/{userId}/assign-partner")
    public ResponseEntity<?> assignPartner(@PathVariable String userId, @Validated @RequestBody AssignTraineeToPartnerDto dto) {
        traineeService.assignTraineeToPartner(userId, dto.getPartnerId());
        return ResponseEntity.ok("Trainee assigned to partner");
    }

    @PostMapping("/trainees/{userId}/assign-course")
    public ResponseEntity<?> assignCourse(@PathVariable String userId, @RequestParam String courseId) {
        traineeService.assignTraineeToCourse(userId, courseId);
        return ResponseEntity.ok("Trainee assigned to course");
    }

    @DeleteMapping("/trainees/{userId}")
    public ResponseEntity<?> deleteTrainee(@PathVariable String userId) {
        traineeService.deleteTrainee(userId);
        return ResponseEntity.ok("Trainee deleted");
    }

    @GetMapping("/trainees")
    public ResponseEntity<List<Trainee>> getAllTrainees() {
        return ResponseEntity.ok(traineeService.getAllTrainees());
    }

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable String courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    @PostMapping("/courses")
    public ResponseEntity<?> createCourse(@Validated @RequestBody CourseCreationRequest courseCreationRequest) {
        return ResponseEntity.ok(courseService.createCourse(courseCreationRequest));
    }

    @PutMapping("/courses/{courseId}")
    public ResponseEntity<?> updateCourse(@PathVariable String courseId, @Validated @RequestBody Course course) {
        courseService.updateCourse(course);
        return ResponseEntity.ok("Course updated");
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable String courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok("Course deleted");
    }

    @PostMapping("/courses/{courseId}/assign-partner")
    public ResponseEntity<?> assignCourseToPartner(@PathVariable String courseId, @RequestParam String partnerId) {
        courseService.assignCourseToPartner(courseId, partnerId);
        return ResponseEntity.ok("Course assigned to partner");
    }

    @PostMapping("/courses/{courseId}/assign-trainer")
    public ResponseEntity<?> assignCourseToTrainer(@PathVariable String courseId, @RequestParam String trainerId) {
        courseService.assignCourseToTrainer(courseId, trainerId);
        return ResponseEntity.ok("Course assigned to trainer");
    }

}
