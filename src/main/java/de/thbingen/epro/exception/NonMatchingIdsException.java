package de.thbingen.epro.exception;

public class NonMatchingIdsException extends RuntimeException {
    public NonMatchingIdsException(String errorMessage) {
        super(errorMessage);
    }
}
