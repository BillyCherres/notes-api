package com.williamcherres.notes_api.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> firstMessagePerField = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(fe -> {
            firstMessagePerField.putIfAbsent(
                    fe.getField(),
                    fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value");
        });

        List<ApiError.FieldError> fieldErrors = firstMessagePerField.entrySet().stream()
                .map(e -> new ApiError.FieldError(e.getKey(), e.getValue()))
                .toList();

        ApiError body = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Request validation failed",
                request.getRequestURI(),
                fieldErrors);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NoteNotFoundException ex,
            HttpServletRequest request) {

        ApiError body = new ApiError(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI(),
                List.of());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    private ApiError.FieldError toApiFieldError(FieldError fe) {
        String msg = fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value";
        return new ApiError.FieldError(fe.getField(), msg);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ApiError> handleEmailInUse(EmailAlreadyInUseException ex,
            HttpServletRequest request) {

        ApiError body = new ApiError(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "EMAIL_IN_USE",
                ex.getMessage(),
                request.getRequestURI(),
                List.of());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentials(
            InvalidCredentialsException ex,
            HttpServletRequest request) {

        ApiError body = new ApiError(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "INVALID_CREDENTIALS",
                ex.getMessage(),
                request.getRequestURI(),
                List.of());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
}
