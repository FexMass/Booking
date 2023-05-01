package com.hostfully.booking;

import com.hostfully.booking.exception.ExceptionManager;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExceptionManagerTest {

    @Test
    public void testHandleBookingConflictException() {
        ExceptionManager exceptionManager = new ExceptionManager();
        String message = "There is already a booking during the requested time period.";
        ExceptionManager.conflictException exception = new ExceptionManager.conflictException(message);
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        WebRequest webRequest = new ServletWebRequest(servletRequest);

        ResponseEntity<Object> response = exceptionManager.handleConflict(exception, webRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ExceptionManager.ErrorInfo errorInfo = (ExceptionManager.ErrorInfo) response.getBody();
        assert errorInfo != null;
        assertEquals(message, errorInfo.getMessage());
    }
}
