package com.nguyentung.identity_service.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
  UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception"),
  KEY_INVALID(1001, "Invalid message key"),
  USER_EXISTED(1002, "User existed"),
  USERNAME_INVALID(1003, "Username must be at least 3 characters"),
  PASSWORD_INVALID(1004, "Password must be at least 8 characters"),
  USER_NOT_EXISTED(1005, "User not existed")
  ;

  private final int code;
  private final String message;

  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

}
