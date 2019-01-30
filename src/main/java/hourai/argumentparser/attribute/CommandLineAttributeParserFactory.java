/*
 * Copyright 2018 Nathan Hansen. ALL RIGHTS RESERVED
 */

package hourai.argumentparser.attribute;

import hourai.argumentparser.attribute.interfaces.CommandLineArgumentAnnotation;
import hourai.argumentparser.attribute.interfaces.CommandLineArgumentData;
import hourai.argumentparser.attribute.interfaces.CommandLineArgumentLongCommand;
import hourai.argumentparser.attribute.interfaces.CommandLineArgumentParameter;
import hourai.argumentparser.attribute.interfaces.CommandLineArgumentParameters;
import hourai.argumentparser.attribute.interfaces.CommandLineArgumentShortCommand;
import hourai.argumentparser.attribute.interfaces.HelpArgument;
import hourai.argumentparser.builder.ArgumentListBuilder;
import hourai.argumentparser.exception.AggregateException;
import hourai.argumentparser.interfaces.ArgumentBuilder;
import hourai.argumentparser.interfaces.CommandLineArgument;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A service that creates a command line attribute factory
 */
public class CommandLineAttributeParserFactory {

	private static class CommandLineSorter implements Comparator<Field> {

		@Override
		public int compare(Field o1, Field o2) {
			CommandLineArgumentAnnotation argument1 = o1.getAnnotation(CommandLineArgumentAnnotation.class);
			CommandLineArgumentAnnotation argument2 = o2.getAnnotation(CommandLineArgumentAnnotation.class);
			return argument1.position() - argument2.position();
		}

	}

	public static CommandLineAttributeParser createCommandLineArributeParser(CommandLineArgumentData data) throws AggregateException {
		ArgumentListBuilder builder = new ArgumentListBuilder();
		//Sort the fields. i guess?
		Map<CommandLineArgument, Field> argumentFieldMappings = new HashMap<CommandLineArgument, Field>();
		List<Field> positionalArguments = new ArrayList<Field>();
		for (Field field : data.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(CommandLineArgumentAnnotation.class)) {
				//Check for help
				if (field.isAnnotationPresent(HelpArgument.class)) {
					HelpArgument helpArgument = field.getAnnotation(HelpArgument.class);
					//If we are marked as a template help then create as such and continue onward
					if (helpArgument.templateHelp()) {
						argumentFieldMappings.put(builder.createTemplateHelpMessage(), field);
						continue;
					}
				}
				//We have an argument
				CommandLineArgumentAnnotation argument = field.getAnnotation(CommandLineArgumentAnnotation.class);
				if (argument.position() != -1) {
					positionalArguments.add(field);
					continue;
				}
				//Trust them not to be mean
				CommandLineArgument arg = buildFlaggedArgument(field, argument, builder);
				argumentFieldMappings.put(arg, field);
				if (field.isAnnotationPresent(HelpArgument.class)) {
					//If we have a help argument then we are not a template (otherwise we would have continued
					builder.setHelpArgument(arg);
				}
			}
		}
		positionalArguments.sort(new CommandLineSorter());
		//We think their positions are cute... but yeah we are on own own boat
		for (Field positional : positionalArguments) {
			argumentFieldMappings.put(buildPositionalArgument(positional, builder), positional);
		}
		return new CommandLineAttributeParser(data, builder.createParser(), argumentFieldMappings, builder.createArgumentList());
	}

	private static CommandLineArgument buildFlaggedArgument(Field field, CommandLineArgumentAnnotation argument, ArgumentListBuilder listBuilder) {
		ArgumentBuilder builder = listBuilder.addArgument(argument.name());
		builder.setHelpText(argument.helpText());
		boolean hasCommand = false;
		if (field.isAnnotationPresent(CommandLineArgumentLongCommand.class)) {
			builder.setLongCommand(field.getAnnotation(CommandLineArgumentLongCommand.class).value());
			hasCommand = true;
		}
		if (field.isAnnotationPresent(CommandLineArgumentShortCommand.class)) {
			builder.setShortCommand(field.getAnnotation(CommandLineArgumentShortCommand.class).value());
			hasCommand = true;
		}
		//We are not as nice as the other one. just fail now
		if (!hasCommand) {
			throw new RuntimeException("A flagged argument must have a short and/or long command: " + argument.name());
		}
		appendParameters(field, builder);
		return builder.create();
	}

	private static void appendParameters(Field field, ArgumentBuilder builder) {
		//CommandLineArgumentParameters
		if (field.isAnnotationPresent(CommandLineArgumentParameter.class)) {
			//We have 1 parameter
			CommandLineArgumentParameter parameter = field.getAnnotation(CommandLineArgumentParameter.class);
			builder.appendParameter(parameter.name(), parameter.helpText());
		} else if (field.isAnnotationPresent(CommandLineArgumentParameters.class)) {
			//We have many
			String[] parameters = field.getAnnotation(CommandLineArgumentParameters.class).value();
			for (int i = 0; i < parameters.length; i += 2) {
				builder.appendParameter(parameters[i], parameters[i + 1]);
			}
		}
	}

	private static CommandLineArgument buildPositionalArgument(Field field, ArgumentListBuilder listBuilder) {
		CommandLineArgumentAnnotation argument = field.getAnnotation(CommandLineArgumentAnnotation.class);
		ArgumentBuilder builder = listBuilder.addPositionalArgument(argument.name(), argument.requiredPostitional());
		builder.setHelpText(argument.helpText());
		if (field.isAnnotationPresent(CommandLineArgumentLongCommand.class)) {
			//A positional cannot have a long (if required) but it will detonate on create so we don't worry
			builder.setLongCommand(field.getAnnotation(CommandLineArgumentLongCommand.class).value());
		}
		if (field.isAnnotationPresent(CommandLineArgumentShortCommand.class)) {
			builder.setShortCommand(field.getAnnotation(CommandLineArgumentShortCommand.class).value());
		}
		appendParameters(field, builder);
		return builder.create();
	}


}
