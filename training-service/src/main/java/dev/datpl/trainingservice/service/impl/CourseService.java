package dev.datpl.trainingservice.service.impl;

import dev.datpl.trainingservice.pojo.entity.Course;
import dev.datpl.trainingservice.pojo.request.CourseCreationRequest;
import dev.datpl.trainingservice.pojo.response.CourseCreationResponse;
import dev.datpl.trainingservice.repository.CourseRepository;
import dev.datpl.trainingservice.repository.PartnerRepository;
import dev.datpl.trainingservice.repository.TrainerRepository;
import dev.datpl.trainingservice.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService implements ICourseService {

    private final CourseRepository courseRepository;
    private final PartnerRepository partnerRepository;
    private final TrainerRepository trainerRepository;

    @Override
    public CourseCreationResponse createCourse(CourseCreationRequest courseCreationRequest) {
        return null;
    }

    @Override
    public List<Course> getAllCourses() {
        return List.of();
    }

    @Override
    public Course getCourseById(String courseId) {
        return null;
    }

    @Override
    public void updateCourse(Course course) {

    }

    @Override
    public void deleteCourse(String courseId) {

    }

    @Override
    public void assignCourseToPartner(String courseId, String partnerId) {

    }

    @Override
    public void assignCourseToTrainer(String courseId, String trainerId) {

    }
}
