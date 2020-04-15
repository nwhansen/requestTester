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
 * Tests the StringManipulation
 */
@RunWith(value = Parameterized.class)
public class StringManipulation_FixWidthTest {

	@Parameterized.Parameters(name = "{index}: fixWidth(''{0}'', {1}) == ''{2}''")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{
			{null, 8, "        "},
			{"", 8, "        "},
			{"Test", 1, "T"},
			{"Simple", 8, "Simple  "}
		});
	}

	private final String source;
	private final int width;
	private final String expected;

	public StringManipulation_FixWidthTest(String source, int width, String expected) {
		//Arrange:
		this.source = source;
		this.width = width;
		this.expected = expected;
	}

	@Test
	public void fixWidth_Test() {
		//Act
		String result = StringManipulation.fixWidth(source, width);
		//Assert
		Assert.assertEquals(expected, result);
	}
}
