package com.apirest.backend.Exception;

public class InvalidReplicaConfigurationException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public InvalidReplicaConfigurationException(String message) {
        super(message);
    }

}
