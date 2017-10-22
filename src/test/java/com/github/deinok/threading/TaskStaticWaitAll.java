package com.github.deinok.threading;

import org.junit.Test;

public class TaskStaticWaitAll {

	@Test(timeout = 150)
	public void waitAllFact1() {
		Task task1 = Task.delay(100).executeAsync();
		Task task2 = Task.delay(100).executeAsync();
		Task task3 = Task.delay(100).executeAsync();
		Task.waitAll(task1, task2, task3);
	}

	@Test(timeout = 150)
	public void waitAllFact2() {
		Task task1 = Task.delay(100).executeAsync();
		Task task2 = Task.delay(100).executeAsync();
		Task task3 = Task.delay(100).executeAsync();
		Task[] taskArray = new Task[]{task1, task2, task3};
		Task.waitAll(taskArray);
	}

}
