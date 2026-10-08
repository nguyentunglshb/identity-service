package com.nguyentung.identity_service.controller;

import com.nguyentung.identity_service.dto.ApiResponse;
import com.nguyentung.identity_service.dto.request.UserCreationRequest;
import com.nguyentung.identity_service.dto.request.UserUpdateRequest;
import com.nguyentung.identity_service.dto.response.UserResponse;
import com.nguyentung.identity_service.entity.User;
import com.nguyentung.identity_service.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
  UserService userService;

  @PostMapping()
  ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest req) {
    ApiResponse<User> apiResponse = new ApiResponse<>();
    apiResponse.setResult(userService.createUser(req));
    return apiResponse;
  }

  @GetMapping()
  List<User> getUsers() {
    return userService.getUsers();
  }

  @GetMapping("/{userId}")
  UserResponse getUser(@PathVariable("userId") String userId) {
    return userService.getUser(userId);
  }

  @PutMapping("/{userId}")
  UserResponse updateuser(@PathVariable String userId, @RequestBody UserUpdateRequest req) {
    return userService.updateUser(userId, req);
  }

  @DeleteMapping("/{userId}")
  String deleteUser(@PathVariable String userId) {
     userService.deleteUser(userId);
     return "User has been deleted";
  }
}
