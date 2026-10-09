package com.nguyentung.identity_service.service;

import com.nguyentung.identity_service.dto.request.AuthenticationRequest;
import com.nguyentung.identity_service.dto.request.IntrospectRequest;
import com.nguyentung.identity_service.dto.response.AuthenticationResponse;
import com.nguyentung.identity_service.dto.response.IntrospectResponse;
import com.nguyentung.identity_service.exception.AppException;
import com.nguyentung.identity_service.exception.ErrorCode;
import com.nguyentung.identity_service.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
  UserRepository userRepository;

  @NonFinal
  @Value("${jwt.signer-key}")
  protected String SIGNER_KEY;

  public IntrospectResponse introspect(IntrospectRequest req) throws JOSEException, ParseException {
    var token = req.getToken();

    JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

    SignedJWT signedJWT = SignedJWT.parse(token);

    Date expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();

    var verified = signedJWT.verify(verifier);

    return  IntrospectResponse.builder()
        .valid(verified && expireTime.after(new Date()))
        .build();
  }

  public AuthenticationResponse authenticated(AuthenticationRequest req) {
    var user = userRepository.findByUsername(req.getUsername())
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder((10));
    boolean authenticated = passwordEncoder.matches(req.getPassword(), user.getPassword());

    if(!authenticated) {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    var token = generateToken(req.getUsername());

    return AuthenticationResponse.builder()
        .token(token)
        .authenticated(true)
        .build();
  }

  private String generateToken(String username) {
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
    JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
        .subject(username)
        .issuer("nguyentung")
        .issueTime(new Date())
        .expirationTime(new Date(
            Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
        ))
        .claim("customClaim", "Custom")
        .build();
    Payload payload = new Payload(jwtClaimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(header, payload);

    try {
      jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes(StandardCharsets.UTF_8)));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      log.error("Cannot create token", e);
      throw new RuntimeException(e);
    }
  }

}
