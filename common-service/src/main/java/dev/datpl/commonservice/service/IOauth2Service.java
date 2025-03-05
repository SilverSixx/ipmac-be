package dev.datpl.commonservice.service;

import dev.datpl.commonservice.exception.UserCreationException;
import dev.datpl.commonservice.pojo.request.UserCreationRequest;
import dev.datpl.commonservice.pojo.response.UserResponse;

import java.util.List;

public interface IOauth2Service {
    UserResponse createUser(UserCreationRequest userRepresentation) throws UserCreationException;
    List<UserResponse> getAllUsers();
    void deleteUser(String userId);
}
