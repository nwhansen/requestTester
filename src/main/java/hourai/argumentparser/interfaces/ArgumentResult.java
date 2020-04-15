/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */
package hourai.argumentparser.interfaces;

/**
 * Represents the result of the argument
 */
public interface ArgumentResult {

	/**
	 * @return Returns if the parameter was present in the argument list
	 */
	boolean getIsPresent();

	/**
	 * @return the parameter values passed in the command line
	 */
	String[] getParameters();

	/**
	 * @return the value of the parameter, or the first in the parameter list
	 */
	String getValue();

}
