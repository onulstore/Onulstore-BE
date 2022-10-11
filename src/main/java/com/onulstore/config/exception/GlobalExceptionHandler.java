package com.onulstore.config.exception;

import com.onulstore.domain.enums.SuperErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> exception(CustomException CustomException) {
        return createErrorResponse(CustomException.getCustomErrorResult());
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(SuperErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getHttpStatus())
            .body(new ErrorResponse(errorResult.getName(), errorResult.getMessage()));
    }

    @Getter
    @RequiredArgsConstructor
    static class ErrorResponse {

        private final String code;
        private final String message;
    }
}