package com.tournament_organizer.exception.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> bodyValidation(MethodArgumentNotValidException exception, WebRequest request) {
        String msg = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::format)
                .collect(Collectors.joining("; "));
        return build(HttpStatus.BAD_REQUEST, msg, request);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> paramValidation(ConstraintViolationException exception, WebRequest request) {
        String msg = exception.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .collect(Collectors.joining("; "));
        return build(HttpStatus.BAD_REQUEST, msg, request);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> badJson(HttpMessageNotReadableException exception, WebRequest request) {
        String msg = "Malformed JSON: " + exception.getMostSpecificCause().getMessage();
        return build(HttpStatus.BAD_REQUEST, msg, request);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> notFound(EntityNotFoundException exception, WebRequest request) {
        return build(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> conflict(IllegalStateException exception, WebRequest request) {
        return build(HttpStatus.CONFLICT, exception.getMessage(), request);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException exception, WebRequest request) {
        return build(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<Object> forbidden(
            AccessDeniedException ex, WebRequest req) {
        return build(HttpStatus.FORBIDDEN, "Access denied", req);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> unhandled(Exception exception, WebRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", request);
    }

    private String format(FieldError fe) {
        return "%s %s".formatted(fe.getField(), fe.getDefaultMessage());
    }
    private ResponseEntity<Object> build(HttpStatus status, String message, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return ResponseEntity.status(status)
                .body(ApiError.of(status, message, path));
    }
}