/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */
package hourai.argumentparser.attribute.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The long command for an command line argument
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandLineArgumentLongCommand {

	/**
	 * The value for the command line argument
	 *
	 * @return
	 */
	String value();

}
