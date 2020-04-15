/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */

package hourai.argumentparser.attribute.mapping;

import hourai.argumentparser.attribute.interfaces.CommandLineArgumentData;
import hourai.argumentparser.attribute.interfaces.FieldSetter;
import hourai.argumentparser.interfaces.CommandLineArgument;
import java.lang.reflect.Field;

/**
 * Represents a value for mapping booleans
 */
public class BooleanMapping implements FieldSetter {

	@Override
	public void setField(Field field, CommandLineArgumentData data, String[] values) throws IllegalAccessException {
		//This is far simpler?
		field.setAccessible(true);
		//If we are even called and have no values to associate then set to "true" as in present
		Class<?> type = field.getType();
		if (values.length == 0 && !type.isArray()) {
			field.setBoolean(data, true);
		} else if (values.length == 0 && type.isArray()) {
			//Set the first value? I don't really know what they wanted.. we shouldn't be here normally
			field.set(data, new boolean[]{true});
		} else {
			//Treat this like a normal data type
			boolean[] boolArr = toBooleanArray(values);
			if (type.isArray()) {
				field.set(data, boolArr);
			} else {
				field.setBoolean(type, boolArr[0]);
			}
		}
	}

	private boolean[] toBooleanArray(String[] value) {
		if (value.length == 0) {
			return new boolean[0];
		}
		boolean[] dest = new boolean[value.length];
		for (int i = 0; i < value.length; i++) {
			dest[i] = Boolean.parseBoolean(value[i]);
		}
		return dest;
	}

}
