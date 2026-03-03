package com.javarush.zdanovskih.publisher_service.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Data not saved", "Validation Error");
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        log.error(errors.toString());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleConstraint(DataIntegrityViolationException ex) {

        String msg = ex.getMostSpecificCause().getMessage();
        if (msg.contains("Duplicate") || msg.contains("повторяющееся значение ключа нарушает ограничение уникальности")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Data already exists");
        }
        if (msg.contains("foreign key")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Can not delete a linked record");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Data integrity error");
    }
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<?> handleTransactionException(TransactionSystemException ex) {
        Throwable cause = ex.getRootCause();
        if (cause instanceof jakarta.validation.ConstraintViolationException cve) {
            Map<String, String> errors = new HashMap<>();
            errors.put("Data not saved", "Validation Error");
            cve.getConstraintViolations().forEach(v ->
                    errors.put(v.getPropertyPath().toString(), v.getMessage())
            );
            log.error(errors.toString());

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errors);
        }
        log.error("Cannot save data: {}", ex.toString());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Cannot save data");
    }
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<?> handleJpaValidation(jakarta.validation.ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Data not saved", "Validation Error");
        ex.getConstraintViolations().forEach(v -> {
            String field = v.getPropertyPath().toString();
            String message = v.getMessage();
            errors.put(field, message);
        });
        log.error(errors.toString());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception exception) {
        log.error("Unexpected error: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred. Please try again later.");
    }
}
