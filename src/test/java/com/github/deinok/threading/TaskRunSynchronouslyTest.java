package com.github.deinok.threading;

import org.junit.Assert;
import org.junit.Test;

public class TaskRunSynchronouslyTest extends BaseTest {

	@Test
	public void runSynchronouslyFact() {
		Task<Void> task = Task.delay(Constants.DefaultTimeOut);
		task.runSynchronously();
		Assert.assertEquals(TaskStatus.RanToCompletion, task.getStatus());
	}

}
