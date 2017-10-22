package com.github.deinok.threading;

import org.jetbrains.annotations.NotNull;

/**
 * A Task in parallel
 *
 * @param <R> The Result Type
 */
interface ITask<R> extends IDeferred<R>, IPromise<R> {

	//region Priority

	/**
	 * Gets the priority of the Task
	 *
	 * @return The Priority
	 */
	int getPriority();

	/**
	 * Sets the priority
	 *
	 * @param priority The new Priority
	 * @return The ITask
	 */
	@NotNull
	ITask<R> setPriority(int priority);

	//endregion

	//region Executors

	/**
	 * Executes the Task in the selected mode
	 *
	 * @param executionMode The Execution Mode
	 * @return The ITask
	 */
	ITask<R> execute(@NotNull ExecutionMode executionMode);

	/**
	 * Executes the Task Asynchronous
	 *
	 * @return The ITask
	 */
	@NotNull
	ITask<R> executeAsync();

	/**
	 * Executes the Task Synchronous
	 *
	 * @return The Task
	 */
	@NotNull
	ITask<R> executeSync();

	/**
	 * Cancel the Task
	 *
	 * @return Returns if it is canceled
	 */
	boolean cancel();

	/**
	 * Ensures that the task is finished
	 *
	 * @return The Awaited Task(Finished)
	 */
	@NotNull
	ITask<R> await();

	//endregion
}
