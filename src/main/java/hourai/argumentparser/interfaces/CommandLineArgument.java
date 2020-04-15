/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */
package hourai.argumentparser.interfaces;

import hourai.argumentparser.model.ArgumentHelp;

/**
 * Represents the command line argument, this also presents an argument result
 */
public interface CommandLineArgument extends ArgumentResult {

	/**
	 * @return the help object for help message
	 */
	ArgumentHelp getHelp();

	/**
	 * @return If this argument is required to be a in the given position
	 */
	boolean getIsPositionalRequred();

	/**
	 * @return the command that is usually prefixed by '--'
	 */
	String getLongCommand();

	/**
	 * @return the number of parameters for the argument
	 */
	int getParameterCount();

	/**
	 * @return the position the argument could be present in
	 */
	int getPositional();

	/**
	 * @return the command that is prefixed by a '-' usually 1 character long
	 */
	String getShortCommand();

}
