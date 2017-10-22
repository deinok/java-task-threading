package com.github.deinok.threading;

import org.junit.Assert;
import org.junit.Test;

public class TaskStaticFromResult extends BaseTest {

	@Test(timeout = 100)
	public void fromResultFact() {
		Assert.assertEquals(5, (int) Task.fromResult(5).getResult());
	}

}
