package com.github.deinok.threading;

import org.junit.Assert;
import org.junit.Test;

public class TaskIsCompletedTest extends BaseTest {

	private Task<Void> task = Task.delay(Constants.DefaultTimeOut);

	@Test
	public void isCompletedTrueFact() {
		task.runSynchronously();
		Assert.assertTrue(task.isCompleted());
	}

	@Test
	public void isCompletedFalseFact() {
		task.start();
		Assert.assertFalse(task.isCompleted());
	}

}
