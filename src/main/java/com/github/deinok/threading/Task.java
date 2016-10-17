package com.github.deinok.threading;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;


public class Task<T> {

    //region Variables
    @NotNull
    private final Thread thread;

    @Nullable
    private T result;

    @NotNull
    private TaskState taskState = TaskState.NotStarted;

    //endregion

    //region Constructors

    public Task(@NotNull final TaskRunnable<T> function) {
        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                taskState = TaskState.Started;
                result = function.execute();
                taskState = TaskState.Finished;
            }
        });
    }

    //endregion

    //region Executors
    @NotNull
    public Task<T> executeAsync() {
        if (this.taskState == TaskState.NotStarted) {
            this.thread.start();
        }
        return this;
    }

    @NotNull
    public Task<T> executeSync() {
        if (this.taskState == TaskState.NotStarted) {
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
    public Task<T> await() {
        switch (this.taskState) {
            case NotStarted:
                return this.executeAsync().await();
            case Started:
                try {
                    this.thread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return this.await();
            case Finished:
                return this;
        }
        throw new IllegalStateException();
    }

    @NotNull
    public T getResult() {
        return this.await().result;
    }

    @NotNull
    public TaskState getTaskState() {
        return this.taskState;
    }

}
