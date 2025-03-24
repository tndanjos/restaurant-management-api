package br.com.fiap.techchallenge.restaurantmanagementapi.exception;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleEntityNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");
        ResponseEntity<String> response = handler.handleEntityNotFound(ex);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Entity not found", response.getBody());
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");
        ResponseEntity<String> response = handler.handleIllegalArgumentException(ex);

        assertEquals(CONFLICT, response.getStatusCode());
        assertEquals("Invalid argument", response.getBody());
    }

    @Test
    void testHandleDataIntegrityViolation_withoutConstraintViolationMessage() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Some other error");
        ResponseEntity<String> response = handler.handleDataIntegrityViolation(ex);

        assertEquals(CONFLICT, response.getStatusCode());
        assertEquals("Duplicate value detected for a unique field.", response.getBody());
    }

    @Test
    void testHandleDataIntegrityViolation_withConstraintViolationMessage() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("org.hibernate.exception.ConstraintViolationException: ...");
        ResponseEntity<String> response = handler.handleDataIntegrityViolation(ex);

        assertEquals(CONFLICT, response.getStatusCode());
        assertEquals("One of the fields violates a unique constraint. Check 'email' or 'username'.", response.getBody());
    }

    @Test
    void testHandleValidationExceptions() {
        var target = new Object();
        var bindingResult = new BeanPropertyBindingResult(target, "objectName");
        bindingResult.addError(new FieldError("objectName", "email", "Email is required"));
        bindingResult.addError(new FieldError("objectName", "username", "Username is required"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<Map<String, String>> response = handler.handleValidationExceptions(ex);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        Map<String, String> body = response.getBody();

        assertNotNull(body);
        assertEquals(2, body.size());
        assertEquals("Email is required", body.get("email"));
        assertEquals("Username is required", body.get("username"));
    }
}
