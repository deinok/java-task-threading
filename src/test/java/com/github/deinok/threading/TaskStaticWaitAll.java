package com.github.deinok.threading;

import org.junit.Test;

public class TaskStaticWaitAll extends BaseTest {

	@Test(timeout = Constants.DefaultTimeOut)
	public void waitAllFact1() {
		Task task1 = Task.delay(Constants.Default90PercentTimeOut).executeAsync();
		Task task2 = Task.delay(Constants.Default90PercentTimeOut).executeAsync();
		Task task3 = Task.delay(Constants.Default90PercentTimeOut).executeAsync();
		Task.waitAll(task1, task2, task3);
	}

	@Test(timeout = Constants.DefaultTimeOut)
	public void waitAllFact2() {
		Task task1 = Task.delay(Constants.Default90PercentTimeOut).executeAsync();
		Task task2 = Task.delay(Constants.Default90PercentTimeOut).executeAsync();
		Task task3 = Task.delay(Constants.Default90PercentTimeOut).executeAsync();
		Task[] taskArray = new Task[]{task1, task2, task3};
		Task.waitAll(taskArray);
	}

}
