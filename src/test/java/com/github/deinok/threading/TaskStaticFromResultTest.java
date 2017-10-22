package com.github.deinok.threading;

import org.junit.Assert;
import org.junit.Test;

public class TaskStaticFromResultTest extends BaseTest {

	@Test(timeout = Constants.DefaultTimeOut)
	public void fromResultFact() {
		Assert.assertEquals(5, (int) Task.fromResult(5).getResult());
	}

}
