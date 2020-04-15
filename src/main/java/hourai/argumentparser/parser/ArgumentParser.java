/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */
package hourai.argumentparser.parser;

import hourai.argumentparser.model.Argument;
import java.security.InvalidParameterException;

/**
 * Performs the parsing logic for a given argument
 */
public class ArgumentParser {

	/**
	 * Parses an argument if it was a flagged argument
	 *
	 * @param argument The argument that was identified
	 * @param arguments The argument list
	 * @param position The current position of the flagged argument
	 * @return The number of indices we processed
	 */
	public int parseFlaggedArgument(Argument argument, String[] arguments, int position) {
		argument.setIsPresent(true);
		//This handles if we are a flag vs an implicit parameter
		if (argument.getParameterCount() == 0) {
			//If we have a positional that means we had an implicit argument
			if (argument.getPositional() != -1) {
				//If the next position is outside our bounds fail to process
				if (arguments.length == position + 1) {
					return 0;
				}
				//Then we are taking next argument
				argument.setParameters(new String[]{arguments[position + 1]});
				argument.setValue(argument.getParameters()[0]);
				//Take the next argument
				return 2;
			}
			return 1;

		}
		//If we don't have enough parameters
		if (argument.getParameterCount() + position >= arguments.length) {
			throw new InvalidParameterException("Insufficient parameters for parameter: " + argument.getHelp().getCommandLineName());
		}
		//If there is only 1 put it in value
		//Copy the parameters
		String[] params = new String[argument.getParameterCount()];
		int copyAt = position + 1;
		for (int i = 0; i < argument.getParameterCount(); i++, copyAt++) {
			params[i] = arguments[copyAt];
		}
		argument.setParameters(params);
		argument.setValue(params[0]);
		//We have processed our index and the next parameter indexes

		return argument.getParameterCount() + 1;
	}

	public int parsePositional(Argument argument, String[] arguments, int position) {
		argument.setIsPresent(true);
		argument.setParameters(new String[]{arguments[position]});
		argument.setValue(argument.getParameters()[0]);
		return 1;
	}

}
