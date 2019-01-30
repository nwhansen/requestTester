/*
 * Copyright 2018 Nathan Hansen. ALL RIGHTS RESERVED
 */
package hourai.argumentparser.attribute;

import hourai.argumentparser.attribute.interfaces.CommandLineArgumentData;
import hourai.argumentparser.attribute.interfaces.FieldSetter;
import hourai.argumentparser.attribute.mapping.BooleanMapping;
import hourai.argumentparser.attribute.mapping.IntegerMapping;
import hourai.argumentparser.attribute.mapping.StringMapping;
import hourai.argumentparser.interfaces.ArgumentList;
import hourai.argumentparser.interfaces.CommandLineArgument;
import hourai.argumentparser.parser.ArgumentListParser;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * A service that consumes an attribute class and uses its information to
 * generate a argument object
 */
public class CommandLineAttributeParser {

	private final Map<CommandLineArgument, Field> argumentFieldMappings;
	private final ArgumentListParser parser;
	private final CommandLineArgumentData dataObject;
	private final Map<Class<?>, FieldSetter> setterTypeMapping;
	private final ArgumentList arguments;

	public CommandLineAttributeParser(CommandLineArgumentData data, ArgumentListParser parser, Map<CommandLineArgument, Field> argumentFieldMappings, ArgumentList arguments) {
		this.argumentFieldMappings = argumentFieldMappings;
		this.parser = parser;
		this.dataObject = data;
		setterTypeMapping = new HashMap<Class<?>, FieldSetter>();
		setterTypeMapping.put(Integer.class, new IntegerMapping());
		setterTypeMapping.put(int.class, new IntegerMapping());
		setterTypeMapping.put(String.class, new StringMapping());
		setterTypeMapping.put(Boolean.class, new BooleanMapping());
		setterTypeMapping.put(boolean.class, new BooleanMapping());
		this.arguments = arguments;
	}

	/**
	 * Parses the arguments populating the data object
	 *
	 * @param arguments The arguments to parse
	 */
	public void parseArguments(String[] arguments) {
		parser.parseArguments(arguments);
		for (CommandLineArgument argument : argumentFieldMappings.keySet()) {
			try {
				//Skip processing of the value (null or their default)
				if (!argument.getIsPresent()) {
					continue;
				}
				Field currentField = argumentFieldMappings.get(argument);
				//This normally doesn't happen since all arguments are required
				if (currentField != null) {
					FieldSetter setter = setterTypeMapping.get(currentField.getType());
					setter.setField(currentField, dataObject, argument.getParameters());
				}
			} catch (IllegalAccessException ex) {
				throw new IllegalStateException("Unable to convert type to correct type", ex);
			}
		}
	}

	public ArgumentList getArguments() {
		return arguments;
	}

}
