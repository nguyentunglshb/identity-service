package com.nguyentung.identity_service.service;

import com.nguyentung.identity_service.dto.request.UserCreationRequest;
import com.nguyentung.identity_service.dto.request.UserUpdateRequest;
import com.nguyentung.identity_service.dto.response.UserResponse;
import com.nguyentung.identity_service.entity.User;
import com.nguyentung.identity_service.enums.Role;
import com.nguyentung.identity_service.exception.AppException;
import com.nguyentung.identity_service.exception.ErrorCode;
import com.nguyentung.identity_service.mapper.UserMapper;
import com.nguyentung.identity_service.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
  UserRepository userRepository;
  UserMapper userMapper;
  PasswordEncoder passwordEncoder;

  public UserResponse createUser(UserCreationRequest req) {
    if(userRepository.existsByUsername(req.getUsername())) {
      throw new AppException(ErrorCode.USER_EXISTED);
    }

    User user = userMapper.toUser(req);

    user.setPassword(passwordEncoder.encode(req.getPassword()));

    HashSet<String> roles = new HashSet<>();
    roles.add(Role.USER.name());
    user.setRoles(roles);

    return userMapper.toUserResponse(userRepository.save(user));
  }

  public List<UserResponse> getUsers() {
    return userMapper.toUserResponses(userRepository.findAll());
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
