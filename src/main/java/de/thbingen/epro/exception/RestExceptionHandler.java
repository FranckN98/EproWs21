package de.thbingen.epro.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * This RestExceptionHandler handles the most common Exceptions that occur, when a user interacts with the application.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * This Method handles Errors, which are due to malformed json in the request body.
     *
     * @param ex      the Exception
     * @param headers the Headers to be written to the response
     * @param status  the selected Response status
     * @param request the current request
     * @return a Response Entity, that represents the error
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error, ex);
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    /**
     * Handles Errors, which are due to an entity being requested that is not present in the Database
     *
     * @param exception the exception
     * @return a ResponseEntity, that represents the error
     */
    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException exception) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(exception.getMessage());
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    /**
     * Handles Errors, which are due to an invalid timespan being given in the request.
     * An invalid timespan is for example one where the start date is before the enddate.
     *
     * @param exception the exception
     * @return a ResponseEntity, that represents the error
     */
    @ExceptionHandler(InvalidDateRangeException.class)
    ResponseEntity<Object> handleInvalidDateRange(InvalidDateRangeException exception) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Invalid Date Range. Make sure the Start Date is before the End Date");
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    /**
     * This Method handles Errors, which are due to invalid json in the request body.
     *
     * @param ex      the Exception
     * @param headers the Headers to be written to the response
     * @param status  the selected Response status
     * @param request the current request
     * @return a Response Entity, that represents the error
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Invalid JSON");
        apiError.addErrors(buildValidationError(ex.getFieldErrors()));
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    private List<ApiSubError> buildValidationError(List<FieldError> fieldErrors) {
        List<ApiSubError> apiValidationErrors = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            apiValidationErrors.add(new ApiValidationError(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage()));
        }
        return apiValidationErrors;
    }
}
