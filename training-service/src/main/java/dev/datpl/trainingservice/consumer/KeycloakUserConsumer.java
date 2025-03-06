package dev.datpl.trainingservice.consumer;

import dev.datpl.trainingservice.pojo.dto.UserCreationEvent;
import dev.datpl.trainingservice.pojo.entity.User;
import dev.datpl.trainingservice.pojo.entity.UserFactory;
import dev.datpl.trainingservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserConsumer {
    private final UserFactory userFactory;
    private final IUserService userService;

    @KafkaListener(topics = "${kafka.topics.user-creation}", groupId = "${kafka.group-id}")
    public void consumeUserCreationEvent(UserCreationEvent event) {
        User u = userFactory.getUser(event.getRole());
        u.setUserId(event.getUserId());
        u.setUsername(event.getUsername());
        u.setEmail(event.getEmail());
        u.setRole(event.getRole());
        u.setFirstName(event.getFirstName());
        u.setLastName(event.getLastName());

        userService.handleUserCreationEvent(u);
    }
}
