package com.apirest.backend.Exception;

public class InvalidSolicitudConfigurationException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public InvalidSolicitudConfigurationException(String message) {
        super(message);
    }
}
