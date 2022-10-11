package com.onulstore.config.exception;

import com.onulstore.domain.enums.CustomErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

  private final CustomErrorResult customErrorResult;

}