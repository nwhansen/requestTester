/*
 * Copyright 2018 Nathan Hansen. ALL RIGHTS RESERVED
 */

package hourai.argumentparser.attribute.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A collection of command line argument parameters
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandLineArgumentParameters {

	/**
	 * The parameter help text and values in an array must be of mod 2 in length
	 *
	 * @return
	 */
	String[] value();
}
