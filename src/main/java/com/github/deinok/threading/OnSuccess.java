package com.github.deinok.threading;

import org.jetbrains.annotations.Nullable;

/**
 * A Functional Interface to detect when a IPromise is Finished Successful
 *
 * @param <R> The Type of the IPromise
 */
public interface OnSuccess<R> {

	/**
	 * Executed when the IPromise have the result
	 * @param result The result of the IPromise
	 */
	void execute(@Nullable R result);

}
