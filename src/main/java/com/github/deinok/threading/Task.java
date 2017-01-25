package com.github.deinok.threading;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * A Task in parallel
 *
 * @param <R> The Result Type
 */
public class Task<R> implements IPromise<R>, IDeferred<R> {

	//region Variables
	@NotNull
	private final InnerFutureTask<R> innerFutureTask;
	//endregion

	//region Constructors

	/**
	 * Creates a new Task
	 *
	 * @param callable The callable function
	 */
	public Task(@NotNull final Callable<R> callable) {
		this.innerFutureTask = new InnerFutureTask<R>(callable);
	}

	//endregion

	/**
	 * Gets the priority of the Task
	 *
	 * @return The Priority
	 */
	public int getPriority() {
		return this.innerFutureTask.getPriority();
	}

	/**
	 * Sets the priority
	 *
	 * @param priority The new Priority
	 * @return The Task
	 */
	@NotNull
	public Task<R> setPriority(int priority) {
		this.innerFutureTask.setPriority(priority);
		return this;
	}

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
		this.innerFutureTask.executeAsync();
		return this;
	}

	/**
	 * Executes the Task Synchronous
	 *
	 * @return The Task
	 */
	@NotNull
	public Task<R> executeSync() {
		this.innerFutureTask.executeSync();
		return this;
	}
	//endregion

	/**
	 * Cancel the Task
	 *
	 * @return Returns if it is canceled
	 */
	public boolean cancel() {
		return this.innerFutureTask.cancel(true);
	}

	/**
	 * Ensures that the result is ready to be returned
	 *
	 * @return The Awaited Task(Finished)
	 */
	@NotNull
	public Task<R> await() throws RuntimeThreadException {
		this.innerFutureTask.await();
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
			return this.innerFutureTask.executeAsync().get();
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
		this.innerFutureTask.onSuccess(onSuccess);
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
		this.innerFutureTask.onException(onException);
		return this;
	}

	private class InnerFutureTask<P> extends FutureTask<P> implements IPromise<P> {

		@NotNull
		private final Thread thread;

		@Nullable
		private OnSuccess<P> onSuccess;

		@Nullable
		private OnException onException;

		public InnerFutureTask(@NotNull Callable<P> callable) {
			super(callable);
			this.thread = new Thread(this);
		}

		@NotNull
		public InnerFutureTask<P> executeAsync() {
			if (this.thread.getState() == Thread.State.NEW) {
				this.thread.start();
			}
			return this;
		}

		@NotNull
		public InnerFutureTask<P> executeSync() {
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
		public InnerFutureTask<P> setPriority(int newPriority) {
			if (this.thread.getState() == Thread.State.NEW) {
				this.thread.setPriority(newPriority);
			}
			return this;
		}

		@NotNull
		public InnerFutureTask<P> await() throws RuntimeThreadException {
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
