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
public class Task<R> {

	//region Static

	/**
	 * Gets a task that has already completed successfully
	 *
	 * @return The successfully completed task
	 */
	@NotNull
	public static Task<Void> getCompletedTask() {
		Task<Void> task = new Task<Void>(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return null;
			}
		});
		task.runSynchronously();
		return task;
	}

	/**
	 * Creates a task that completes after a time delay
	 *
	 * @param millisecondsDelay The number of milliseconds to wait before completing the returned task, or -1 to wait indefinitely
	 * @return A task that represents the time delay
	 */
	@NotNull
	public static Task<Void> delay(final int millisecondsDelay) {
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
		Task<TResult> task = new Task<TResult>(new Callable<TResult>() {
			@Override
			public TResult call() throws Exception {
				return result;
			}
		});
		task.runSynchronously();
		return task;
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
		Task<TResult> task = new Task<TResult>(function);
		task.start();
		return task;
	}

	/**
	 * Waits for all of the provided Task objects to complete execution
	 *
	 * @param tasks An array of Task instances on which to wait
	 */
	public static void waitAll(@NotNull final Task... tasks) {
		for (final Task task : tasks) {
			task.await();
		}
	}

	/**
	 * Waits for any of the provided Task objects to complete execution
	 *
	 * @param tasks An array of Task instances on which to wait
	 * @return The index of the completed Task object in the tasks array
	 */
	public static int waitAny(@NotNull final Task... tasks) {
		while (true) {
			for (int i = 0; i < tasks.length; i++) {
				tasks[i].start();
				if (tasks[i].internalFutureTask.thread.getState() == Thread.State.TERMINATED) {
					return i;
				}
			}
		}
	}

	/**
	 * Creates a task that will complete when all of the Task objects in an array have completed
	 *
	 * @param tasks The tasks to wait on for completion.
	 * @return A task that represents the completion of all of the supplied tasks
	 */
	@NotNull
	public static Task<Void> whenAll(@NotNull final Task... tasks) {
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
	public static Task<Task> whenAny(@NotNull final Task... tasks) {
		return new Task<Task>(new Callable<Task>() {
			@Override
			public Task call() throws Exception {
				return tasks[Task.waitAny(tasks)];
			}
		});
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

	/**
	 * Gets an ID for this Task instance
	 *
	 * @return The identifier that is assigned by the system to this Task instance
	 */
	public int getId() {
		return Long.valueOf(this.internalFutureTask.thread.getId()).intValue();
	}

	/**
	 * Gets the TaskStatus of this task
	 *
	 * @return The current TaskStatus of this task instance
	 */
	@NotNull
	public TaskStatus getStatus() {
		switch (this.internalFutureTask.thread.getState()) {
			case BLOCKED:
				return TaskStatus.WaitingForChildrenToComplete;
			case NEW:
				return TaskStatus.Created;
			case RUNNABLE:
				return TaskStatus.Running;
			case TERMINATED:
				return TaskStatus.RanToCompletion;
			case TIMED_WAITING:
				return TaskStatus.WaitingForActivation;
			case WAITING:
				return TaskStatus.WaitingToRun;
			default:
				throw new RuntimeException("Imposible State");
		}
	}

	/**
	 * Gets whether this Task instance has completed execution due to being canceled
	 *
	 * @return true if the task has completed due to being canceled; otherwise false
	 */
	public boolean isCanceled() {
		throw new RuntimeException("Not Implemented");
	}

	/**
	 * Gets whether this Task has completed
	 *
	 * @return true if the task has completed; otherwise false
	 */
	public boolean isCompleted() {
		final TaskStatus taskStatus = this.getStatus();
		return taskStatus == TaskStatus.Canceled
			|| taskStatus == TaskStatus.Faulted
			|| taskStatus == TaskStatus.RanToCompletion;
	}

	/**
	 * Gets whether the Task completed due to an unhandled exception
	 *
	 * @return true if the task has thrown an unhandled exception; otherwise false
	 */
	public boolean isFaulted() {
		throw new RuntimeException("Not Implemented");
	}

	//endregion

	//region Executors

	/**
	 * Starts the Task, scheduling it for execution to the current TaskScheduler.
	 */
	public void start() {
		this.internalFutureTask.executeAsync();
	}

	/**
	 * Runs the Task synchronously on the current TaskScheduler
	 */
	public void runSynchronously() {
		this.internalFutureTask.executeSync();
		this.internalFutureTask.await();
	}
	//endregion

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
