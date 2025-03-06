package dev.datpl.trainingservice.pojo.entity;

import org.springframework.stereotype.Component;

@Component
public class UserFactory {
    public User getUser(String role) {
        return switch (role) {
            case "trainee" -> new Trainee();
            case "trainer" -> new Trainer();
            case "partner" -> new Partner();
            default -> null;
        };
    }
}
