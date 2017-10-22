package com.github.deinok.threading;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TaskContinueWithTest extends BaseTest {

	@Test
	public void continueWithFact() {
		final Task<Integer> initialTask = new Task<Integer>(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				Thread.sleep(Constants.Default25PercentTimeOut);
				return Constants.Default25PercentTimeOut;
			}
		});
		Assert.assertTrue(initialTask.getStatus() == TaskStatus.Created);

		final Task<Void> finalTask = initialTask.continueWith(new Task.ContinueWithAction<Integer>() {
			@Override
			public void continueAction(@NotNull Task<Integer> antecedentTask) throws Exception {
				Assert.assertTrue(antecedentTask.isCompleted());
				Thread.sleep(antecedentTask.getResult());
			}
		});
		Assert.assertTrue(initialTask.getStatus() == TaskStatus.Created);
		Assert.assertTrue(finalTask.getStatus() == TaskStatus.Created);

		finalTask.await();
		Assert.assertTrue(initialTask.isCompleted());
		Assert.assertTrue(finalTask.isCompleted());
	}

}
