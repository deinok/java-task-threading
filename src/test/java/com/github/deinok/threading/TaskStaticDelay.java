package com.github.deinok.threading;

import org.junit.Test;

public class TaskStaticDelay {

	@Test(timeout = 100)
	public void delayNotWaitFact() {
		Task task = Task.delay(900);
		task.executeAsync();
	}

	@Test(timeout = 150)
	public void delayWaitFact() {
		Task task = Task.delay(100);
		task.getResult();
	}

}
