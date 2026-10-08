package com.nguyentung.identity_service.service;

import com.nguyentung.identity_service.dto.request.AuthenticationRequest;
import com.nguyentung.identity_service.exception.AppException;
import com.nguyentung.identity_service.exception.ErrorCode;
import com.nguyentung.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
  UserRepository userRepository;

  public boolean authenticated(AuthenticationRequest req) {
    var user = userRepository.findByUsername(req.getUsername())
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder((10));
    return passwordEncoder.matches(req.getPassword(), user.getPassword());
  }

}
