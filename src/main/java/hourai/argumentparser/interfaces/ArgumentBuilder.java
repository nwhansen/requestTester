/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */
package hourai.argumentparser.interfaces;

/**
 * Defines a builder for an argument
 */
public interface ArgumentBuilder {

	/**
	 * Sets the long command for this argument
	 *
	 * @param command The argument's long command
	 * @return The builder to chain calls
	 */
	ArgumentBuilder setLongCommand(String command);

	/**
	 * Sets the main help text for the argument
	 *
	 * @param text The help text to create
	 * @return The builder to chain calls
	 */
	ArgumentBuilder setHelpText(String text);

	/**
	 * Sets the short command
	 *
	 * @param command The command to set
	 * @return The builder to chain call
	 */
	ArgumentBuilder setShortCommand(char command);

	/**
	 * Sets the short command, only uses the first character
	 *
	 * @param command the single character to set
	 * @return The builder to chain calls
	 */
	ArgumentBuilder setShortCommand(String command);

	/**
	 * Appends a parameter to this argument
	 *
	 * @param name The name of the parameter
	 * @param helpText The help text for this parameter
	 * @return The Argument builder to chain calls
	 */
	ArgumentBuilder appendParameter(String name, String helpText);

	/**
	 * Returns the resulting argument
	 *
	 * @return The argument representing
	 */
	CommandLineArgument create();
}
