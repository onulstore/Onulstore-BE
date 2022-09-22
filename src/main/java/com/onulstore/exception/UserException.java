package com.onulstore.exception;

import com.onulstore.domain.enums.UserErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserException extends SuperException {

  private final UserErrorResult errorResult;

}