/*
 * Copyright 2018 Nathan Hansen. ALL RIGHTS RESERVED
 */

package hourai.argumentparser.attribute.mapping;

import hourai.argumentparser.attribute.interfaces.CommandLineArgumentData;
import hourai.argumentparser.attribute.interfaces.FieldSetter;
import hourai.argumentparser.interfaces.CommandLineArgument;
import java.lang.reflect.Field;

/**
 * A class that maps a value array to a string
 */
public class StringMapping implements FieldSetter {

	@Override
	public void setField(Field field, CommandLineArgumentData data, String[] values) throws IllegalAccessException {
		field.setAccessible(true);
		if (field.getType().isArray()) {
			field.set(data, values);
		} else if (values.length > 0) {
			field.set(data, values[0]);
		}
	}

}
