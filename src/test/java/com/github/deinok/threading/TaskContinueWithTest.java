package com.github.deinok.threading;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TaskContinueWithTest extends BaseTest {

	@Ignore
	@Test
	public void continueWithFact() {
		final Task<Integer> initialTask = new Task<Integer>(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				Thread.sleep(Constants.Default25PercentTimeOut);
				return 0;
			}
		});
		Assert.assertTrue(initialTask.getStatus() == TaskStatus.Created);

		final Task<Void> finalTask = initialTask.continueWith(new ContinueWithAction<Integer>() {
			@Override
			public void continueAction(@NotNull Task<Integer> continuationAction) throws Exception {
				Thread.sleep(Constants.Default25PercentTimeOut);
			}
		});
		Assert.assertTrue(finalTask.getStatus() == TaskStatus.Created);


		finalTask.await();
	}

}
