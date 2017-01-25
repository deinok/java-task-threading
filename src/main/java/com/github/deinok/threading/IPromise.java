package com.github.deinok.threading;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface of a IPromise
 *
 * @param <R> The Return Type
 */
public interface IPromise<R> {

	/**
	 * Sets the action to execute when the IPromise ends successful
	 *
	 * @param onSuccess The Functional Interface
	 * @return The same IPromise
	 */
	@NotNull
	IPromise<R> onSuccess(@Nullable OnSuccess<R> onSuccess);

	/**
	 * Sets the action to execute when the IPromise ends with an exception
	 *
	 * @param onException The Functional Interface
	 * @return The same IPromise
	 */
	@NotNull
	IPromise<R> onException(@Nullable OnException onException);

}
