package dev.datpl.trainingservice.service.impl;

import dev.datpl.trainingservice.pojo.entity.Course;
import dev.datpl.trainingservice.pojo.entity.Partner;
import dev.datpl.trainingservice.pojo.entity.Trainee;
import dev.datpl.trainingservice.pojo.entity.User;
import dev.datpl.trainingservice.repository.CourseRepository;
import dev.datpl.trainingservice.repository.PartnerRepository;
import dev.datpl.trainingservice.repository.TraineeRepository;
import dev.datpl.trainingservice.service.ITraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TraineeService implements ITraineeService {

    private final TraineeRepository traineeRepository;
    private final PartnerRepository partnerRepository;
    private final CourseRepository courseRepository;

    @Override
    public void assignTraineeToPartner(String traineeId, String partnerId) {
        Trainee trainee = traineeRepository.findByUserId(traineeId).orElseThrow(
                () -> new IllegalArgumentException("Trainee not found")
        );
        Partner partner = partnerRepository.findByUserId(partnerId).orElseThrow(
                () -> new IllegalArgumentException("Partner not found")
        );

        trainee.setPartner(partner);
        partner.getTrainees().add(trainee);
        traineeRepository.save((User) trainee);
        partnerRepository.save((User) partner);
    }

    @Override
    public void assignTraineeToCourse(String traineeId, String courseId) {
        Trainee trainee = traineeRepository.findByUserId(traineeId).orElseThrow(
                () -> new IllegalArgumentException("Trainee not found")
        );
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new IllegalArgumentException("Course not found")
        );

        List<Course> courses = trainee.getCourses();
        courses.add(course);
        course.getTrainees().add(trainee);
        traineeRepository.save((User) trainee);
        courseRepository.save(course);
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return List.of();
    }

    @Override
    public void deleteTrainee(String traineeId) {}
}
