package dev.datpl.commonservice.utils;

import dev.datpl.commonservice.pojo.request.UserCreationRequest;
import dev.datpl.commonservice.pojo.response.UserResponse;

public class UserDataProvider {

    public static UserResponse createUserResponse() {
        return UserResponse.builder()
                .id("id")
                .username("username")
                .email("email")
                .firstName("firstName")
                .lastName("lastName")
                .roles(null)
                .build();
    }

    public static UserCreationRequest userCreationRequest(){
        return UserCreationRequest.builder()
                .username("username")
                .email("email@email.com")
                .password("password")
                .firstName("firstName")
                .lastName("lastName")
                .role("admin")
                .build();

    }

}
