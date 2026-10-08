package com.nguyentung.identity_service.controller;

import com.nguyentung.identity_service.dto.ApiResponse;
import com.nguyentung.identity_service.dto.request.AuthenticationRequest;
import com.nguyentung.identity_service.dto.response.AuthenticationResponse;
import com.nguyentung.identity_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

  AuthenticationService authenticationService;

  @PostMapping("/login")
  ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest req) {
    boolean result = authenticationService.authenticated(req);
    return ApiResponse.<AuthenticationResponse>builder()
        .result(AuthenticationResponse.builder().authenticated(result).build())
        .build();
  }
}
