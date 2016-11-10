package com.github.deinok.threading;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;


public class TaskRuntimeThreadExceptionTest {

    @Test
    public void taskRuntimeThreadExceptionTest1() {
        final String testExceptionString = "TestingException";

        final Task<Void> task = new Task<Void>(new Callable<Void>() {
            public Void call() throws Exception {
                throw new RuntimeException(testExceptionString);
            }
        });

        task.executeAsync();

        try {
            task.getResult();
            Assert.fail();
        } catch (RuntimeThreadException e) {
            Assert.assertTrue(e.getCause() instanceof RuntimeException);
            Assert.assertEquals(e.getCause().getMessage(), testExceptionString);
        }
    }

}
