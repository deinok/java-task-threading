package com.github.deinok.threading;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TaskStaticRun extends BaseTest {

	@Test
	public void taskRunFact() {
		Task<Integer> asyncTask = Task.run(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				Thread.sleep(1000);
				return 5;
			}
		});
		Assert.assertEquals(5, (int) asyncTask.getResult());
	}

}
