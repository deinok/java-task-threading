package com.github.deinok.threading;

/**
 * Interface of a Deferred
 *
 * @param <R> The return type
 */
interface IDeferred<R> {

	/**
	 * Gets the result of a deferred instance
	 *
	 * @return The Result
	 */
	R getResult();
}
