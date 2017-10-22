package com.github.deinok.threading;

import org.junit.Test;

public class TaskStaticGetCompletedTaskTest extends BaseTest {

	@Test(timeout = Constants.DefaultTimeOut)
	public void getCompletedTaskFact() {
		Task.getCompletedTask().await();
	}

}
