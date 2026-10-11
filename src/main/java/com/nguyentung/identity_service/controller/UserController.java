package com.nguyentung.identity_service.controller;

import com.nguyentung.identity_service.dto.ApiResponse;
import com.nguyentung.identity_service.dto.request.UserCreationRequest;
import com.nguyentung.identity_service.dto.request.UserUpdateRequest;
import com.nguyentung.identity_service.dto.response.UserResponse;
import com.nguyentung.identity_service.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
  UserService userService;

  @PostMapping()
  ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest req) {
    return ApiResponse.<UserResponse>builder()
        .result(userService.createUser(req))
        .build();
  }

  @GetMapping()
  ApiResponse<List<UserResponse>> getUsers(){
    var authentication = SecurityContextHolder.getContext().getAuthentication();

    log.info("Username: {}", authentication.getName());
    authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

    return ApiResponse.<List<UserResponse>>builder().result(userService.getUsers()).build();
  }

  @GetMapping("/{userId}")
  ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
    return ApiResponse.<UserResponse>builder().result(userService.getUser(userId)).build();
  }

  @PutMapping("/{userId}")
  ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest req) {
    return ApiResponse.<UserResponse>builder().result(userService.updateUser(userId, req)).build();
  }

  @DeleteMapping("/{userId}")
  ApiResponse<String> deleteUser(@PathVariable String userId) {
     userService.deleteUser(userId);
     return ApiResponse.<String>builder().result("User has been deleted").build();
  }
}
