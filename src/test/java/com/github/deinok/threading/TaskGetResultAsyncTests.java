package com.github.deinok.threading;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

/**
 * Created by raulh on 04/11/2016.
 */
public class TaskGetResultAsyncTests {

    @Test(timeout = 500)
    public void executeAsync1() throws InterruptedException {
        final Task<Void> task = new Task<Void>(new Callable<Void>() {
            public Void call() throws InterruptedException {
                Thread.sleep(250);
                return null;
            }
        }).executeAsync();

        Thread.sleep(250);

        task.getResult();
    }

    @Test
    public void executeAsync2() {
        final String currentThreadName = Thread.currentThread().getName();

        final Task<String> task = new Task<String>(new Callable<String>() {
            public String call() throws Exception {
                return Thread.currentThread().getName();
            }
        });

        Assert.assertNotEquals(currentThreadName, task.getResult());
    }

    @Test
    public void executeAsync3() {
        final Long currentThreadId = Thread.currentThread().getId();

        final Task<Long> task = new Task<Long>(new Callable<Long>() {
            public Long call() throws Exception {
                return Thread.currentThread().getId();
            }
        });

        Assert.assertNotEquals(currentThreadId, task.getResult());
    }


}
