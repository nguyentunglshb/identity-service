package com.nguyentung.identity_service.controller;

import com.nguyentung.identity_service.dto.request.UserCreationRequest;
import com.nguyentung.identity_service.dto.request.UserUpdateRequest;
import com.nguyentung.identity_service.entity.User;
import com.nguyentung.identity_service.service.UserService;
import java.util.List;
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
public class UserController {
  @Autowired
  private UserService userService;

  @PostMapping()
  User createUser(@RequestBody UserCreationRequest req) {
    return userService.createRequest(req);
  }

  @GetMapping()
  List<User> getUsers() {
    return userService.getUsers();
  }

  @GetMapping("/{userId}")
  User getUser(@PathVariable("userId") String userId) {
    return userService.getUser(userId);
  }

  @PutMapping("/{userId}")
  User updateuser(@PathVariable String userId, @RequestBody UserUpdateRequest req) {
    return userService.updateUser(userId, req);
  }

  @DeleteMapping("/{userId}")
  String deleteUser(@PathVariable String userId) {
     userService.deleteUser(userId);
     return "User has been deleted";
  }
}
