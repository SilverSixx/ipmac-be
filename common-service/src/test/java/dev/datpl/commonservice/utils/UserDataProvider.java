package dev.datpl.commonservice.utils;

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

}
