package com.github.deinok.threading;

import org.junit.Test;

public class TaskStaticDelay {

	@Test(timeout = 100)
	public void delayNotWaitFact() {
		Task<Void> task = Task.delay(900);
	}

	@Test(timeout = 1000)
	public void delayWaitFact() {
		Task<Void> task = Task.delay(900);
		task.getResult();
	}

}
