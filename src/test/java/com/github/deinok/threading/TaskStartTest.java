package com.github.deinok.threading;

import org.junit.Test;

public class TaskStartTest extends BaseTest {

	@Test(timeout = Constants.DefaultTimeOut)
	public void startFact() {
		Task<Void> task = Task.delay(Constants.Default125PercentTimeOut);
		task.start();
	}

}
