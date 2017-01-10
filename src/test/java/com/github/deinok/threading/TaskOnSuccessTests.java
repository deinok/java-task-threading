package com.github.deinok.threading;


import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TaskOnSuccessTests {

	@Test
	public void onSuccessTest1() {

		final long start = System.currentTimeMillis();

		final Task<Void> task = new Task<Void>(new Callable<Void>() {
			public Void call() throws Exception {
				Thread.sleep(250);
				return null;
			}
		}).executeAsync().onSuccess(new OnSuccess<Void>() {
			public void execute(@Nullable Void result) {
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		});

		task.getResult();
		final long middle = System.currentTimeMillis();
		Assert.assertTrue(String.valueOf(true), ((middle - start) < 500));


		task.await();
		final long end = System.currentTimeMillis();
		Assert.assertTrue(String.valueOf(true), ((end - start) >= 500));

	}
}
