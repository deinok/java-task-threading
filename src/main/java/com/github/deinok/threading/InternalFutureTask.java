package com.github.deinok.threading;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

class InternalFutureTask<P> extends FutureTask<P> implements IPromise<P> {

	@NotNull
	public final Thread thread;

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

