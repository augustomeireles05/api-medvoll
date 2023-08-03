package br.com.med.voll.api.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DatabaseAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DatabaseAccessException(String message) {
        super(message);
    }

    public DatabaseAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
