package dev.datpl.trainingservice.repository;

import dev.datpl.trainingservice.pojo.entity.User;

public interface UserRepository {
    User save(User user);
}
