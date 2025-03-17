package dev.datpl.trainingservice.service.impl;

import dev.datpl.trainingservice.pojo.entity.Course;
import dev.datpl.trainingservice.pojo.entity.Partner;
import dev.datpl.trainingservice.pojo.entity.Trainee;
import dev.datpl.trainingservice.pojo.entity.User;
import dev.datpl.trainingservice.repository.CourseRepository;
import dev.datpl.trainingservice.repository.PartnerRepository;
import dev.datpl.trainingservice.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeRepository mockTraineeRepository;
    @Mock
    private PartnerRepository mockPartnerRepository;
    @Mock
    private CourseRepository mockCourseRepository;

    private TraineeService traineeServiceUnderTest;

    @BeforeEach
    void setUp() {
        traineeServiceUnderTest = new TraineeService(mockTraineeRepository, mockPartnerRepository,
                mockCourseRepository);
    }

    @Test
    void testAssignTraineeToPartner() {
        // Setup
        // Configure TraineeRepository.findByUserId(...).
        final Optional<Trainee> trainee = Optional.of(Trainee.builder()
                .courses(List.of(Course.builder()
                        .trainees(List.of())
                        .build()))
                .partner(Partner.builder()
                        .trainees(List.of())
                        .build())
                .build());
        when(mockTraineeRepository.findByUserId("traineeId")).thenReturn(trainee);

        // Configure PartnerRepository.findByUserId(...).
        final Optional<Partner> partner = Optional.of(Partner.builder()
                .trainees(List.of(Trainee.builder()
                        .courses(List.of(Course.builder().build()))
                        .build()))
                .build());
        when(mockPartnerRepository.findByUserId("partnerId")).thenReturn(partner);

        // Run the test
        traineeServiceUnderTest.assignTraineeToPartner("traineeId", "partnerId");

        // Verify the results
        verify(mockTraineeRepository).save(any(User.class));
        verify(mockPartnerRepository).save(any(User.class));
    }

    @Test
    void testAssignTraineeToPartner_TraineeRepositoryFindByUserIdReturnsAbsent() {
        // Setup
        when(mockTraineeRepository.findByUserId("traineeId")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> traineeServiceUnderTest.assignTraineeToPartner("traineeId", "partnerId"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testAssignTraineeToPartner_PartnerRepositoryFindByUserIdReturnsAbsent() {
        // Setup
        // Configure TraineeRepository.findByUserId(...).
        final Optional<Trainee> trainee = Optional.of(Trainee.builder()
                .courses(List.of(Course.builder()
                        .trainees(List.of())
                        .build()))
                .partner(Partner.builder()
                        .trainees(List.of())
                        .build())
                .build());
        when(mockTraineeRepository.findByUserId("traineeId")).thenReturn(trainee);

        when(mockPartnerRepository.findByUserId("partnerId")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> traineeServiceUnderTest.assignTraineeToPartner("traineeId", "partnerId"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testAssignTraineeToCourse() {
        // Setup
        // Configure TraineeRepository.findByUserId(...).
        final Optional<Trainee> trainee = Optional.of(Trainee.builder()
                .courses(List.of(Course.builder()
                        .trainees(List.of())
                        .build()))
                .partner(Partner.builder()
                        .trainees(List.of())
                        .build())
                .build());
        when(mockTraineeRepository.findByUserId("traineeId")).thenReturn(trainee);

        // Configure CourseRepository.findById(...).
        final Optional<Course> course = Optional.of(Course.builder()
                .trainees(List.of(Trainee.builder()
                        .courses(List.of())
                        .partner(Partner.builder().build())
                        .build()))
                .build());
        when(mockCourseRepository.findById("courseId")).thenReturn(course);

        // Run the test
        traineeServiceUnderTest.assignTraineeToCourse("traineeId", "courseId");

        // Verify the results
        verify(mockTraineeRepository).save(any(User.class));
        verify(mockCourseRepository).save(Course.builder()
                .trainees(List.of(Trainee.builder()
                        .courses(List.of())
                        .partner(Partner.builder().build())
                        .build()))
                .build());
    }

    @Test
    void testAssignTraineeToCourse_TraineeRepositoryFindByUserIdReturnsAbsent() {
        // Setup
        when(mockTraineeRepository.findByUserId("traineeId")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> traineeServiceUnderTest.assignTraineeToCourse("traineeId", "courseId"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testAssignTraineeToCourse_CourseRepositoryFindByIdReturnsAbsent() {
        // Setup
        // Configure TraineeRepository.findByUserId(...).
        final Optional<Trainee> trainee = Optional.of(Trainee.builder()
                .courses(List.of(Course.builder()
                        .trainees(List.of())
                        .build()))
                .partner(Partner.builder()
                        .trainees(List.of())
                        .build())
                .build());
        when(mockTraineeRepository.findByUserId("traineeId")).thenReturn(trainee);

        when(mockCourseRepository.findById("courseId")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> traineeServiceUnderTest.assignTraineeToCourse("traineeId", "courseId"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testGetAllTrainees() {
        assertThat(traineeServiceUnderTest.getAllTrainees()).isEqualTo(Collections.emptyList());
    }

    @Test
    void testDeleteTrainee() {
        traineeServiceUnderTest.deleteTrainee("traineeId");
    }
}
