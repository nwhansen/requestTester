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
 * Tests the block string functionality for the help printer
 */
@RunWith(value = Parameterized.class)
public class StringManipulation_BlockStringTest {

	@Parameterized.Parameters(name = "{index}: blockString(''{0}'', {1})")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{
			{"ThisIsAMessageThatCannotBeBroken", 4, new String[]{"This", "IsAM", "essa", "geTh", "atCa", "nnot", "BeBr", "oken"}},
			{"All words in here break nicely", 9, new String[]{"All words", "in here ", "break ", "nicely"}},
			{"Time to make. Some Strange Tests", 5, new String[]{"Time ", "to ", "make.", "Some ", "Stran", "ge ", "Tests"}}
		});
	}

	private final String test;
	private final int width;
	private final String[] expected;

	public StringManipulation_BlockStringTest(String test, int width, String[] expected) {
		this.test = test;
		this.width = width;
		this.expected = expected;
	}

	@Test
	public void bockString_Test() {
		//Act:
		String[] result = StringManipulation.blockString(test, width);

		//Assert:
		Assert.assertArrayEquals(expected, result);
	}
}
