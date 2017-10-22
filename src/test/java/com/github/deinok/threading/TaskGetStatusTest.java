package com.github.deinok.threading;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TaskGetStatusTest extends BaseTest {

	@Test
	public void createdFact() {
		Task<Void> task = new Task<Void>(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return null;
			}
		});
		Assert.assertEquals(TaskStatus.Created, task.getStatus());
	}

	@Test
	public void ranToCompletionFact() {
		Task<Void> task = new Task<Void>(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return null;
			}
		});
		task.await();
		Assert.assertEquals(TaskStatus.RanToCompletion, task.getStatus());
	}

}
