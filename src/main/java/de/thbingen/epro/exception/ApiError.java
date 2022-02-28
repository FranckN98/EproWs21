package de.thbingen.epro.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class represents the Response the User gets, when an Exception is thrown, for example {@link javax.persistence.EntityNotFoundException}
 */
public class ApiError {

    /**
     * The HttpStatus that is returned with the error
     */
    private HttpStatus httpStatus;
    /**
     * The time at which the error occured
     */
    private LocalDateTime timestamp;
    /**
     * An error message that can be displayed to the user
     */
    private String message;
    /**
     * An error message, which can be sent to the developers for debugging and fixing possible bugs
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String debugMessage;
    /**
     * A list of errors, if multiple occured, like when multiple fields were invalid in validation
     */
    private List<ApiSubError> errors;

    /**
     * Creates an ApiError with the current Timestamp and the message "Unexpected Error"
     */
    private ApiError() {
        timestamp = LocalDateTime.now();
        this.message = "Unexpected error";
    }

    /**
     * Creates an ApiError with the given HttpStatus
     *
     * @param httpStatus The HttpStatus, which will be returned with this error
     */
    public ApiError(HttpStatus httpStatus) {
        this();
        this.httpStatus = httpStatus;
    }

    /**
     * Creates an ApiError with the given HttpStatus and a debug message, which is extracted from the Throwable
     *
     * @param httpStatus The HttpStatus, which will be returned with this error
     * @param exception  The Throwable from which the DebugMessage will be taken
     */
    public ApiError(HttpStatus httpStatus, Throwable exception) {
        this(httpStatus);
        this.debugMessage = exception.getLocalizedMessage();
    }

    /**
     * Creates an ApiError with the given HttpStatus, the given message and a debug message,
     * which is extracted from the Throwable
     *
     * @param httpStatus The HttpStatus, which will be returned with this error
     * @param message    The message, which will be displayed to the user
     * @param exception  The Throwable from which the DebugMessage will be taken
     */
    public ApiError(HttpStatus httpStatus, String message, Throwable exception) {
        this(httpStatus, exception);
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public List<ApiSubError> getErrors() {
        return errors;
    }

    public void setErrors(List<ApiSubError> errors) {
        this.errors = errors;
    }

    /**
     * Adds an Error to the List of ApiSubErrors
     *
     * @param errors the errors to be added
     */
    public void addErrors(List<ApiSubError> errors) {
        if (this.errors == null) {
            setErrors(errors);
            return;
        }
        this.errors.addAll(errors);
    }
}
