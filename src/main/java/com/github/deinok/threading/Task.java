package com.github.deinok.threading;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Represents an asynchronous operation that can return a value
 *
 * @param <R> The type of the result produced by this Task<R>
 */
public class Task<R> implements ITask<R> {

	//region Static

	/**
	 * Gets a task that has already completed successfully
	 *
	 * @return The successfully completed task
	 */
	@NotNull
	public static Task getCompletedTask() {
		return new Task<Void>(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return null;
			}
		}).executeSync();
	}

	/**
	 * Creates a task that completes after a time delay
	 *
	 * @param millisecondsDelay The number of milliseconds to wait before completing the returned task, or -1 to wait indefinitely
	 * @return A task that represents the time delay
	 */
	@NotNull
	public static Task delay(final int millisecondsDelay) {
		return new Task<Void>(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				if (millisecondsDelay == -1) {
					Thread.sleep(Integer.MAX_VALUE);
				} else {
					Thread.sleep(millisecondsDelay);
				}
				return null;
			}
		});
	}

	/**
	 * Creates a Task<TResult> that's completed successfully with the specified result
	 *
	 * @param result    The result to store into the completed task
	 * @param <TResult> The type of the result returned by the task
	 * @return The successfully completed task
	 */
	@NotNull
	public static <TResult> Task<TResult> fromResult(final TResult result) {
		return new Task<TResult>(new Callable<TResult>() {
			@Override
			public TResult call() throws Exception {
				return result;
			}
		}).executeSync();
	}

	/**
	 * Queues the specified work to run on the thread pool and returns a Task<TResult> object that represents that work
	 *
	 * @param function  The work to execute asynchronously
	 * @param <TResult> The return type of the task
	 * @return A task object that represents the work queued to execute in the thread pool
	 */
	@NotNull
	public static <TResult> Task<TResult> run(@NotNull final Callable<TResult> function) {
		return new Task<TResult>(function).executeAsync();
	}

	/**
	 * Waits for all of the provided Task objects to complete execution
	 *
	 * @param tasks An array of Task instances on which to wait
	 */
	public static void waitAll(@NotNull final Task<Object>... tasks) {
		for (final Task<Object> task : tasks) {
			task.await();
		}
	}

	/**
	 * Waits for any of the provided Task objects to complete execution
	 *
	 * @param tasks An array of Task instances on which to wait
	 * @return The index of the completed Task object in the tasks array
	 */
	public static int waitAny(@NotNull final Task<Object>[] tasks) {
		throw new RuntimeException("Not Implemented");
	}

	/**
	 * Creates a task that will complete when all of the Task objects in an array have completed
	 *
	 * @param tasks The tasks to wait on for completion.
	 * @return A task that represents the completion of all of the supplied tasks
	 */
	@NotNull
	public static Task whenAll(@NotNull final Task<Object>[] tasks) {
		return new Task<Void>(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				Task.waitAll(tasks);
				return null;
			}
		});
	}

	/**
	 * Creates a task that will complete when any of the supplied tasks have completed
	 *
	 * @param tasks The tasks to wait on for completion
	 * @return A task that represents the completion of one of the supplied tasks. The return task's Result is the task that completed
	 */
	@NotNull
	public static Task whenAny(@NotNull final Task<Object>[] tasks) {
		throw new RuntimeException("Not Implemented");
	}

	//endregion

	//region Variables
	@NotNull
	private final InternalFutureTask<R> internalFutureTask;
	//endregion

	//region Constructors

	/**
	 * Creates a new Task
	 *
	 * @param callable The callable function
	 */
	public Task(@NotNull final Callable<R> callable) {
		this.internalFutureTask = new InternalFutureTask<R>(callable);
	}

	//endregion

	//region Properties

	//endregion

	//region Priority

	/**
	 * Gets the priority of the Task
	 *
	 * @return The Priority
	 */
	public int getPriority() {
		return this.internalFutureTask.getPriority();
	}

	/**
	 * Sets the priority
	 *
	 * @param priority The new Priority
	 * @return The Task
	 */
	@NotNull
	public Task<R> setPriority(int priority) {
		this.internalFutureTask.setPriority(priority);
		return this;
	}

	//endregion

	//region Executors

	/**
	 * Executes the Task in the selected mode
	 *
	 * @param executionMode The Execution Mode
	 * @return The Task
	 */
	public Task<R> execute(@NotNull ExecutionMode executionMode) {
		switch (executionMode) {
			case SYNC:
				return this.executeSync();
			case ASYNC:
				return this.executeAsync();
		}
		throw new IllegalStateException();
	}

	/**
	 * Executes the Task Asynchronous
	 *
	 * @return The Task
	 */
	@NotNull
	public Task<R> executeAsync() {
		this.internalFutureTask.executeAsync();
		return this;
	}

	/**
	 * Executes the Task Synchronous
	 *
	 * @return The Task
	 */
	@NotNull
	public Task<R> executeSync() {
		this.internalFutureTask.executeSync();
		return this;
	}
	//endregion

	/**
	 * Cancel the Task
	 *
	 * @return Returns if it is canceled
	 */
	public boolean cancel() {
		return this.internalFutureTask.cancel(true);
	}

	/**
	 * Ensures that the result is ready to be returned
	 *
	 * @return The Awaited Task(Finished)
	 */
	@NotNull
	public Task<R> await() throws RuntimeThreadException {
		this.internalFutureTask.await();
		return this;
	}

	/**
	 * Gets the result of the Task
	 *
	 * @return The Result
	 * @throws RuntimeThreadException The probable Exception throw by the Thread
	 */
	@Nullable
	public R getResult() throws RuntimeThreadException {
		try {
			return this.internalFutureTask.executeAsync().get();
		} catch (ExecutionException e) {
			throw new RuntimeThreadException(e.getCause());
		} catch (InterruptedException e) {
			throw new RuntimeThreadException(e);
		}
	}

	/**
	 * Callback executed when the Task result is ready
	 *
	 * @param onSuccess The Callback Interface
	 * @return The Task
	 */
	@NotNull
	public Task<R> onSuccess(@Nullable OnSuccess<R> onSuccess) {
		this.internalFutureTask.onSuccess(onSuccess);
		return this;
	}

	/**
	 * Callback executed when the Task ends with an exception
	 *
	 * @param onException The Callback Interface
	 * @return The Task
	 */
	@NotNull
	public Task<R> onException(@Nullable OnException onException) {
		this.internalFutureTask.onException(onException);
		return this;
	}

	private class InternalFutureTask<P> extends FutureTask<P> implements IPromise<P> {

		@NotNull
		private final Thread thread;

		@Nullable
		private OnSuccess<P> onSuccess;

		@Nullable
		private OnException onException;

		public InternalFutureTask(@NotNull Callable<P> callable) {
			super(callable);
			this.thread = new Thread(this);
		}

		@NotNull
		public InternalFutureTask<P> executeAsync() {
			if (this.thread.getState() == Thread.State.NEW) {
				this.thread.start();
			}
			return this;
		}

		@NotNull
		public InternalFutureTask<P> executeSync() {
			if (this.thread.getState() == Thread.State.NEW) {
				this.thread.run();
			}
			return this;
		}

		@NotNull
		public IPromise<P> onSuccess(@Nullable OnSuccess<P> onSuccess) {
			this.onSuccess = onSuccess;
			return this;
		}

		@NotNull
		public IPromise<P> onException(@Nullable OnException onException) {
			this.onException = onException;
			return this;
		}

		public int getPriority() {
			return this.thread.getPriority();
		}

		@NotNull
		public InternalFutureTask<P> setPriority(int newPriority) {
			if (this.thread.getState() == Thread.State.NEW) {
				this.thread.setPriority(newPriority);
			}
			return this;
		}

		@NotNull
		public InternalFutureTask<P> await() throws RuntimeThreadException {
			switch (this.thread.getState()) {
				case NEW:
					return this.executeAsync().await();

				case RUNNABLE:
					this.join();
					return this.await();

				case BLOCKED:
					this.join();
					return this.await();

				case WAITING:
					this.join();
					return this.await();

				case TIMED_WAITING:
					this.join();
					return this.await();

				case TERMINATED:
					return this;
			}

			throw new IllegalThreadStateException();
		}

		protected void done() throws RuntimeThreadException {
			super.done();

			P result = null;

			try {
				result = this.get();
			} catch (InterruptedException e) {
				this.executeOnException(new RuntimeThreadException(e.getCause()));
			} catch (ExecutionException e) {
				this.executeOnException(new RuntimeThreadException(e.getCause()));
			}

			try {
				if (this.onSuccess != null) {
					this.onSuccess.execute(result);
				}
			} catch (Exception e) {
				this.executeOnException(new RuntimeThreadException(e));
			}

		}

		private void join() throws RuntimeThreadException {
			try {
				this.thread.join();
			} catch (InterruptedException e) {
				throw new RuntimeThreadException(e);
			}
		}

		private void executeOnException(@Nullable RuntimeThreadException runtimeThreadException) {
			if (runtimeThreadException != null) {
				if (this.onException != null) this.onException.execute(runtimeThreadException);
				else throw runtimeThreadException;
			}
		}

	}

}
