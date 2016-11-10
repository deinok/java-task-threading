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
public class Task<R> {

    //region Variables
    @NotNull
    private final Promise<R> promise;
    //endregion

    //region Constructors

    /**
     * Creates a new Task
     *
     * @param callable The callable function
     */
    public Task(@NotNull final Callable<R> callable) {
        this.promise = new Promise<R>(callable);
    }

    //endregion

    /**
     * Gets the priority of the Task
     *
     * @return The Priority
     */
    public int getPriority() {
        return this.promise.getPriority();
    }

    /**
     * Sets the priority
     *
     * @param priority The new Priority
     * @return The Task
     */
    @NotNull
    public Task<R> setPriority(int priority) {
        this.promise.setPriority(priority);
        return this;
    }

    //region Executors

    /**
     * Executes the Task Asynchronous
     *
     * @return The Task
     */
    @NotNull
    public Task<R> executeAsync() {
        this.promise.executeAsync();
        return this;
    }

    /**
     * Executes the Task Synchronous
     *
     * @return The Task
     */
    @NotNull
    public Task<R> executeSync() {
        this.promise.executeSync();
        return this;
    }
    //endregion

    /**
     * Cancel the Task
     *
     * @return Returns if it is canceled
     */
    public boolean cancel() {
        return this.promise.cancel(true);
    }

    /**
     * Ensures that the result is ready to be returned
     *
     * @return The Awaited Task(Finished)
     */
    @NotNull
    public Task<R> await() throws RuntimeThreadException {
        this.promise.await();
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
            return this.promise.executeAsync().get();
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
    public Task<R> onSuccess(@NotNull OnSuccess<R> onSuccess) {
        this.promise.setOnSuccess(onSuccess);
        return this;
    }

    private class Promise<P> extends FutureTask<P> {

        @NotNull
        private final Thread thread;

        @Nullable
        private OnSuccess<P> onSuccess;

        public Promise(@NotNull Callable<P> callable) {
            super(callable);
            this.thread = new Thread(this);
        }

        @NotNull
        public Promise<P> setOnSuccess(@NotNull OnSuccess<P> onSuccess) {
            this.onSuccess = onSuccess;
            return this;
        }

        @NotNull
        public Promise<P> executeAsync() {
            if (this.thread.getState() == Thread.State.NEW) {
                this.thread.start();
            }
            return this;
        }

        @NotNull
        public Promise<P> executeSync() {
            if (this.thread.getState() == Thread.State.NEW) {
                this.thread.run();
            }
            return this;
        }

        public int getPriority() {
            return this.thread.getPriority();
        }

        @NotNull
        public Promise<P> setPriority(int newPriority) {
            if (this.thread.getState() == Thread.State.NEW) {
                this.thread.setPriority(newPriority);
            }
            return this;
        }

        @NotNull
        public Promise<P> await() throws RuntimeThreadException{
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
            if (this.onSuccess != null) {
                try {
                    this.onSuccess.execute(get());
                } catch (ExecutionException e) {
                    throw new RuntimeThreadException(e.getCause());
                } catch (InterruptedException e) {
                    throw new RuntimeThreadException(e);
                }
            }
        }

        private void join() throws RuntimeThreadException {
            try {
                this.thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeThreadException(e);
            }
        }

    }

}
