package com.github.deinok.threading;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @param <T>
 */
public class Task<T> {

    //region Variables
    @NotNull
    private final Promise<T> promise;
    //endregion

    //region Constructors

    public Task(@NotNull final Callable<T> callable) {
        this.promise = new Promise<T>(callable);
    }

    //endregion

    //region Executors
    @NotNull
    public Task<T> executeAsync() {
        this.promise.executeAsync();
        return this;
    }

    @NotNull
    public Task<T> executeSync() {
        this.promise.executeSync();
        return this;
    }
    //endregion

    /**
     * Ensures that the result is ready to be returned
     *
     * @return The Awaited Task(Finished)
     */
    @NotNull
    public Task<T> await() {
        this.promise.await();
        return this;
    }

    @Nullable
    public T getResult() throws RuntimeThreadException {
        this.await();
        try {
            return this.promise.get();
        } catch (ExecutionException e) {
            throw new RuntimeThreadException(e.getCause());
        } catch (InterruptedException e) {
            throw new RuntimeThreadException(e);
        }
    }

    @NotNull
    public Task<T> onSuccess(@NotNull OnSuccess<T> onSuccess){
        this.promise.setOnSuccess(onSuccess);
        return this;
    }


    private class Promise<R> extends FutureTask<R> {

        @NotNull
        private final Thread thread;

        @Nullable
        private OnSuccess<R> onSuccess;

        public Promise(@NotNull Callable<R> callable) {
            super(callable);
            this.thread = new Thread(this);
        }

        @NotNull
        public Promise<R> setOnSuccess(@NotNull OnSuccess<R> onSuccess) {
            this.onSuccess = onSuccess;
            return this;
        }

        @NotNull
        public Promise<R> executeAsync() {
            if (this.thread.getState() == Thread.State.NEW) {
                this.thread.start();
            }
            return this;
        }

        @NotNull
        public Promise<R> executeSync() {
            if (this.thread.getState() == Thread.State.NEW) {
                this.thread.run();
            }
            return this;
        }

        @NotNull
        public Promise<R> await() {
            switch (this.thread.getState()) {
                case NEW:
                    return this.executeAsync().await();

                case RUNNABLE:
                    try {
                        this.thread.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return this.await();

                case BLOCKED:
                    try {
                        this.thread.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return this.await();

                case WAITING:
                    try {
                        this.thread.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
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

    }



}
