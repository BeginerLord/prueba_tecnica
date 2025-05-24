package com.broers.prueba_tecnica.utils.exceptions;

public class SeeOtherException extends RuntimeException {
    public SeeOtherException(String message) {
        super(message);
    }

    public SeeOtherException(String message, Throwable cause) {
        super(message, cause);
    }
}
