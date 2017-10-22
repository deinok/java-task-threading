package com.github.deinok.threading;

import org.junit.Test;

public class TaskStaticWaitAllTest extends BaseTest {

	@Test(timeout = Constants.DefaultTimeOut)
	public void waitAllFact1() {
		Task task1 = Task.delay(Constants.Default90PercentTimeOut).start();
		Task task2 = Task.delay(Constants.Default90PercentTimeOut).start();
		Task task3 = Task.delay(Constants.Default90PercentTimeOut).start();
		Task.waitAll(task1, task2, task3);
	}

	@Test(timeout = Constants.DefaultTimeOut)
	public void waitAllFact2() {
		Task task1 = Task.delay(Constants.Default90PercentTimeOut).start();
		Task task2 = Task.delay(Constants.Default90PercentTimeOut).start();
		Task task3 = Task.delay(Constants.Default90PercentTimeOut).start();
		Task[] taskArray = new Task[]{task1, task2, task3};
		Task.waitAll(taskArray);
	}

}
