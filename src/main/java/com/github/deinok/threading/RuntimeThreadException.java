package com.github.deinok.threading;


import org.jetbrains.annotations.NonNls;

public class RuntimeThreadException extends RuntimeException {

    public RuntimeThreadException() {
    }

    public RuntimeThreadException(@NonNls String message) {
        super(message);
    }

    public RuntimeThreadException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeThreadException(Throwable cause) {
        super(cause);
    }

    public RuntimeThreadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
