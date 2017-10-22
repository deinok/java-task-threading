package com.github.deinok.threading;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TaskOnExceptionTests {

	@Test
	public void onExceptionTest1() {
		final RuntimeException testedException = new RuntimeException("OK");
		final RuntimeThreadException[] exceptionCatch = new RuntimeThreadException[1];

		final Task<Void> task = new Task<Void>(new Callable<Void>() {
			public Void call() throws Exception {
				Thread.sleep(250);
				throw testedException;
			}
		}).onException(new OnException() {
			public void execute(@NotNull RuntimeThreadException exception) {
				exceptionCatch[0] = exception;
			}
		}).executeAsync();

		task.await();

		Assert.assertEquals(testedException, exceptionCatch[0].getCause());

	}

	@Test
	public void onExceptionTest2() {
		final RuntimeException testedException = new RuntimeException("OK");
		final RuntimeThreadException[] exceptionCatch = new RuntimeThreadException[1];

		final Task<Void> task = new Task<Void>(new Callable<Void>() {
			public Void call() throws Exception {
				Thread.sleep(250);
				return null;
			}
		}).onSuccess(new OnSuccess<Void>() {
			public void execute(@Nullable Void result) {
				throw testedException;
			}
		}).onException(new OnException() {
			public void execute(@NotNull RuntimeThreadException exception) {
				exceptionCatch[0] = exception;
			}
		}).executeAsync();

		task.await();

		Assert.assertEquals(testedException, exceptionCatch[0].getCause());

	}

}
