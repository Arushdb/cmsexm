package edu.dei.examination.phd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import edu.dei.examination.phd.dto.ApiResponse;

@RestControllerAdvice(basePackages = "edu.dei.examination.phd")
public class RestExceptionHandler {

    @ExceptionHandler(ScholarValidationException.class)
    public ResponseEntity<ApiResponse<?>> handleScholarValidation(
            ScholarValidationException ex) {
    	
    

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneric(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal server error", null));
    }
}

