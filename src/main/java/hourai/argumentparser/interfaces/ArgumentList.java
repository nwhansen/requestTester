/*
 * Copyright 2018 Nathan Hansen. ALL RIGHTS RESERVED
 */
package hourai.argumentparser.interfaces;

/**
 *
 */
public interface ArgumentList {

	/**
	 * @return The list of all arguments
	 */
	public Iterable<CommandLineArgument> getAvailableArguments();

	/**
	 * @return The list of Required positionals in order
	 */
	public Iterable<CommandLineArgument> getRequiredPositionalArguments();

	/**
	 * @return The list of positionals that are not required
	 */
	public Iterable<CommandLineArgument> getPositionalArguments();

}
