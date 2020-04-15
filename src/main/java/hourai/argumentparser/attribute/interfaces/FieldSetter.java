/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */
package hourai.argumentparser.attribute.interfaces;

import java.lang.reflect.Field;

/**
 * An interface that sets field types
 */
public interface FieldSetter {

	/**
	 * Sets the field with the given values
	 *
	 * @param field The field to set
	 * @param values The values to set
	 * @param data The data object to set
	 * @throws java.lang.IllegalAccessException In case java hates us
	 */
	public void setField(Field field, CommandLineArgumentData data, String[] values) throws IllegalAccessException;

}
