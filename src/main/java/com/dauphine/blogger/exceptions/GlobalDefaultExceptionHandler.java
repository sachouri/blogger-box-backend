package com.dauphine.blogger.exceptions;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    // 404 Not Found
    @ExceptionHandler({
            CategoryNotFoundByIdException.class,
            PostNotFoundByIdException.class
    })
    public ResponseEntity<String> handleNotFoundException(Exception ex) {
        logger.warn("[NOT FOUND] {}", ex.getMessage());
        return ResponseEntity
                .status(404)
                .body(ex.getMessage());
    }

    // 409 Conflict (e.g. duplicate names)
    @ExceptionHandler({
            CategoryNameAlreadyExistsException.class,
            PostNameAlreadyExistsException.class,
            DataIntegrityViolationException.class
    })
    public ResponseEntity<String> handleConflict(Exception ex) {
        logger.warn("[CONFLICT] {}", ex.getMessage());
        return ResponseEntity
                .status(409)
                .body(ex.getMessage());
    }

    // 400 Bad Request
    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class,
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<String> handleBadRequest(Exception ex) {
        logger.warn("[BAD REQUEST] {}", ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        logger.warn("[VALIDATION FAILED] {}", errors);
        return ResponseEntity
                .badRequest()
                .body(errors);
    }
    /*
    // 500 Internal Server Error (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneral(Exception ex) {
        logger.error("[INTERNAL ERROR]", ex);
        return ResponseEntity
                .status(500)
                .body("An unexpected error occurred.");
    }
    */
}