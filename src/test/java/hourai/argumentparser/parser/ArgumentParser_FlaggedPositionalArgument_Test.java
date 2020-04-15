/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */

package hourai.argumentparser.parser;

import hourai.argumentparser.model.Argument;
import hourai.argumentparser.model.ArgumentHelp;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the argument parser for a flagged parameter which also is positional
 * (implicit parameter)
 */
@RunWith(value = Parameterized.class)
public class ArgumentParser_FlaggedPositionalArgument_Test {

	@Parameterized.Parameters(name = "ArgumentParser_FlaggedArgument_Test#{index}: {0} = position:{1}, parameters:{2}, fail:{3}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{
			buildTestData(1, 0, 0, false, "--test"),
			buildTestData(2, 1, 0, false, "--test", "1"),
			buildTestData(3, 2, 0, false, "--test", "1", "2"),
			buildTestData(2, 1, 1, false, "a", "--test", "1"),
			buildTestData(0, 2, 0, true, "--test")
		});
	}

	private static Object[] buildTestData(int step, int parameter, int position, boolean fail, String... params) {
		//Build the params response
		List<String> assertParams = new ArrayList<String>();
		for (int i = 1; i <= parameter; i++) {
			assertParams.add(Integer.toString(i));
		}
		return new Object[]{step, parameter, position, fail, params, assertParams.toArray(new String[0])};
	}

	private final int step;
	private final int parameterCount;
	private final int position;
	private final boolean invalid;
	private final String[] arguments;
	private final String[] resultParams;

	public ArgumentParser_FlaggedPositionalArgument_Test(int step, int parameterCount, int position, boolean invalid, String[] arguments, String[] resultParams) {
		this.step = step;
		this.parameterCount = parameterCount;
		this.position = position;
		this.arguments = arguments;
		this.invalid = invalid;
		this.resultParams = resultParams;
	}


	@Test
	public void test() {
		//Arrange
		Argument argument = createFlagArgument("test", "t", parameterCount);
		ArgumentParser target = new ArgumentParser();

		try {
			int result = target.parseFlaggedArgument(argument, arguments, position);

			assertFalse(invalid);
			//If we made here its present
			assertTrue(argument.getIsPresent());
			assertEquals(step, result);
			assertArrayEquals(resultParams, argument.getParameters());
		} catch (InvalidParameterException ex) {
			assertTrue(invalid);
		}
	}

	private Argument createFlagArgument(String longName, String shortName, int params) {
		Argument arg = new Argument();
		arg.setLongCommand(longName);
		arg.setShortCommand(shortName);
		arg.setParameterCount(params);
		ArgumentHelp help = new ArgumentHelp();
		help.setCommandLineName("test");
		arg.setHelp(help);
		return arg;
	}

}
