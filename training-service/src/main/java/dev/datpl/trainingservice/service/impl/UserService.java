package dev.datpl.trainingservice.service.impl;

import dev.datpl.trainingservice.pojo.entity.User;
import dev.datpl.trainingservice.pojo.entity.UserFactory;
import dev.datpl.trainingservice.repository.UserRepository;
import dev.datpl.trainingservice.repository.UserRepositoryFactory;
import dev.datpl.trainingservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepositoryFactory userRepositoryFactory;

    @Override
    public void handleUserCreationEvent(User user) {
        if (user != null) {
            UserRepository repository = userRepositoryFactory.getUserRepository(user.getRole());
            if (repository != null) {
                repository.save(user);
            }
        }
    }
}
