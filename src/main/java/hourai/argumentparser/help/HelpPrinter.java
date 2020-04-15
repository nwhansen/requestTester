/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */
package hourai.argumentparser.help;

import hourai.argumentparser.StringManipulation;
import hourai.argumentparser.interfaces.ArgumentList;
import hourai.argumentparser.interfaces.CommandLineArgument;

import static hourai.argumentparser.StringManipulation.blockString;
import static hourai.argumentparser.StringManipulation.fixWidth;

/**
 * Generates a help message formatted consistently
 */
public class HelpPrinter {

	private final String spaceTab = "   ";
	private final String newLine;
	private final int minSpace = 2;
	private final int terminalWidth;

	/**
	 * Creates a help printer that prints the arguments to the fixed sizes (adds
	 * some padding)
	 *
	 * @param terminalWidth How large do you want the terminal to be
	 */
	public HelpPrinter(int terminalWidth) {
		this.terminalWidth = terminalWidth;
		newLine = System.getProperty("line.separator");
	}

	public String generateHelp(String description, ArgumentList validator) {
		int maxArgumentSize = -1, maxLongNameSize = -1;
		for (CommandLineArgument argument : validator.getAvailableArguments()) {
			maxArgumentSize = Math.max(maxArgumentSize, StringManipulation.getLength(argument.getHelp().getCommandLineName()));
			maxLongNameSize = Math.max(maxLongNameSize, StringManipulation.getLength(argument.getLongCommand()));

		}
		StringBuilder builderString = new StringBuilder();
		if (StringManipulation.getLength(description) != 0) {
			blockStringToBuilder(builderString, description);
		}

		buildUsage(builderString, validator);
		for (CommandLineArgument argument : validator.getAvailableArguments()) {
			generateArgumentHelp(argument, builderString, maxArgumentSize, maxLongNameSize);
		}
		return builderString.toString();
	}

	private void buildUsage(StringBuilder builderString, ArgumentList validator) {
		StringBuilder argumentUsage = new StringBuilder("Usage: java -jar <jarName>");
		addSpaces(argumentUsage, 1);
		for (CommandLineArgument requiredPositional : validator.getRequiredPositionalArguments()) {
			argumentUsage.append(requiredPositional.getHelp().getCommandLineName());
			addSpaces(argumentUsage, 1);
		}
		for (CommandLineArgument positional : validator.getPositionalArguments()) {
			argumentUsage.append("[").append(positional.getHelp().getCommandLineName()).append("]");
			addSpaces(argumentUsage, 1);
		}
		argumentUsage.append("[options]");
		int padding = spaceTab.length() / 2;
		String[] usageParts = blockString(argumentUsage.toString(), terminalWidth - padding);
		for (String line : usageParts) {
			addSpaces(builderString, padding);
			builderString.append(line).append(newLine);
		}
	}

	private void blockStringToBuilder(StringBuilder builder, String text) {
		String[] blocked = blockString(text, terminalWidth);
		for (int i = 0; i < blocked.length; i++) {
			builder.append(blocked[i]).append(newLine);
		}
	}

	/**
	 * Generates the help line for a singular argument
	 *
	 * @param argument
	 * @param toAppend
	 */
	private void generateArgumentHelp(CommandLineArgument argument, StringBuilder toAppend, int maxArgumentSize, int maxLongNameSize) {
		//Start with space
		StringBuilder currentArgument = new StringBuilder();
		String minSpacing = fixWidth(null, minSpace);
		currentArgument.append(spaceTab);
		currentArgument.append(fixWidth(argument.getHelp().getCommandLineName(), maxArgumentSize));
		currentArgument.append(minSpacing);
		if (argument.getLongCommand() != null && !argument.getLongCommand().isEmpty()) {
			currentArgument.append("--");
		} else {
			currentArgument.append("  ");
		}
		//Print the long name
		currentArgument.append(fixWidth(argument.getLongCommand(), maxLongNameSize));
		currentArgument.append(minSpacing);
		if (argument.getShortCommand() != null && !argument.getShortCommand().isEmpty()) {
			currentArgument.append("-");
		} else {
			currentArgument.append(" ");
		}
		//You only get 2 characters for short command, anything more than 1 is ignored anyways
		currentArgument.append(fixWidth(argument.getShortCommand(), minSpace));
		currentArgument.append(minSpacing);
		// Print to the end of line
		int descriptionStart = currentArgument.length();
		if (terminalWidth - currentArgument.length() < 0) {
			//We are going to have to just underflow
			printCompactDescription(argument, currentArgument);
		} else {
			printDescription(argument, currentArgument, descriptionStart);
		}
		//Process params. They must be at the same position as the description + tab

		if (argument.getParameterCount() > 0) {
			printArgumentParamters(argument, currentArgument, descriptionStart);
		}
		toAppend.append(currentArgument);
	}

	private void printArgumentParamters(CommandLineArgument argument, StringBuilder builder, int descriptionStart) {
		String[] parameters = argument.getHelp().getArgumentParametersHelpText();
		//Because we are strange
		addSpaces(builder, descriptionStart);
		builder.append("Argument Parameters").append(newLine);
		for (int i = 0; i < parameters.length; i += 2) {
			addSpaces(builder, descriptionStart);
			builder.append(spaceTab).append(parameters[i]);
			addSpaces(builder, minSpace);
			builder.append(parameters[i + 1]);
			builder.append(newLine);
		}

	}

	private void printDescription(CommandLineArgument argument, StringBuilder builder, int descriptionStart) {
		//We are already at description start
		int canPrint = terminalWidth - descriptionStart;
		String[] blocked = blockString(argument.getHelp().getArgumentHelpText(), canPrint);
		for (int i = 0; i < blocked.length; i++) {
			if (i != 0) {
				addSpaces(builder, descriptionStart);
			}
			builder.append(blocked[i]).append(newLine);
		}
		if (blocked.length == 0) {
			builder.append(newLine);
		}
	}

	private static void addSpaces(StringBuilder builder, int total) {
		for (int i = 0; i < total; i++) {
			builder.append(" ");
		}
	}

	private void printCompactDescription(CommandLineArgument argument, StringBuilder builder) {
		//Print the
		builder.append(newLine);
		int spaceLength = spaceTab.length() * 3;
		String[] blocked = blockString(argument.getHelp().getArgumentHelpText(), terminalWidth - spaceLength);
		for (int i = 0; i < blocked.length; i++) {
			if (i != 0) {
				addSpaces(builder, spaceLength);
			}
			builder.append(blocked[i]).append(newLine);
		}

	}


}
