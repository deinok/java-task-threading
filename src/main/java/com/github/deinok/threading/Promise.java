package com.github.deinok.threading;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface of a Promise
 *
 * @param <T> The Return Type
 */
public interface Promise<T> {

	/**
	 * Sets the action to execute when the Promise ends successful
	 *
	 * @param onSuccess The Functional Interface
	 * @return The same Promise
	 */
	@NotNull
	Promise<T> onSuccess(@Nullable OnSuccess<T> onSuccess);

	/**
	 * Sets the action to execute when the Promise ends with an exception
	 *
	 * @param onException The Functional Interface
	 * @return The same Promise
	 */
	@NotNull
	Promise<T> onException(@Nullable OnException onException);

}
