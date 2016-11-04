package com.github.deinok.threading.group;


import com.github.deinok.threading.Task;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TaskListExecuteAsyncTest {

    @Test(timeout = 500)
    public void executeAsync1() throws InterruptedException {
        final TaskList<Void> taskList = new TaskList<Void>();
        for (int i = 0; i < 5; i++) {
            taskList.add(new Task<Void>(new Callable<Void>() {
                public Void call() throws InterruptedException {
                    Thread.sleep(250);
                    return null;
                }
            }));
        }

        taskList.executeAsync();

        Thread.sleep(250);

        taskList.await();
    }

    @Test
    public void executeAsync2() {
        final String currentThreadName = Thread.currentThread().getName();

        final TaskList<String> taskList = new TaskList<String>();
        for (int i = 0; i < 5; i++) {
            taskList.add(new Task<String>(new Callable<String>() {
                public String call() throws InterruptedException {
                    return Thread.currentThread().getName();
                }
            }));
        }

        taskList.executeAsync();

        for (String result : taskList.getResult()) {
            Assert.assertNotEquals(currentThreadName, result);
        }
    }

    @Test
    public void executeAsync3() {
        final Long currentThreadId = Thread.currentThread().getId();

        final TaskList<Long> taskList = new TaskList<Long>();
        for (int i = 0; i < 5; i++) {
            taskList.add(new Task<Long>(new Callable<Long>() {
                public Long call() throws InterruptedException {
                    return Thread.currentThread().getId();
                }
            }));
        }

        for (Long result : taskList.getResult()) {
            Assert.assertNotEquals(currentThreadId, result);
        }
    }
}
