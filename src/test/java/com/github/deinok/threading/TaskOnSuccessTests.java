package com.github.deinok.threading;


import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TaskOnSuccessTests {

    @Test
    public void onSuccessTest1() {
        final long start = System.currentTimeMillis();

        final Task<Long> task = new Task<Long>(new Callable<Long>() {
            public Long call() throws Exception {
                Thread.sleep(250);
                return Thread.currentThread().getId();
            }
        }).executeAsync().onSuccess(new OnSuccess<Long>() {
            public void execute(@NotNull Long result) {
                try {
                    Thread.sleep(250);
                    final long mainThreadId = Thread.currentThread().getId();
                    Assert.assertEquals(mainThreadId, (long) result);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        task.await();

        final long end = System.currentTimeMillis();

        Assert.assertTrue(String.valueOf(true), ((end - start) > 500));
    }
}
