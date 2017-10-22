package com.github.deinok.threading;

import org.junit.Test;

public class TaskStaticWhenAll {

	@Test(timeout = 150)
	public void whenAllFact1() {
		Task task1 = Task.delay(100).executeAsync();
		Task task2 = Task.delay(100).executeAsync();
		Task task3 = Task.delay(100).executeAsync();
		Task whenAllTask = Task.whenAll(task1, task2, task3);
		whenAllTask.await();
	}

	@Test(timeout = 150)
	public void whenAllFact2() {
		Task task1 = Task.delay(100).executeAsync();
		Task task2 = Task.delay(100).executeAsync();
		Task task3 = Task.delay(100).executeAsync();
		Task[] taskArray = new Task[]{task1, task2, task3};
		Task whenAllTask = Task.whenAll(taskArray);
		whenAllTask.await();
	}

}
