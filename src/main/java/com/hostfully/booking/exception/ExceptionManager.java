package com.hostfully.booking.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionManager extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleNotFoundException(NotFoundException ex) {
        return createErrorInfo(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = { conflictException.class })
    public ResponseEntity<Object> handleConflict(conflictException ex, WebRequest request) {
        return handleExceptionInternal(ex, createErrorInfo(ex.getMessage()), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, HttpServletRequest request) {
        // set the status code to 500
        request.setAttribute("javax.servlet.error.status_code", HttpStatus.INTERNAL_SERVER_ERROR.value());

        // return the error view
        return "error";
    }

    // Helper method to create an ErrorInfo object
    private ErrorInfo createErrorInfo(String message) {
        return new ErrorInfo(message);
    }

    // Exception class to be used for all exceptions that should result in a 404 Not Found response
    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    // Exception class to be used for all exceptions that should result in a 409 Conflict response
    public static class conflictException extends RuntimeException {
        public conflictException(String message) {
            super(message);
        }
    }

    // Wrapper class for error messages
    public static class ErrorInfo {
        private String message;

        public ErrorInfo(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
