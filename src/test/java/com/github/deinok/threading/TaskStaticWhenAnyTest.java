package com.github.deinok.threading;

import org.junit.Assert;
import org.junit.Test;

public class TaskStaticWhenAnyTest extends BaseTest {

	@Test(timeout = Constants.DefaultTimeOut)
	public void whenAnyFact1() {
		Task task1 = Task.delay(Constants.Default125PercentTimeOut).start();
		Task task2 = Task.delay(Constants.Default50PercentTimeOut).start();
		Task task3 = Task.delay(Constants.Default125PercentTimeOut).start();
		Task<Task> firstFinished = Task.whenAny(task1, task2, task3);
		Assert.assertEquals(task2, firstFinished.getResult());
	}

	@Test(timeout = Constants.DefaultTimeOut)
	public void whenAnyFact2() {
		Task task1 = Task.delay(Constants.Default125PercentTimeOut).start();
		Task task2 = Task.delay(Constants.Default50PercentTimeOut).start();
		Task task3 = Task.delay(Constants.Default125PercentTimeOut).start();
		Task[] taskArray = new Task[]{task1, task2, task3};
		Task<Task> firstFinished = Task.whenAny(taskArray);
		Assert.assertEquals(task2, firstFinished.getResult());
	}

}
