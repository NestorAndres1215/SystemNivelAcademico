package com.example.exception;

public class ValidacionException extends RuntimeException {

    public ValidacionException() {
        super();
    }

    public ValidacionException(String mensaje) {
        super(mensaje);
    }

    public ValidacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}