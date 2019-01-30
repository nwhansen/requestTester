/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given. 
 */
package hourai.argumentparser.model;

import java.security.InvalidParameterException;

/**
 * A help message for an Argument. Provides a location to store the help content
 * @author nhansen
 */
public class ArgumentHelp {

    private String commandLineName;
    private String argumentHelpText;
    private String[] argumentParametersHelpText;

    public ArgumentHelp() {
        argumentParametersHelpText = new String[0];
    }

    /**
     * @return the argumentHelpText
     */
    public String getArgumentHelpText() {
        return argumentHelpText;
    }

    /**
     * @param argumentHelpText the argumentHelpText to set
     */
    public void setArgumentHelpText(String argumentHelpText) {
        this.argumentHelpText = argumentHelpText;
    }

    /**
     *
     * @return the argumentParametersHelpText
     */
    public String[] getArgumentParametersHelpText() {
        return argumentParametersHelpText;
    }

    /**
     * @param argumentParametersHelpText the argumentParametersHelpText to set
     */
    public void setArgumentParametersHelpText(String[] argumentParametersHelpText) {
        if (argumentParametersHelpText == null) {
            throw new InvalidParameterException("argumentParametersHelpText cannot be null");
        }
        if (argumentParametersHelpText.length % 2 != 0) {
			throw new InvalidParameterException("The argument list must be in pairs i%2=0");
        }
        this.argumentParametersHelpText = argumentParametersHelpText;
    }

    /**
     * @return the commandLineName
     */
    public String getCommandLineName() {
        return commandLineName;
    }

    /**
     * @param commandLineName the commandLineName to set
     */
    public void setCommandLineName(String commandLineName) {
        this.commandLineName = commandLineName;
    }
}


