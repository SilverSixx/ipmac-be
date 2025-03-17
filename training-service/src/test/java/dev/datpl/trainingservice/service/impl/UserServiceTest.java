package dev.datpl.trainingservice.service.impl;

import dev.datpl.trainingservice.pojo.entity.User;
import dev.datpl.trainingservice.repository.UserRepositoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryFactory mockUserRepositoryFactory;

    private UserService userServiceUnderTest;

    @BeforeEach
    void setUp() {
        userServiceUnderTest = new UserService(mockUserRepositoryFactory);
    }

    @Test
    void testHandleUserCreationEvent() {
        // Setup
        final User user = null;
        when(mockUserRepositoryFactory.getUserRepository("role")).thenReturn(null);

        // Run the test
        userServiceUnderTest.handleUserCreationEvent(user);

        // Verify the results
    }
}
