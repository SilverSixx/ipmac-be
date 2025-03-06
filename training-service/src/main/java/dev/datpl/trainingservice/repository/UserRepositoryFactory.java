package dev.datpl.trainingservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryFactory {
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final PartnerRepository partnerRepository;

    public UserRepository getUserRepository(String role) {
        return switch (role) {
            case "trainee" -> traineeRepository;
            case "trainer" -> trainerRepository;
            case "partner" -> partnerRepository;
            default -> null;
        };
    }

}
