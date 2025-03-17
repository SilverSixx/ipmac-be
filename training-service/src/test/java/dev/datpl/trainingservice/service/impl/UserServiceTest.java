package dev.datpl.trainingservice.service.impl;

import dev.datpl.trainingservice.pojo.entity.Trainee;
import dev.datpl.trainingservice.pojo.entity.User;
import dev.datpl.trainingservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryFactory mockUserRepositoryFactory;

    @Mock
    private TraineeRepository mockTraineeRepository;

    @Mock
    private UserRepository mockUserRepository;

    private UserService userServiceUnderTest;

    @BeforeEach
    void setUp() {
        userServiceUnderTest = new UserService(mockUserRepositoryFactory);
    }

    @Test
    void testHandleUserCreationEvent() {
        // Setup
        final User user = new Trainee();
        user.setRole("trainee");
        when(mockUserRepositoryFactory.getUserRepository(user.getRole())).thenReturn(mockTraineeRepository);

        // Run the test
        userServiceUnderTest.handleUserCreationEvent(user);

        // Verify the results
        verify(mockUserRepositoryFactory).getUserRepository("trainee");
        verify(mockTraineeRepository).save(user);
    }


    @Test
    void handleUserCreationEvent_NullUser_NoInteraction() {
        userServiceUnderTest.handleUserCreationEvent(null);

        verify(mockUserRepositoryFactory, never()).getUserRepository(anyString());
        verify(mockUserRepository, never()).save(any());
    }

    @Test
    void handleUserCreationEvent_UserWithNullRole_UserSaved() {
        User user = new Trainee();
        user.setRole(null);

        userServiceUnderTest.handleUserCreationEvent(user);

        verify(mockUserRepositoryFactory).getUserRepository(null);
    }

    @Test
    void handleUserCreationEvent_UserRepositoryNotFoundForRole_NoException() {
        User user = new Trainee();
        user.setRole("unknown");

        when(mockUserRepositoryFactory.getUserRepository("unknown")).thenReturn(null);

        userServiceUnderTest.handleUserCreationEvent(user);

        verify(mockUserRepositoryFactory).getUserRepository("unknown");
        verify(mockUserRepository, never()).save(user);
    }
}
