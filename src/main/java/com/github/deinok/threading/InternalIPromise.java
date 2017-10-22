package com.github.deinok.threading;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface of a InternalIPromise
 *
 * @param <R> The Return Type
 */
interface InternalIPromise<R> {

	/**
	 * Sets the action to execute when the InternalIPromise ends successful
	 *
	 * @param onSuccess The Functional Interface
	 * @return The same InternalIPromise
	 */
	@NotNull
	InternalIPromise<R> onSuccess(@Nullable OnSuccess<R> onSuccess);

	/**
	 * Sets the action to execute when the InternalIPromise ends with an exception
	 *
	 * @param onException The Functional Interface
	 * @return The same InternalIPromise
	 */
	@NotNull
	InternalIPromise<R> onException(@Nullable OnException onException);

}
