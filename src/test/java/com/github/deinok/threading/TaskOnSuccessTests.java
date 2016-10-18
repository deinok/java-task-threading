package com.github.deinok.threading;


import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TaskOnSuccessTests {

    @Test
    public void onSuccessTest1() {

        final Long[] threadIds = new Long[3];

        final long start = System.currentTimeMillis();

        final Task<Long> task = new Task<Long>(new Callable<Long>() {
            public Long call() throws Exception {
                threadIds[0] = Thread.currentThread().getId();
                Thread.sleep(250);
                return threadIds[0];
            }
        }).executeAsync().onSuccess(new OnSuccess<Long>() {
            public void execute(@Nullable Long result) {
                threadIds[1] = Thread.currentThread().getId();
                try {
                    Thread.sleep(250);
                    Assert.assertNotEquals(Thread.currentThread().getId(), (long) result);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        threadIds[2] = Thread.currentThread().getId();

        task.await();

        final long end = System.currentTimeMillis();
        Assert.assertTrue(String.valueOf(true), ((end - start) > 500));

        Assert.assertNotEquals(threadIds[0], threadIds[1]);
        Assert.assertNotEquals(threadIds[0], threadIds[2]);
        Assert.assertEquals(threadIds[1], threadIds[2]);

    }
}
