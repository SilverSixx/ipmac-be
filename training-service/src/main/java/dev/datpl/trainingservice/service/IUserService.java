package dev.datpl.trainingservice.service;

import dev.datpl.trainingservice.pojo.entity.User;

public interface IUserService {
    void handleUserCreationEvent(User user);
}
