package com.onulstore.domain.enums;

import org.springframework.http.HttpStatus;

public interface SuperErrorResult {

  HttpStatus getHttpStatus();
  String getMessage();
  String getName();

}
