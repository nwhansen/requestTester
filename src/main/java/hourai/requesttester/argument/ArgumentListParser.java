/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given. 
 */
package hourai.requesttester.argument;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import hourai.requesttester.exception.AggregateException;

/**
 *
 * @author nhansen
 */
public class ArgumentListParser {

    List<Argument> availableArguments;

    public ArgumentListParser(List<Argument> arguments) throws AggregateException {
        availableArguments = arguments;
        //Validate it with a bit of hash magic
        HashSet<String> argumentSet = new HashSet<String>();
        validateArguments(arguments, argumentSet);
    }

    private static void validateArguments(List<Argument> arguments, HashSet<String> argumentSet) throws AggregateException {
        List<Throwable> failures = new ArrayList<Throwable>();
        for (Argument argument : arguments) {
            if (argument.getShortCommand() != null) {
                String command = "-" + argument.getShortCommand();
                if (argumentSet.contains(command)) {
                    failures.add(new IllegalArgumentException(String.format("The command \"%s\" duplicates short command \"%s\"",
                            argument.getHelp().getCommandLineName(), command)));
                } else {
                    argumentSet.add(command);
                }
            }
            if (argument.getLongCommand() != null) {
                String command = "--" + argument.getLongCommand();
                if (argumentSet.contains(command)) {
                    failures.add(new IllegalArgumentException(String.format("The command \"%s\" duplicates long command \"%s\"",
                            argument.getHelp().getCommandLineName(), command)));
                } else {
                    argumentSet.add(command);
                }
            }
        }
        if(failures.size() > 0) {
            throw new AggregateException("Arguments violate unique command names", failures);
        }
    }

    public void parseArguments(String[] arguments) {
        if (arguments.length == 0) {
            //Check for required.
            Argument firstMissingArgument = null;
            for (Argument argument : availableArguments) {
                if (argument.getIsPositionalRequred()) {
                    if (firstMissingArgument == null || argument.getPositional() < firstMissingArgument.getPositional()) {
                        firstMissingArgument = argument;
                    }
                }
            }
            if (firstMissingArgument != null) {
                System.err.printf("Missing required argument: %s", firstMissingArgument.getHelp().getCommandLineName());
                System.exit(1024);
            }
            //No processing required
            return;
        }
        //We wish we have the stream java 6 is the required compatability version
        int parsingPosition;
        int nextPosition = 0;
        do {
            parsingPosition = nextPosition;
            for (Argument argument : availableArguments) {
                nextPosition = ArgumentParser.tryParseArgument(argument, arguments, parsingPosition);
                //If we ever parsed we need to bail to advance
                if (nextPosition != parsingPosition) {
                    break;
                }
            }
        } while (parsingPosition != nextPosition && nextPosition < arguments.length);
        if (nextPosition != arguments.length) {
            //We failed to parse all the arguments
            System.err.println("There is additional command line arguments that were not processed, did you miss-type something?");
            System.exit(1024);
        }
    }

    /**
     * Generates the help message
     *
     * @param headerLine The line to use before the usage
     * @return The string for the help message
     */
    public String writeHelp(String headerLine) {
        //Go java-6 compatability
        String newLine = System.getProperty("line.separator");
        char tab = '\t';
        StringBuilder builderString = new StringBuilder(headerLine);
        if (headerLine != null && headerLine.length() > 0) {
            builderString.append(newLine);
        }
        //Now our argument list is composed of the arguments with optional positional information placed
        builderString.append(tab).append("Usage: java -jar <jarName>");
        int positionRemaining = addRequiredParameters(builderString);
        addOptionalParameters(builderString, positionRemaining);
        builderString.append(" [options]").append(newLine);
        for (Argument argument : availableArguments) {
            printArgumentLine(builderString, tab, argument, newLine);

        }
        return builderString.toString();
    }

    private void printArgumentLine(StringBuilder builderString, char tab, Argument argument, String newLine) {
        builderString.append(tab).append(tab);
        ArgumentHelp help = argument.getHelp();
        builderString.append(help.getCommandLineName());
        boolean hasTabbed = false;
        if (argument.getLongCommand() != null) {
            builderString.append(tab).append(" --").append(argument.getLongCommand());
            hasTabbed = true;
        }
        if (argument.getShortCommand() != null) {
            if (!hasTabbed) {
                builderString.append(tab);
            }
            builderString.append(" -").append(argument.getShortCommand());
        }
        builderString.append(tab).append(help.getArgumentHelpText()).append(newLine);
        if (help.getArgumentParametersHelpText().length > 0) {
            //we have a parameter list
            builderString.append(tab).append(tab).append(tab).append("Argument Parameters").append(newLine);
            String[] argParams = help.getArgumentParametersHelpText();
            for (int i = 0; i < argParams.length; i = i + 2) {
                builderString.append(tab).append(tab).append(tab)
                        .append(argParams[i]).append(tab).append(argParams[i + 1]).append(newLine);
            }
        }
    }

    private int addRequiredParameters(StringBuilder builderString) {
        //find the first positional it may or may not be required
        boolean requiredPositionalFound;
        int currentRequiredPositional = 0;
        do {
            requiredPositionalFound = false;
            for (Argument possible : availableArguments) {
                if (possible.getIsPositionalRequred() && possible.getPositional() == currentRequiredPositional) {
                    builderString.append(" ").append(possible.getHelp().getCommandLineName());
                    requiredPositionalFound = true;
                    currentRequiredPositional++;
                    break;
                }
            }
        } while (requiredPositionalFound);
        return currentRequiredPositional;
    }

    private void addOptionalParameters(StringBuilder builderString, int currentOptionalPositional) {
        //find the first positional it may or may not be required
        boolean optionalPositionalFound;
        do {
            optionalPositionalFound = false;
            for (Argument possible : availableArguments) {
                if (possible.getIsPositionalRequred() && possible.getPositional() == currentOptionalPositional) {
                    builderString.append(" [").append(possible.getHelp().getCommandLineName()).append("]");
                    optionalPositionalFound = true;
                    currentOptionalPositional++;
                    break;
                }
            }
        } while (optionalPositionalFound);
    }

}




