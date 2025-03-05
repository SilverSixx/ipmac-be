package dev.datpl.commonservice.service;

import dev.datpl.commonservice.exception.UserCreationException;
import dev.datpl.commonservice.pojo.dto.UserRepresentationDto;
import dev.datpl.commonservice.pojo.response.UserResponse;

import java.util.List;

public interface IOauth2Service {
    UserResponse createUser(UserRepresentationDto userRepresentation) throws UserCreationException;
    List<UserResponse> getAllUsers();
    void deleteUser(String userId);
}
