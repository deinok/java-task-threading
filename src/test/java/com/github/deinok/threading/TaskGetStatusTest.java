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

	@Test
	public void runningFact() throws InterruptedException {
		Task<Void> task = new Task<Void>(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				StringBuilder stringBuilder = new StringBuilder(1000);
				for (int i = 0; i < stringBuilder.capacity(); i++) {
					stringBuilder.append(i);
				}
				return null;
			}
		});
		task.start();
		Assert.assertEquals(TaskStatus.Running, task.getStatus());
	}

}
