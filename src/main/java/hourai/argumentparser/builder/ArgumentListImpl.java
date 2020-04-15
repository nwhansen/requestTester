/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */

package hourai.argumentparser.builder;

import hourai.argumentparser.interfaces.ArgumentList;
import hourai.argumentparser.interfaces.CommandLineArgument;
import hourai.argumentparser.model.Argument;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of the argument list interface using the validator
 */
class ArgumentListImpl implements ArgumentList {

	private final List<CommandLineArgument> availableArguments;
	private final List<CommandLineArgument> requiredPositionals;
	private final List<CommandLineArgument> positionals;

	/**
	 * Creates the argument list impl with the validator to populate its lists
	 *
	 * @param validator
	 */
	public ArgumentListImpl(ArgumentValidator validator) {
		availableArguments = copyList(validator.getAvailableArguments());
		requiredPositionals = copyList(validator.getRequiredPositionals());
		positionals = copyList(validator.getPositionalArguments());
	}

	private List<CommandLineArgument> copyList(List<Argument> source) {
		List<CommandLineArgument> list = new ArrayList<CommandLineArgument>();
		for (Argument argument : source) {
			list.add(argument);
		}
		return list;
	}

	@Override
	public Iterable<CommandLineArgument> getAvailableArguments() {
		return availableArguments;
	}

	@Override
	public Iterable<CommandLineArgument> getRequiredPositionalArguments() {
		return requiredPositionals;
	}

	@Override
	public Iterable<CommandLineArgument> getPositionalArguments() {
		return positionals;
	}

}
