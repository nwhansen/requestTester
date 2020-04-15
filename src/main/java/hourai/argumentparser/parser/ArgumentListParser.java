/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */
package hourai.argumentparser.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hourai.argumentparser.StringManipulation;
import hourai.argumentparser.builder.ArgumentValidator;
import hourai.argumentparser.model.Argument;

/**
 * A service which parses arguments
 */
public class ArgumentListParser {

	private final List<Argument> requiredPositionals;
	private final List<Argument> positionalArguments;
	private final Map<String, Argument> argumentMapping;
	//A help argument bypasses the normal rules (It can be at any point) 
	private final Argument helpArgument;
	private final ArgumentParser parser = new ArgumentParser();
	private final boolean exitIfFail;

	/**
	 * Creates the argument parser that has the
	 *
	 * @param validator The argument validator that has validated the argument
	 * list
	 * @param help The help argument
	 * @param exitIfFail If we fail to process the argument list, exit the JVM
	 */
	public ArgumentListParser(ArgumentValidator validator, Argument help, boolean exitIfFail) {
		this.requiredPositionals = validator.getRequiredPositionals();
		this.positionalArguments = validator.getPositionalArguments();
		//All arguments except required positional
		argumentMapping = createMapping(validator.getAvailableArguments());
		this.helpArgument = help;
		this.exitIfFail = exitIfFail;
	}

	private static Map<String, Argument> createMapping(List<Argument> availableArguments) {
		Map<String, Argument> mapping = new HashMap<String, Argument>();
		final String longFormat = "--%s";
		final String shortFormat = "-%s";
		for (Argument argument : availableArguments) {
			if (!argument.getIsPositionalRequred()) {
				if (StringManipulation.getLength(argument.getLongCommand()) != 0) {
					mapping.put(String.format(longFormat, argument.getLongCommand()), argument);
				}
				if (StringManipulation.getLength(argument.getShortCommand()) != 0) {
					mapping.put(String.format(shortFormat, argument.getShortCommand()), argument);
				}
			}
		}
		return mapping;
	}

	public void parseArguments(String[] arguments) {
		if (arguments == null || arguments.length == 0) {
			if (!requiredPositionals.isEmpty()) {
				//Print that we are missing an argument
				exitOrThrow(String.format("Missing required argument: %s", requiredPositionals.get(0).getHelp().getCommandLineName()));
			}
			return;
		} else {
			if (helpArgument != null) {
				//Check for help argument and parse as such terminating early
				for (int i = 0; i < arguments.length; i++) {
					//If there is a help return
					Argument argument = argumentMapping.get(arguments[i]);
					if (argument == helpArgument) {
						parser.parseFlaggedArgument(argument, arguments, i);
						return;
					}
				}
			} else if (arguments.length < requiredPositionals.size()) {
				exitOrThrow(String.format("Missing required argument: %s", requiredPositionals.get(arguments.length - 1).getHelp().getCommandLineName()));
			}
		}

		//Its critical here our lists are constructed properly
		int parsingPosition = 0;
		if (!requiredPositionals.isEmpty()) {
			//This will allways succeed
			for (Argument argument : requiredPositionals) {
				parsingPosition += parser.parsePositional(argument, arguments, parsingPosition);
			}
		}
		//Check if we need to bail
		if (parsingPosition >= arguments.length) {
			return;
		}
		//We need to track our virtual position vs our actual position.
		// This allows for flags and params to be intermixed
		int virtualPosition = requiredPositionals.size();
		int positionalIndex;
		//Track our "next" position, persists into our parsing after each
		// iteration
		int nextPosition = parsingPosition;
		do {
			//We are processing at the "next" location
			parsingPosition = nextPosition;
			String currentArgumentString = arguments[parsingPosition];
			Argument argument = argumentMapping.get(currentArgumentString);
			if (argument != null) {
				//Attempt to do normal processing. If it is processed then
				// move on
				nextPosition += parser.parseFlaggedArgument(argument, arguments, parsingPosition);
			} else if (currentArgumentString.startsWith("-") && !currentArgumentString.startsWith("--") && currentArgumentString.length() > 2) {
				//We have a single dash (but doesn't match) & length > 2 (-l == 2)
				// So it is likely a composite flag group
				int[] subArguments = currentArgumentString.chars().toArray();
				//Skip the first
				int i;
				for (i = 1; i < subArguments.length; i++) {
					argument = argumentMapping.get(String.format("-%s", Character.toString((char) subArguments[i])));
					//If the argument wasn't found break this loop
					if (argument == null) {
						break;
					}
					//If we are the last parse normally
					if (i == subArguments.length - 1) {
						nextPosition += parser.parseFlaggedArgument(argument, arguments, parsingPosition);
					} else {
						//If we are a positional (we have an implicit paramter, or if we have an explicit paramter)
						if (argument.getPositional() == -1 && argument.getParameterCount() == 0) {
							//Mark present
							argument.setIsPresent(true);
						} else {
							break;
						}
					}
				}
			} else if ((positionalIndex = getNextVirualPosition(virtualPosition)) != -1) {
				//Use our virutal position for argument processing
				argument = positionalArguments.get(positionalIndex);
				nextPosition += parser.parsePositional(argument, arguments, parsingPosition);
				virtualPosition++;
			}
		} while (nextPosition != parsingPosition && nextPosition < arguments.length);
		if (nextPosition != arguments.length) {
			//Maybe list where we failed?
			exitOrThrow(String.format("There are additional unprocessed arguments have you miss typed something? Argument '%s' is where processing failed", arguments[parsingPosition]));
		}
	}

	/**
	 * This computes the next position skipping arguments already processed via
	 * flags
	 *
	 * @param virtualPostion The current virtual position
	 * @return The next virtual position or -1 if we failed to find one
	 */
	private int getNextVirualPosition(int virtualPostion) {
		//go back 1 so the do/while is easier and doesn't require a special case
		virtualPostion -= 1;
		int index;
		Argument argument;
		do {
			virtualPostion++;
			index = virtualPostion - requiredPositionals.size();
			if (index >= positionalArguments.size() || index < 0) {
				return -1;
			}
			argument = positionalArguments.get(index);
		} while (argument != null && argument.getIsPresent());
		return index;
	}

	private void exitOrThrow(String message) {
		if (exitIfFail) {
			//We have insufficient arguments
			//Print that we are missing an argument
			System.err.printf(message);
			System.exit(1024);
		} else {
			//Throw an an unhandled exception
			throw new IllegalStateException(message);
		}
	}

}
