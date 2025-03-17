package dev.datpl.trainingservice.consumer;

import dev.datpl.trainingservice.pojo.dto.UserCreationEvent;
import dev.datpl.trainingservice.pojo.entity.User;
import dev.datpl.trainingservice.pojo.entity.UserFactory;
import dev.datpl.trainingservice.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeycloakUserConsumerTest {

    @Mock
    private UserFactory mockUserFactory;
    @Mock
    private IUserService mockUserService;

    private KeycloakUserConsumer keycloakUserConsumerUnderTest;

    @BeforeEach
    void setUp() {
        keycloakUserConsumerUnderTest = new KeycloakUserConsumer(mockUserFactory, mockUserService);
    }

    @Test
    void testConsumeUserCreationEvent() {
        // Setup
        final UserCreationEvent event = UserCreationEvent.builder()
                .userId("userId")
                .username("username")
                .email("email")
                .firstName("firstName")
                .lastName("lastName")
                .role("role")
                .build();
        when(mockUserFactory.getUser("role")).thenReturn(null);

        // Run the test
        keycloakUserConsumerUnderTest.consumeUserCreationEvent(event);

        // Verify the results
        verify(mockUserService).handleUserCreationEvent(any(User.class));
    }
}
