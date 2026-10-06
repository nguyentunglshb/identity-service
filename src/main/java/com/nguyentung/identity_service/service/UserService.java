package com.nguyentung.identity_service.service;

import com.nguyentung.identity_service.dto.request.UserCreationRequest;
import com.nguyentung.identity_service.dto.request.UserUpdateRequest;
import com.nguyentung.identity_service.entity.User;
import com.nguyentung.identity_service.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public User createRequest(UserCreationRequest req) {
    User user = new User();

    user.setUsername(req.getUsername());
    user.setPassword(req.getPassword());
    user.setFirstName(req.getFirstName());
    user.setLastName(req.getLastName());
    user.setDob(req.getDob());

    return userRepository.save(user);
  }

  public List<User> getUsers() {
    return userRepository.findAll();
  }

  public User getUser(String userId) {
    return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
  }

  public User updateUser(String userId, UserUpdateRequest req) {
    User user = getUser(userId);

    user.setPassword(req.getPassword());
    user.setFirstName(req.getFirstName());
    user.setLastName(req.getLastName());
    user.setDob(req.getDob());

    return userRepository.save(user);
  }

  public void deleteUser(String userId) {
    userRepository.deleteById(userId);
  }
}
