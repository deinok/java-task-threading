package com.github.deinok.threading;

import org.jetbrains.annotations.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: rhc1
 * Date: 17/10/16
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public interface OnSuccess<R> {
    void execute(@Nullable R result);
}
