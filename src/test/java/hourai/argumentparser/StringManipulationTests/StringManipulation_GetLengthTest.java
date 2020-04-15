/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */
package hourai.argumentparser.StringManipulationTests;

import hourai.argumentparser.StringManipulation;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Tests the getLenth method of StringManipulation
 */
@RunWith(value = Parameterized.class)
public class StringManipulation_GetLengthTest {

	private final String test;
	private final int length;

	@Parameterized.Parameters(name = "{index}: getLength(''{0}'') == {1}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{
			{null, 0},
			{"", 0},
			{" ", 1},
			{"22", 2}
		});
	}

	public StringManipulation_GetLengthTest(String string, int length) {
		//Arrange:
		this.test = string;
		this.length = length;
	}

	@Test
	public void getLength_Test() {
		//Act
		int result = StringManipulation.getLength(test);
		//Assert
		Assert.assertEquals(length, result);
	}

}
