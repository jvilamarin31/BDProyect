package com.apirest.backend.Exception;

public class InvalidRespuestaConfigurationException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public InvalidRespuestaConfigurationException(String message) {
        super(message);
    }
}
