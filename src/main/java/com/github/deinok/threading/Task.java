package com.github.deinok.threading;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @param <T>
 */
public class Task<T> {

    //region Variables
    @Nullable
    private OnSuccess<T> onSuccess;

    @NotNull
    private final Thread thread;

    @Nullable
    private T result;
    //endregion

    //region Constructors

    public Task(@NotNull final TaskRunnable<T> function) {
        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                result = function.execute();
            }
        });
    }

    //endregion

    //region Executors
    @NotNull
    public Task<T> executeAsync() {
        if (this.thread.getState() == Thread.State.NEW) {
            this.thread.start();
        }
        return this;
    }

    @NotNull
    public Task<T> executeSync() {
        if (this.thread.getState() == Thread.State.NEW) {
            this.thread.run();
        }
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
            case TERMINATED:
                return this;
        }
        throw new IllegalThreadStateException();
    }

    @Nullable
    public T getResult() {
        this.await();
        return this.await().result;
    }

    public void onSuccess(@NotNull OnSuccess<T> onSuccess){
        this.onSuccess=onSuccess;
    }

}
