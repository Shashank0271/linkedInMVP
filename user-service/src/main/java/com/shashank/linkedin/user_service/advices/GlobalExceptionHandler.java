package com.shashank.linkedin.user_service.advices;


import com.shashank.linkedin.user_service.exceptions.BadRequestException;
import com.shashank.linkedin.user_service.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ApiError apiError = ApiError.builder().
                message(exception.getMessage()).
                httpStatus(HttpStatus.NOT_FOUND).
                build();
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException exception) {
        ApiError apiError = ApiError.builder().
                message(exception.getMessage()).
                build();
        return new ResponseEntity<>(apiError,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException exception) {
        ApiError apiError = ApiError.builder().
                httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage()).
                build();
        return new ResponseEntity<>(apiError,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
