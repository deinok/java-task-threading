package com.github.deinok.threading;


import org.jetbrains.annotations.NotNull;

public class RuntimeThreadException extends RuntimeException {

    public RuntimeThreadException() {
    }

    public RuntimeThreadException(@NotNull String message) {
        super(message);
    }

    public RuntimeThreadException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeThreadException(Throwable cause) {
        super(cause);
    }

}
