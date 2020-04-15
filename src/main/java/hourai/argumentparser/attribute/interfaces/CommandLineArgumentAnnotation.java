/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */
package hourai.argumentparser.attribute.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A marker for a field to be consumed by an argument
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandLineArgumentAnnotation {

	/**
	 * The Name of the argument (command line)
	 *
	 * @return
	 */
	String name();

	/**
	 * The position of the argument if its positional, making an argument
	 * positional also creates an implicit parameter
	 *
	 * @return
	 */
	int position() default -1;

	/**
	 * If this is a required positional meaning that it must be provided in the
	 * command line
	 *
	 * @return
	 */
	boolean requiredPostitional() default false;

	/**
	 * The help text for this argument
	 *
	 * @return
	 */
	String helpText();
}
