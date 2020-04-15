/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */

package hourai.argumentparser.builder;

import hourai.argumentparser.exception.AggregateException;
import hourai.argumentparser.model.Argument;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * A service that validates the argument list
 */
public class ArgumentValidator {
	/**
	 * A class that sorts arguments in positional ascending order
	 */
	private class ArgumentSorter implements Comparator<Argument> {

		@Override
		public int compare(Argument o1, Argument o2) {
			return o1.getPositional() - o2.getPositional();
		}

	}
	private final List<Argument> requiredPositionals;
	private final List<Argument> positionalArguments;
	private final List<Argument> availableArguments;

	public ArgumentValidator(List<Argument> arguments) throws AggregateException {
		this.availableArguments = arguments;
		this.requiredPositionals = new ArrayList<Argument>();
		this.positionalArguments = new ArrayList<Argument>();
		validateArguments();
	}

	/**
	 * @return The arguments that are required and have a position
	 */
	public List<Argument> getRequiredPositionals() {
		return requiredPositionals;
	}

	/**
	 * @return the positionalArguments sorted and ready for usage
	 */
	public List<Argument> getPositionalArguments() {
		return positionalArguments;
	}

	/**
	 * @return All of the arguments avaiable
	 */
	public List<Argument> getAvailableArguments() {
		return availableArguments;
	}

	private void validateArguments() throws AggregateException {
		List<Throwable> failures = new ArrayList<Throwable>();
		//Get the required
		for (Argument argument : availableArguments) {
			if (argument.getIsPositionalRequred()) {
				requiredPositionals.add(argument);
			} else if (argument.getPositional() != -1) {
				positionalArguments.add(argument);
			}
		}
		//Sort
		requiredPositionals.sort(new ArgumentValidator.ArgumentSorter());
		positionalArguments.sort(new ArgumentValidator.ArgumentSorter());
		//Validate order
		failures.addAll(validatePositional(requiredPositionals));
		failures.addAll(validatePositional(positionalArguments));
		failures.addAll(validateListTransition(requiredPositionals, positionalArguments));
		//Validate the flags
		HashSet<String> argumentSet = new HashSet<String>();
		for (Argument argument : availableArguments) {
			if (argument.getShortCommand() != null) {
				String command = "-" + argument.getShortCommand();
				if (argumentSet.contains(command)) {
					failures.add(new IllegalArgumentException(String.format("The command \"%s\" duplicates short command \"%s\"",
							argument.getHelp().getCommandLineName(), command)));
				} else {
					argumentSet.add(command);
				}
			}
			if (argument.getLongCommand() != null) {
				String command = "--" + argument.getLongCommand();
				if (argumentSet.contains(command)) {
					failures.add(new IllegalArgumentException(String.format("The command \"%s\" duplicates long command \"%s\"",
							argument.getHelp().getCommandLineName(), command)));
				} else {
					argumentSet.add(command);
				}
			}
		}
		if (failures.size() > 0) {
			throw new AggregateException("Arguments violate unique command name rules", failures);
		}
	}

	private static List<Throwable> validateListTransition(List<Argument> requiredPositionals, List<Argument> positionalArguments) {
		List<Throwable> errors = new ArrayList<Throwable>();
		if (!requiredPositionals.isEmpty() && positionalArguments.isEmpty()) {
			if (requiredPositionals.get(0).getPositional() != 0) {
				errors.add(new IllegalArgumentException("Required parameter list starts at a non-zero amount"));
			}
		} else if (requiredPositionals.isEmpty() && !positionalArguments.isEmpty()) {
			if (positionalArguments.get(0).getPositional() != 0) {
				errors.add(new IllegalArgumentException("Positional parameter list starts at a non-zero amount"));
			}
		} else if (!requiredPositionals.isEmpty() && !positionalArguments.isEmpty()) {
			int requiredPosition = requiredPositionals.get(requiredPositionals.size() - 1).getPositional();
			int positionalPosition = positionalArguments.get(0).getPositional();
			if (positionalPosition - requiredPosition != 1) {
				errors.add(new IllegalArgumentException("Required Parameters and Positional parameters do not match"));
			}
		}
		return errors;
	}

	private static List<Throwable> validatePositional(List<Argument> arguments) {
		List<Throwable> errors = new ArrayList<Throwable>();
		if (arguments.isEmpty()) {
			return errors;
		}
		int lastPosition = -1;
		for (Argument argument : arguments) {
			if (argument.getPositional() < lastPosition) {
				errors.add(new IllegalArgumentException(String.format("The command \"%s\" position is invalid, it either duplicates another", argument.getHelp().getCommandLineName())));
			} else if (lastPosition != -1 && argument.getPositional() - 1 != lastPosition) {
				errors.add(new IllegalArgumentException(String.format("The command \"%s\" position is not sequential", argument.getHelp().getCommandLineName())));
			}
			lastPosition = argument.getPositional();
		}
		return errors;
	}


}
