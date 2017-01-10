package com.github.deinok.threading;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;


public class TaskPriorityTest {

	@Test
	public void taskPriorityTest1() {
		int priority = 1;
		final Task<Void> task = new Task<Void>(new Callable<Void>() {
			public Void call() throws Exception {
				return null;
			}
		});

		task.setPriority(priority);
		Assert.assertEquals(task.getPriority(), priority);

		task.executeAsync();
		Assert.assertEquals(task.getPriority(), priority);

		task.await();
		Assert.assertEquals(task.getPriority(), priority);
	}

	@Test
	public void taskPriorityTest2() {
		final Task<Void> task = new Task<Void>(new Callable<Void>() {
			public Void call() throws Exception {
				return null;
			}
		});

		task.setPriority(1);
		Assert.assertEquals(task.getPriority(), 1);

		task.setPriority(2);
		task.executeAsync();
		Assert.assertEquals(task.getPriority(), 2);

		task.setPriority(3);
		task.await();
		Assert.assertEquals(task.getPriority(), 2);
	}
}
