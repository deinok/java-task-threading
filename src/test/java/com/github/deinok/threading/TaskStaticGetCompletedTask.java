package com.github.deinok.threading;

import org.junit.Test;

public class TaskStaticGetCompletedTask {

	@Test(timeout = Constants.DefaultTimeOut)
	public void getCompletedTaskFact() {
		Task.getCompletedTask().await();
	}

}
