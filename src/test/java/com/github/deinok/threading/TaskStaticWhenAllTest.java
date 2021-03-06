package com.github.deinok.threading;

import org.junit.Test;

public class TaskStaticWhenAllTest extends BaseTest {

	@Test(timeout = Constants.DefaultTimeOut)
	public void whenAllFact1() {
		Task task1 = Task.delay(Constants.Default90PercentTimeOut);
		task1.start();
		Task task2 = Task.delay(Constants.Default90PercentTimeOut);
		task2.start();
		Task task3 = Task.delay(Constants.Default90PercentTimeOut);
		task3.start();
		Task whenAllTask = Task.whenAll(task1, task2, task3);
		whenAllTask.await();
	}

	@Test(timeout = Constants.DefaultTimeOut)
	public void whenAllFact2() {
		Task task1 = Task.delay(Constants.Default90PercentTimeOut);
		task1.start();
		Task task2 = Task.delay(Constants.Default90PercentTimeOut);
		task2.start();
		Task task3 = Task.delay(Constants.Default90PercentTimeOut);
		task3.start();
		Task[] taskArray = new Task[]{task1, task2, task3};
		Task whenAllTask = Task.whenAll(taskArray);
		whenAllTask.await();
	}

}
