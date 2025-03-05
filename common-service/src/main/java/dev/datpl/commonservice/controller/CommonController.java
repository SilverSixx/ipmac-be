package dev.datpl.commonservice.controller;

import dev.datpl.commonservice.pojo.dto.UserRepresentationDto;
import dev.datpl.commonservice.pojo.response.UserResponse;
import dev.datpl.commonservice.service.IOauth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common")
public class CommonController {

    private final IOauth2Service oauth2Service;

    @GetMapping
    @RequestMapping("/health")
    public Mono<ResponseEntity<?>> healthCheck() {
        return Mono.just(ResponseEntity.ok("Common service is running"));
    }

    @GetMapping("/users")
    public Mono<ResponseEntity<List<UserResponse>>> listUsers() {
        return Mono.just(ResponseEntity.ok(oauth2Service.getAllUsers()));
    }

    @PostMapping("/users")
    public Mono<ResponseEntity<UserResponse>> createUser(@Validated @RequestBody UserRepresentationDto userRepresentation) {
        return Mono.just(ResponseEntity.ok(oauth2Service.createUser(userRepresentation)));
    }

    @DeleteMapping("/users/{userId}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String userId) {
        oauth2Service.deleteUser(userId);
        return Mono.just(ResponseEntity.noContent().build());
    }

}
