package com.onulstore.config.exception;

import com.onulstore.domain.enums.ErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Exception extends RuntimeException {

  private final ErrorResult errorResult;

}