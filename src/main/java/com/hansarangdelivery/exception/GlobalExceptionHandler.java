package com.hansarangdelivery.exception;

import com.hansarangdelivery.dto.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ExceptionResponseDto> handleDuplicateResourceException(DuplicateResourceException ex) {
        ExceptionResponseDto responseDto = new ExceptionResponseDto(ex.getMessage(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(responseDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<ExceptionResponseDto> handleForbiddenActionException(ForbiddenActionException ex) {
        ExceptionResponseDto responseDto = new ExceptionResponseDto(ex.getMessage(), HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ExceptionResponseDto responseDto = new ExceptionResponseDto(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ExceptionResponseDto responseDto = new ExceptionResponseDto(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<String>> handleBindException(BindException ex) { // Validation 예외
        List<FieldError> fieldErrors = ex.getFieldErrors();
        List<String> errors = new ArrayList<>();
        for(FieldError fieldError : fieldErrors){
            errors.add(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(
            errors,
            HttpStatus.BAD_REQUEST
        );
    }
}
