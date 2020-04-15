/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */

package hourai.argumentparser.builder;

import hourai.argumentparser.exception.AggregateException;
import hourai.argumentparser.interfaces.ArgumentBuilder;
import hourai.argumentparser.interfaces.ArgumentList;
import hourai.argumentparser.interfaces.CommandLineArgument;
import hourai.argumentparser.model.Argument;
import hourai.argumentparser.parser.ArgumentListParser;
import java.util.ArrayList;
import java.util.List;

/**
 * Constructs an argument list
 */
public class ArgumentListBuilder {

	private int currentFixedIndex = 0;
	private boolean isCurrentlyInRequiredList = true;
	private final List<Argument> arguments = new ArrayList<Argument>();
	private Argument help;

	/**
	 * Returns a builder for a normal argument
	 *
	 * @param name The name of the argument in the command line
	 * @return The builder for this argument
	 */
	public ArgumentBuilder addArgument(String name) {
		return new ArgumentBuilderImpl(this, name, -1, false);
	}

	/**
	 * Sets the provided command line argument as the help argument
	 *
	 * @param help The help argument
	 */
	public void setHelpArgument(CommandLineArgument help) {
		if (help instanceof Argument) {

		}
	}

	/**
	 * Returns a builder for a positional argument.
	 *
	 * @param name The to display for this argument, has no relation to the
	 * command line
	 * @param required If this positional argument is required. If it is not
	 * there can be no further required parameters
	 * @return The argument builder for this new argument
	 */
	public ArgumentBuilder addPositionalArgument(String name, boolean required) {
		//Track if we are currently in a required list set
		// once its false it can never be true
		isCurrentlyInRequiredList = isCurrentlyInRequiredList & required;
		if (isCurrentlyInRequiredList != required) {
			throw new IllegalArgumentException("You cannot toggle between required and optional parameters");
		}
		return new ArgumentBuilderImpl(this, name, currentFixedIndex++, required);
	}

	/**
	 * @return Creates the template help message and sets it as our help
	 */
	public CommandLineArgument createTemplateHelpMessage() {
		ArgumentBuilderImpl helpMessage = new ArgumentBuilderImpl(this, "help", -1, false);
		help = (Argument) helpMessage.setLongCommand("help")
				.setShortCommand("h")
				.setHelpText("This message")
				.create();
		return help;
	}

	/**
	 * A protected method used to append the argument to the list
	 *
	 * @param argument The argument to be added
	 */
	protected void addArgumentToList(Argument argument) {
		arguments.add(argument);
	}

	/**
	 * @return Returns an argument list object to extract data from the argument
	 * parser
	 */
	public ArgumentList createArgumentList() {
		try {
			return new ArgumentListImpl(new ArgumentValidator(arguments));
		} catch (AggregateException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates an argument parser that can parse the constructed arguments
	 *
	 * @return The Argument list parser that can process the arguments
	 * @throws AggregateException Is thrown if there is a validation error with
	 * the parameters
	 */
	public ArgumentListParser createParser() throws AggregateException {
		ArgumentValidator validator = new ArgumentValidator(arguments);
		ArgumentListParser parser = new ArgumentListParser(validator, help, true);
		return parser;
	}

}
