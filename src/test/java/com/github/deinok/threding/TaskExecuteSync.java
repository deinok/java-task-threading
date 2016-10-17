package com.github.deinok.threding;


import com.github.deinok.threading.Task;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TaskExecuteSync {

    @Test
    public void executeSync1() throws InterruptedException {
        long start = System.currentTimeMillis();
        Task<Void> task = new Task<Void>(new Callable<Void>() {
            @Override
            public Void call() throws InterruptedException {
                Thread.sleep(250);
                return null;
            }
        }).executeSync();

        Thread.sleep(250);

        task.await();
        long end = System.currentTimeMillis();
        Assert.assertTrue(String.valueOf(true), (end - start) > 500);
    }
}
