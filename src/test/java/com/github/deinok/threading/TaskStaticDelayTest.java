package com.github.deinok.threading;

import org.junit.Test;

public class TaskStaticDelayTest extends BaseTest {

	@Test(timeout = Constants.DefaultTimeOut)
	public void delayNotWaitFact() {
		Task task = Task.delay(Constants.Default125PercentTimeOut);
		task.executeAsync();
	}

	@Test(timeout = Constants.DefaultTimeOut)
	public void delayWaitFact() {
		Task task = Task.delay(Constants.Default90PercentTimeOut);
		task.getResult();
	}

}
