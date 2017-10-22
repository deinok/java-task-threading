package com.github.deinok.threading;

import org.junit.Test;

public class TaskStaticGetCompletedTask {

	@Test(timeout = 100)
	public void getCompletedTaskFact() {
		Task.getCompletedTask().await();
	}

}
