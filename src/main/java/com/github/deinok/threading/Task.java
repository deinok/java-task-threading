package com.github.deinok.threading;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @param <R>
 */
public class Task<R> {

    //region Variables
    @NotNull
    private final Promise<R> promise;
    //endregion

    //region Constructors

    public Task(@NotNull final Callable<R> callable) {
        this.promise = new Promise<R>(callable);
    }

    //endregion

    public int getPriority() {
        return this.promise.getPriority();
    }

    @NotNull
    public Task<R> setPriority(int newPriority) {
        this.promise.setPriority(newPriority);
        return this;
    }

    //region Executors
    @NotNull
    public Task<R> executeAsync() {
        this.promise.executeAsync();
        return this;
    }

    @NotNull
    public Task<R> executeSync() {
        this.promise.executeSync();
        return this;
    }
    //endregion

    public boolean cancel() {
        return this.promise.cancel(true);
    }

    /**
     * Ensures that the result is ready to be returned
     *
     * @return The Awaited Task(Finished)
     */
    @NotNull
    public Task<R> await() {
        this.promise.await();
        return this;
    }

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

    @NotNull
    public Task<R> onSuccess(@NotNull OnSuccess<R> onSuccess) {
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

        public int getPriority() {
            return this.thread.getPriority();
        }

        @NotNull
        public Promise<R> setPriority(int newPriority) {
            if (this.thread.getState() == Thread.State.NEW) {
                this.thread.setPriority(newPriority);
            }
            return this;
        }

        @NotNull
        public Promise<R> await() {
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

        private void join() {
            try {
                this.thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
