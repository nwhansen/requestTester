/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */

package hourai.argumentparser.builder;

import hourai.argumentparser.interfaces.ArgumentBuilder;
import hourai.argumentparser.interfaces.CommandLineArgument;
import hourai.argumentparser.model.Argument;
import hourai.argumentparser.model.ArgumentHelp;
import java.util.ArrayList;
import java.util.List;

/**
 * A builder for an argument list
 */
class ArgumentBuilderImpl implements ArgumentBuilder {
	/**
	 * A wrapper to hold arguments
	 */
	private class ArgumentParameter {
		public final String name;
		public final String help;

		public ArgumentParameter(String name, String help) {
			this.name = name;
			this.help = help;
		}

	}

	private final ArgumentListBuilder owningList;
	private final List<ArgumentParameter> parameters;
	private Argument result;
	private ArgumentHelp help;

	public ArgumentBuilderImpl(ArgumentListBuilder owningList, String argumentName, int position, boolean isRequired) {
		this.owningList = owningList;
		result = new Argument();
		result.setPositionalRequred(isRequired && position >= 0);
		result.setPositional(position);
		help = new ArgumentHelp();
		help.setCommandLineName(argumentName);
		parameters = new ArrayList<ArgumentParameter>();
	}

	@Override
	public CommandLineArgument create() {
		//Build variable parts of argument
		if (parameters.size() > 0) {
			result.setParameterCount(parameters.size());
			String[] paramArray = new String[parameters.size() * 2];
			for (int arrayIndex = 0, parameter = 0; arrayIndex < paramArray.length; arrayIndex += 2, parameter++) {
				ArgumentParameter param = parameters.get(parameter);
				paramArray[arrayIndex] = param.name;
				paramArray[arrayIndex + 1] = param.help;
			}
			help.setArgumentParametersHelpText(paramArray);
		}
		result.setHelp(help);
		owningList.addArgumentToList(result);
		//Save the result and then clear it. our calls should die at this point
		Argument finalResult = result;
		help = null;
		result = null;
		return finalResult;
	}

	@Override
	public ArgumentBuilder setLongCommand(String longCommand) {
		if (result.getIsPositionalRequred()) {
			return this;
		}
		result.setLongCommand(longCommand);
		return this;
	}

	@Override
	public ArgumentBuilder setHelpText(String helpText) {
		help.setArgumentHelpText(helpText);
		return this;
	}

	@Override
	public ArgumentBuilder appendParameter(String name, String helpText) {
		parameters.add(new ArgumentParameter(name, helpText));
		return this;
	}
	@Override
	public ArgumentBuilder setShortCommand(char command) {
		if (result.getIsPositionalRequred()) {
			return this;
		}
		//We use strings because that is how the framework is designed
		setShortCommand(new String(new char[]{command}));
		return this;
	}

	@Override
	public ArgumentBuilder setShortCommand(String command) {
		if (command == null || command.length() == 0) {
			result.setShortCommand(null);
		} else {
			if (command.length() > 1) {
				//Only take the first
				command = command.substring(0, 1);
			}
			result.setShortCommand(command);
		}
		return this;
	}
}
