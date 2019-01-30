/*
 * Copyright 2018 Nathan Hansen. ALL RIGHTS RESERVED
 */
package hourai.argumentparser.parser;

import hourai.argumentparser.model.Argument;
import hourai.argumentparser.model.ArgumentHelp;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the argument parser for simple positional arguments
 */
@RunWith(value = Parameterized.class)
public class ArgumentParser_PositionalArgument_Test {

	@Parameterized.Parameters(name = "ArgumentParser_PositionalArgument_Test#{index}: position:{1}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{
			buildTestData(0, "1"),
			buildTestData(1, "1", "2"),
			buildTestData(2, "--test", "1", "2")
		});
	}

	private static Object[] buildTestData(int position, String... params) {
		//The result param is always the "index" of the position.
		return new Object[]{position, params, new String[]{params[position]}};
	}

	private final int position;
	private final String[] arguments;
	private final String[] resultParams;

	public ArgumentParser_PositionalArgument_Test(int position, String[] arguments, String[] resultParams) {
		this.position = position;
		this.arguments = arguments;
		this.resultParams = resultParams;
	}

	@Test
	public void test() {
		//Arrange
		Argument argument = createPositionalArgument();
		ArgumentParser target = new ArgumentParser();

		int result = target.parsePositional(argument, arguments, position);

		//If we made here its present
		assertTrue(argument.getIsPresent());
		assertEquals(1, result);
		assertArrayEquals(resultParams, argument.getParameters());
		if (resultParams.length > 0) {
			assertEquals(resultParams[0], argument.getValue());
		}
	}

	private Argument createPositionalArgument() {
		Argument arg = new Argument();
		//Any non -1 will work (this matters more for the list parser)
		arg.setPositional(2);
		//Positional doesn't really care about this either.
		// Parsing positional just takes the position's value
		arg.setParameterCount(1);
		ArgumentHelp help = new ArgumentHelp();
		help.setCommandLineName("test");
		arg.setHelp(help);
		return arg;
	}

}
