package com.github.deinok.threading;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class RuntimeThreadException extends RuntimeException {

    public RuntimeThreadException() {
    }

    public RuntimeThreadException(@NotNull String message) {
        super(message);
    }

    public RuntimeThreadException(@NotNull String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public RuntimeThreadException(@Nullable Throwable cause) {
        super(cause);
    }

}
