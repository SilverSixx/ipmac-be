package dev.datpl.trainingservice.consumer;

import dev.datpl.trainingservice.pojo.dto.UserCreationEvent;
import dev.datpl.trainingservice.pojo.entity.Trainee;
import dev.datpl.trainingservice.pojo.entity.User;
import dev.datpl.trainingservice.pojo.entity.UserFactory;
import dev.datpl.trainingservice.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakUserConsumerTest {

    @Mock
    private UserFactory userFactory;
    @Mock
    private IUserService userService;

    @InjectMocks
    private KeycloakUserConsumer keycloakUserConsumerUnderTest;

    @BeforeEach
    void setUp() {
        keycloakUserConsumerUnderTest = new KeycloakUserConsumer(userFactory, userService);
    }

    @Test
    void consumeUserCreationEvent_ValidEvent_UserCreated() {
        UserCreationEvent event = new UserCreationEvent();
        event.setUserId("userId");
        event.setUsername("username");
        event.setEmail("email@example.com");
        event.setRole("role");
        event.setFirstName("firstName");
        event.setLastName("lastName");

        User user = new Trainee();
        when(userFactory.getUser(event.getRole())).thenReturn(user);

        keycloakUserConsumerUnderTest.consumeUserCreationEvent(event);

        verify(userFactory).getUser(event.getRole());
        verify(userService).handleUserCreationEvent(user);
    }

}
