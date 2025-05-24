package com.broers.prueba_tecnica.utils.exceptions;

public class AccessDeniedException extends java.nio.file.AccessDeniedException  {
    public AccessDeniedException(String message) {
        super(message);
    }
}
