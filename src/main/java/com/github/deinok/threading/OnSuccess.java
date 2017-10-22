package com.github.deinok.threading;

import org.jetbrains.annotations.Nullable;

/**
 * A Functional Interface to detect when a InternalIPromise is Finished Successful
 *
 * @param <R> The Type of the InternalIPromise
 */
public interface OnSuccess<R> {

	/**
	 * Executed when the InternalIPromise have the result
	 * @param result The result of the InternalIPromise
	 */
	void execute(@Nullable R result);

}
