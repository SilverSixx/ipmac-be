package dev.datpl.trainingservice.service;

import dev.datpl.trainingservice.pojo.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {
    void handleUserCreationEvent(User user);
}
