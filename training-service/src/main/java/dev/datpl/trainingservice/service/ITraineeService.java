package dev.datpl.trainingservice.service;

import dev.datpl.trainingservice.pojo.entity.Trainee;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ITraineeService {
    void assignTraineeToPartner(String traineeId, String partnerId);

    void assignTraineeToCourse(String traineeId, String courseId);

    List<Trainee> getAllTrainees();

    void deleteTrainee(String traineeId);
}
