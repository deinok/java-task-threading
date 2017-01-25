package com.github.deinok.threading;

import org.jetbrains.annotations.NotNull;

/**
 * Functional Interface to detect exceptions
 */
public interface OnException {

	/**
	 * Executed when a IPromise throws an exception
	 *
	 * @param exception A RuntimeThreadException
	 */
	void execute(@NotNull RuntimeThreadException exception);

}
