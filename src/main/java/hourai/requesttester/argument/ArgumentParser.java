/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given. 
 */
package hourai.requesttester.argument;

import java.security.InvalidParameterException;

/**
 * A class that attempts to parse a given argument against the argument list
 * @author nhansen
 */
public class ArgumentParser {
   
    /**
     * Tries to parse the argument at the given position as the given argument
     * @param argument The argument to attempt to parse the argument list for the position
     * @param arguments The argument list to parse
     * @param position The position to parse at
     * @return The index to continue parsing at
     */
    public static int tryParseArgument(Argument argument, String[] arguments, int position) {
        String argumentString = arguments[position];
        boolean ourArgument = false;
        if(argumentString.startsWith("-")) {
            //Begin parsing as if a short/long
            if(argumentString.startsWith("--")) {
                //Its a long command and we don't have one
                if(argument.getLongCommand() == null) {
                    return position;
                }
                ourArgument = argumentString.indexOf(argument.getLongCommand()) == 2;
                //Validate the length
                ourArgument &= argumentString.length() - 2 == argument.getLongCommand().length();
            } else if(argument.getShortCommand() != null) {
                ourArgument =  argumentString.indexOf(argument.getShortCommand()) == 1;
                ourArgument &= argumentString.length() - 1 == argument.getShortCommand().length();
            }
        } else {
            //If we are positional and it matches ours then yes. 
            ourArgument = position == argument.getPositional();
            //We cannot duplicate positional
            if(ourArgument) {
                argument.setValue(argumentString);
                argument.setIsPresent(true);
                return position+1;
            }
        }
        if(!ourArgument) {
            return position;
        }
        //Its our parameter
        argument.setIsPresent(true);
        //If we have no parameters we are a true/false
        if(argument.getParameterCount() == 0) {
            return position + 1;
        }
        //If we don't have enough parameters
        if(argument.getParameterCount() + position >= arguments.length) {
            throw new InvalidParameterException("Insufficient parameters for parameter: " + argument.getHelp().getCommandLineName());
        }
        //If there is only 1 put it in value
        //Copy the parameters
        String[] params = new String[argument.getParameterCount()];
        int copyAt = position + 1;
        for(int i = 0; i < argument.getParameterCount(); i++, copyAt++) {
            params[i] = arguments[copyAt];
        }
        argument.setParameters(params);
        argument.setValue(params[0]);
        //We go to the next + all of our parameters
        return position + 1 + argument.getParameterCount();
    }
    
}


