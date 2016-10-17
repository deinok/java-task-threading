package com.github.deinok.threding;

import com.github.deinok.threading.Task;
import com.github.deinok.threading.TaskRunnable;
import org.junit.Assert;
import org.junit.Test;

public class TaskTests {

    @Test(timeout = 500)
    public void sleepTest(){
        Task<Void> task = new Task<Void>(new TaskRunnable<Void>() {
            @Override
            public Void execute() {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return null;
                }
        }).executeAsync();

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        task.await();
    }

    @Test
    public void resultIntegerTest(){
        Task<Integer> task = new Task<Integer>(new TaskRunnable<Integer>() {
            @Override
            public Integer execute() {
                return 1;
            }
        }).executeAsync();
        Assert.assertEquals(1, (int)task.getResult());
    }
}
