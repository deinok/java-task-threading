package com.github.deinok.threading;


import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TaskOnSuccessTests {

	@Test
	public void onSuccessTest1() {

		final long start = System.currentTimeMillis();

		final Task<Integer> task = new Task<Integer>(new Callable<Integer>() {
			public Integer call() throws InterruptedException {
				Thread.sleep(250);
				return 1;
			}
		}).onSuccess(new OnSuccess<Integer>() {
			public void execute(@Nullable Integer result) {
				Assert.assertEquals(1, result.intValue());
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}).executeAsync();

		Assert.assertEquals(1, task.getResult().intValue());

		final long middle = System.currentTimeMillis();
		Assert.assertTrue(String.valueOf(true), ((middle - start) < 500));


		task.await();
		final long end = System.currentTimeMillis();
		Assert.assertTrue(String.valueOf(true), ((end - start) >= 500));

	}
}
