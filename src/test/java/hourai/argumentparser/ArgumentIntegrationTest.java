/*
 * Copyright 2018 Nathan Hansen. ALL RIGHTS RESERVED
 */

package hourai.argumentparser;

import hourai.argumentparser.builder.ArgumentListBuilder;
import hourai.argumentparser.exception.AggregateException;
import hourai.argumentparser.interfaces.ArgumentResult;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ArgumentIntegrationTest {

	@Test
	public void test() throws AggregateException {
		String[] args = new String[]{"0", "1", "2", "--test", "test", "4th", "-b", "5"};
		ArgumentListBuilder builder = new ArgumentListBuilder();
		ArgumentResult arg1 = builder.addPositionalArgument("0th", true)
				.setHelpText("Hi")
				.create();
		ArgumentResult arg2 = builder.addPositionalArgument("1st", true)
				.setHelpText("test")
				.create();
		ArgumentResult arg3 = builder.addPositionalArgument("2nd", false)
				.setHelpText("test2")
				.create();
		ArgumentResult arg4 = builder.addPositionalArgument("3rd", false)
				.setHelpText("test3")
				.create();
		ArgumentResult param1 = builder.addArgument("Testing")
				.setLongCommand("test")
				.setShortCommand('t')
				.appendParameter("hi", "nope")
				.create();
		ArgumentResult param2 = builder.addArgument("Best")
				.appendParameter("Second", "Yes")
				.setShortCommand("b")
				.setLongCommand("--builder")
				.create();
		hourai.argumentparser.parser.ArgumentListParser createParser = builder.createParser();
		createParser.parseArguments(args);

		Assert.assertTrue(arg1.getIsPresent());

	}
}
