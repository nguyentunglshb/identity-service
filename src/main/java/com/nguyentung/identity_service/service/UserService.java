package com.nguyentung.identity_service.service;

import com.nguyentung.identity_service.dto.request.UserCreationRequest;
import com.nguyentung.identity_service.dto.request.UserUpdateRequest;
import com.nguyentung.identity_service.dto.response.UserResponse;
import com.nguyentung.identity_service.entity.User;
import com.nguyentung.identity_service.exception.AppException;
import com.nguyentung.identity_service.exception.ErrorCode;
import com.nguyentung.identity_service.mapper.UserMapper;
import com.nguyentung.identity_service.repository.UserRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
  UserRepository userRepository;
  UserMapper userMapper;
  public User createUser(UserCreationRequest req) {
    if(userRepository.existsByUsername(req.getUsername())) {
      throw new AppException(ErrorCode.USER_EXISTED);
    }

    User user = userMapper.toUser(req);

    return userRepository.save(user);
  }

  public List<User> getUsers() {
    return userRepository.findAll();
  }

  public UserResponse getUser(String userId) {
    return userMapper.toUserResponse(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
  }

  public UserResponse updateUser(String userId, UserUpdateRequest req) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    userMapper.updateUser(user, req);

    return userMapper.toUserResponse(userRepository.save(user));
  }

  public void deleteUser(String userId) {
    userRepository.deleteById(userId);
  }
}
