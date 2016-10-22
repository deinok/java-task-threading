package com.github.deinok.threading;

import org.jetbrains.annotations.Nullable;


public interface OnSuccess<R> {
    void execute(@Nullable R result);
}
