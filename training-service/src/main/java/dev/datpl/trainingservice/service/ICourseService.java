package dev.datpl.trainingservice.service;

import dev.datpl.trainingservice.pojo.entity.Course;
import dev.datpl.trainingservice.pojo.request.CourseCreationRequest;
import dev.datpl.trainingservice.pojo.response.CourseCreationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICourseService {
    // CRUD operations for Course
    CourseCreationResponse createCourse(CourseCreationRequest courseCreationRequest);
    List<Course> getAllCourses();
    Course getCourseById(String courseId);
    void updateCourse(Course course);
    void deleteCourse(String courseId);

    void assignCourseToPartner(String courseId, String partnerId);
    void assignCourseToTrainer(String courseId, String trainerId);
}
