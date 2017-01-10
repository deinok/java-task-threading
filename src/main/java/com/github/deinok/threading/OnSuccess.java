package com.github.deinok.threading;

import org.jetbrains.annotations.Nullable;

/**
 * A Functional Interface to detect when a Promise is Finished Successful
 *
 * @param <R> The Type of the Promise
 */
public interface OnSuccess<R> {

	/**
	 * Executed when the Promise have the result
	 * @param result The result of the Promise
	 */
	void execute(@Nullable R result);

}
