package com.github.deinok.threading;

import org.junit.Test;

public class TaskStaticWhenAll {

	@Test(timeout = 1500)
	public void whenAllFact() {
		Task<Void> task1 = Task.delay(1000).executeAsync();
		Task<Void> task2 = Task.delay(1000).executeAsync();
		Task<Void> task3 = Task.delay(1000).executeAsync();
		Task[] taskArray = new Task[]{task1, task2, task3};
		Task<Void> whenAllTask = Task.whenAll(taskArray);
		whenAllTask.await();
	}

}
