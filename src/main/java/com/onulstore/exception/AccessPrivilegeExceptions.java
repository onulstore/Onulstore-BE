package com.onulstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AccessPrivilegeExceptions extends RuntimeException {

    public AccessPrivilegeExceptions(String msg) {
        super(msg);
    }

}
