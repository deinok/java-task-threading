package com.github.deinok.threading;

import org.junit.Assert;
import org.junit.Test;

public class TaskGetIdTest extends BaseTest {

	@Test
	public void getIdFact() {
		Task task1 = Task.getCompletedTask();
		Task task2 = Task.getCompletedTask();
		Assert.assertNotEquals(task1.getId(), task2.getId());
	}

}
