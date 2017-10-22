package com.github.deinok.threading;

import org.junit.Assert;
import org.junit.Test;

public class TaskStaticWaitAnyTest extends BaseTest {

	@Test(timeout = Constants.DefaultTimeOut)
	public void waitAnyFact1() {
		Task task1 = Task.delay(Constants.Default125PercentTimeOut).start();
		Task task2 = Task.delay(Constants.Default50PercentTimeOut).start();
		Task task3 = Task.delay(Constants.Default125PercentTimeOut).start();
		int firstFinished = Task.waitAny(task1, task2, task3);
		Assert.assertEquals(1, firstFinished);
	}

	@Test(timeout = Constants.DefaultTimeOut)
	public void waitAnyFact2() {
		Task task1 = Task.delay(Constants.Default125PercentTimeOut).start();
		Task task2 = Task.delay(Constants.Default50PercentTimeOut).start();
		Task task3 = Task.delay(Constants.Default125PercentTimeOut).start();
		Task[] taskArray = new Task[]{task1, task2, task3};
		int firstFinished = Task.waitAny(taskArray);
		Assert.assertEquals(1, firstFinished);
	}

}
