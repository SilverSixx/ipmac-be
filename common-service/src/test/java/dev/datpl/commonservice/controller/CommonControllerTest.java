package dev.datpl.commonservice.controller;

import dev.datpl.commonservice.config.ConfigProperties;
import dev.datpl.commonservice.config.Oauth2ConfigProperties;
import dev.datpl.commonservice.pojo.request.UserCreationRequest;
import dev.datpl.commonservice.pojo.response.UserResponse;
import dev.datpl.commonservice.security.RequestIDFilter;
import dev.datpl.commonservice.security.SecurityConfig;
import dev.datpl.commonservice.service.IOauth2Service;
import dev.datpl.commonservice.utils.UserDataProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebFluxTest(controllers = CommonController.class)
@Import({SecurityConfig.class, RequestIDFilter.class, Oauth2ConfigProperties.class, ConfigProperties.class})
@ActiveProfiles("test")
public class CommonControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private IOauth2Service oauth2Service;

    @Test
    void healthCheck_shouldReturnOk() {
        webTestClient.get()
                .uri("/api/common/health")
                .header("X-Requested-With", "test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Common service is running");
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void listUsers_shouldReturnListOfUsers() {
        UserResponse userResponse = UserDataProvider.createUserResponse();
        List<UserResponse> userList = Collections.singletonList(userResponse);

        Mockito.when(oauth2Service.getAllUsers()).thenReturn(userList);

        webTestClient.get()
                .uri("/api/common/users")
                .header("X-Requested-With", "test")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(UserResponse.class).isEqualTo(userList);

        verify(oauth2Service, times(1)).getAllUsers();
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void createUser_shouldReturnCreatedUser() {
        UserCreationRequest creationRequest = UserDataProvider.userCreationRequest();
        UserResponse createdUser = UserDataProvider.createUserResponse();

        Mockito.when(oauth2Service.createUser(any(UserCreationRequest.class))).thenReturn(createdUser);

        webTestClient.post()
                .uri("/api/common/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Requested-With", "test")
                .body(Mono.just(creationRequest), UserCreationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserResponse.class).isEqualTo(createdUser);

        verify(oauth2Service, times(1)).createUser(any(UserCreationRequest.class));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void deleteUser_shouldReturnNoContent() {
        String userId = "123";

        webTestClient.delete()
                .uri("/api/common/users/{userId}", userId)
                .header("X-Requested-With", "test")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        verify(oauth2Service, times(1)).deleteUser(userId);
    }

    @Test
    void listUsers_whenUnauthenticated_shouldReturnUnauthorized() {
        webTestClient.get()
                .uri("/api/common/users")
                .header("X-Requested-With", "test")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER") // Role without proper permission
    void listUsers_withInsufficientAuthority_shouldReturnForbidden() {
        webTestClient.get()
                .uri("/api/common/users")
                .header("X-Requested-With", "test")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities = "ROLE_TRAINER") // Another insufficient role
    void createUser_withInsufficientAuthority_shouldReturnForbidden() {
        UserCreationRequest creationRequest = UserDataProvider.userCreationRequest();

        webTestClient.post()
                .uri("/api/common/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Requested-With", "test")
                .body(Mono.just(creationRequest), UserCreationRequest.class)
                .exchange()
                .expectStatus().isForbidden();
    }

}
