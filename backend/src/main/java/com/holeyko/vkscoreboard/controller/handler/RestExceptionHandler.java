package com.holeyko.vkscoreboard.controller.handler;

import com.holeyko.vkscoreboard.exception.ResourceNotFoundException;
import com.holeyko.vkscoreboard.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void resourceNotFound() {
        // Not Found
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void illegalArgument() {
        // Illegal argument
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, ?> errorMessage(ValidationException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        return fromErrorsToMap(bindingResult.getAllErrors());
    }

    private Map<String, ?> fromErrorsToMap(List<? extends ObjectError> errors) {
        Map<String, Object> result = new HashMap<>();
        for (var error : errors) {
            if (error instanceof FieldError fieldError) {
                result.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                ((ArrayList<String>) (result.computeIfAbsent("errors", k -> new ArrayList<String>())))
                        .add(error.getDefaultMessage());
            }
        }

        return result;
    }
}
