package com.github.deinok.threading;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;


public class TaskExecuteSyncTests {

    @Test
    public void executeSync1() throws InterruptedException {
        final long start = System.currentTimeMillis();
        final Task<Void> task = new Task<Void>(new Callable<Void>() {
            public Void call() throws InterruptedException {
                Thread.sleep(250);
                return null;
            }
        }).executeSync();

        Thread.sleep(250);

        task.await();
        final long end = System.currentTimeMillis();
        Assert.assertTrue(String.valueOf(true), (end - start) >= 500);
    }

    @Test
    public void executeSync2() {
        final String currentThreadName = Thread.currentThread().getName();

        final Task<String> task = new Task<String>(new Callable<String>() {
            public String call() throws Exception {
                return Thread.currentThread().getName();
            }
        }).executeSync();

        Assert.assertEquals(currentThreadName, task.getResult());
    }

    @Test
    public void executeSync3() {
        final Long currentThreadId = Thread.currentThread().getId();

        final Task<Long> task = new Task<Long>(new Callable<Long>() {
            public Long call() throws Exception {
                return Thread.currentThread().getId();
            }
        }).executeSync();

        Assert.assertEquals(currentThreadId, task.getResult());
    }
}
