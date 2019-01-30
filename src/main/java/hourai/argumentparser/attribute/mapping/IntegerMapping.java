/*
 * Copyright 2018 Nathan Hansen. ALL RIGHTS RESERVED
 */

package hourai.argumentparser.attribute.mapping;

import hourai.argumentparser.attribute.interfaces.CommandLineArgumentData;
import hourai.argumentparser.attribute.interfaces.FieldSetter;
import hourai.argumentparser.interfaces.CommandLineArgument;
import java.lang.reflect.Field;

/**
 * Provides a service that accepts a type and maps it to a value
 */
public class IntegerMapping implements FieldSetter {

	@Override
	public void setField(Field field, CommandLineArgumentData data, String[] values) throws IllegalAccessException {
		field.setAccessible(true);
		int[] intArr = toIntegerArray(values);
		if (field.getType().isArray()) {
			field.set(data, intArr);
		} else if (values.length > 0) {
			field.set(data, intArr[0]);
		}
	}

	private int[] toIntegerArray(String[] value) {
		if (value.length == 0) {
			return new int[0];
		}
		int[] dest = new int[value.length];
		for (int i = 0; i < value.length; i++) {
			dest[i] = Integer.parseInt(value[i]);
		}
		return dest;
	}

}
