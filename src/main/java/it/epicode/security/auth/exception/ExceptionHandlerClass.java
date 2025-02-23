package it.epicode.security.auth.exception;


import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@ControllerAdvice
public class ExceptionHandlerClass extends ResponseEntityExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionMessage> handleIllegalArgumentException(IllegalArgumentException e) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        exceptionMessage.setStatus("400");
        exceptionMessage.setError("Bad Request: "+e.getMessage());

        return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleUserNotFoundException(UsernameNotFoundException e) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        exceptionMessage.setStatus("404");
        exceptionMessage.setError("User Not Found: " + e.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { EntityNotFoundException.class })
    protected ResponseEntity<ExceptionMessage> handleEntityNotFoundException(EntityNotFoundException e) {

        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        exceptionMessage.setStatus("404");
        exceptionMessage.setError("Not Found");
        return new ResponseEntity<ExceptionMessage>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = EntityExistsException.class)
    protected ResponseEntity<ExceptionMessage> handleEntityExistsException(EntityExistsException e) {

        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        exceptionMessage.setStatus("409");
        exceptionMessage.setError("Conflict: " + e.getMessage());
        return new ResponseEntity<ExceptionMessage>(exceptionMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionMessage> handleConsraintViolationException(ConstraintViolationException e,
                                                                              HttpServletRequest request) {
        Map<String, String> errors = new HashMap<String, String>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();

            if (fieldName.contains(".")) {
                fieldName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
            }
            errors.put(fieldName, violation.getMessage());
        }

        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage("Validation failed");
        exceptionMessage.setStatus("400");
        exceptionMessage.setError(errors);

        return new ResponseEntity<ExceptionMessage>(exceptionMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    protected ResponseEntity<ExceptionMessage> AccessDenied(AccessDeniedException e) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage("Access denied");
        exceptionMessage.setStatus("403");
        exceptionMessage.setError(e.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = SecurityException.class)
    protected ResponseEntity<ExceptionMessage> handlerSecurityException(SecurityException e) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage("Access denied: " + e.getMessage());
        exceptionMessage.setStatus("401");
        exceptionMessage.setError(e.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNAUTHORIZED);
    }
}