package dev.datpl.commonservice.controller;

import dev.datpl.commonservice.pojo.response.UserResponse;
import dev.datpl.commonservice.service.IOauth2Service;
import dev.datpl.commonservice.utils.UserDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class BDDCommonControllerTest {

    @InjectMocks
    private CommonController commonControllerUnderTest;

    @Mock
    private IOauth2Service mockOauth2Service;

    private final UserResponse mockUserResponse = UserDataProvider.createUserResponse();

    @BeforeEach
    public void setUp() {
        BDDMockito.when(
                mockOauth2Service.getAllUsers()
        ).thenReturn(List.of(mockUserResponse));

        BDDMockito.when(
                mockOauth2Service.createUser(BDDMockito.any())
        ).thenReturn(mockUserResponse);

        BDDMockito.doNothing().when(mockOauth2Service).deleteUser(ArgumentMatchers.anyString());
    }

    @Test
    void testHealthCheck() {
        StepVerifier.create(commonControllerUnderTest.healthCheck())
                .expectNextMatches(response -> {
                    assertEquals(200, response.getStatusCode().value());
                    assertEquals("Common service is running", response.getBody());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testListUsers() {
        StepVerifier.create(commonControllerUnderTest.listUsers())
                .expectNextMatches(response -> {
                    assertEquals(200, response.getStatusCode().value());
                    assertEquals(List.of(mockUserResponse), response.getBody());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testCreateUser() {
        StepVerifier.create(commonControllerUnderTest.createUser(BDDMockito.any()))
                .expectNextMatches(response -> {
                    assertEquals(200, response.getStatusCode().value());
                    assertEquals(mockUserResponse, response.getBody());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testDeleteUser() throws Exception {
        StepVerifier.create(commonControllerUnderTest.deleteUser(BDDMockito.any()))
                .expectNextMatches(response -> {
                    assertEquals(204, response.getStatusCode().value());
                    return true;
                })
                .verifyComplete();
    }

}
