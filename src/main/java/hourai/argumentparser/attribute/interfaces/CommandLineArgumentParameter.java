/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */
package hourai.argumentparser.attribute.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes a parameter for a command line argument
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandLineArgumentParameter {

	/**
	 * The name of the argument
	 *
	 * @return
	 */
	String name();

	/**
	 * The help text for this parameter
	 *
	 * @return
	 */
	String helpText();
}
