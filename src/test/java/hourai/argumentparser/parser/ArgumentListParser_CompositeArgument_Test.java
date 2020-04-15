/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */

package hourai.argumentparser.parser;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import hourai.argumentparser.model.Argument;
import hourai.argumentparser.model.ArgumentHelp;

/**
 * Tests the composite flag argument feature
 */
@RunWith(value = Parameterized.class)
public class ArgumentListParser_CompositeArgument_Test {
	@Parameterized.Parameters(name = "ArgumentParser_FlaggedArgument_Test#{index}: {0} = position:{1}, parameters:{2}, fail:{3}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{ /*buildTestData(1, 0, 0, false, "--test"),
			buildTestData(2, 1, 0, false, "--test", "1"),
			buildTestData(3, 2, 0, false, "--test", "1", "2"),
			buildTestData(2, 1, 1, false, "a", "--test", "1"),
			buildTestData(0, 2, 0, true, "--test")*/});
	}

	/**
	 * Creates a bunch of flag arguments (using some shenanigan)
	 *
	 * @param arguments The list of flag params
	 * @return A collection of flag arguments
	 */
	private static Argument[] flagArgument(String... arguments) {
		Argument[] result = new Argument[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			result[i] = new Argument();
			result[i].setLongCommand(arguments[i]);
			result[i].setShortCommand(arguments[i]);
			result[i].setParameterCount(0);
			//Fake help
			ArgumentHelp help = new ArgumentHelp();
			help.setCommandLineName("test");
			result[i].setHelp(help);
		}
		return result;
	}

	/**
	 * Mocks up a simple argument
	 *
	 * @param param the parameter -l or --l
	 * @param paramCount The number of params
	 * @return
	 */
	private static Argument simpleArgument(String param, int paramCount) {
		Argument result = new Argument();
		result.setLongCommand(param);
		result.setShortCommand(param);
		result.setParameterCount(paramCount);
		//Fake help
		ArgumentHelp help = new ArgumentHelp();
		help.setCommandLineName("test");
		result.setHelp(help);
		return result;
	}

	public ArgumentListParser_CompositeArgument_Test(Argument[] flags, Argument[] arguments) {

	}
}
