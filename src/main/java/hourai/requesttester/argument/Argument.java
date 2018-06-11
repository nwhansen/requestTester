/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given. 
 */
package hourai.requesttester.argument;

import java.security.InvalidParameterException;

/**
 * Represents an argument for the command line
 *
 * @author nhansen
 */
public class Argument {

    private boolean present;
    private boolean positionalRequred;
    private int positional;
    private String value;
    private String shortCommand;
    private String longCommand;
    private int parameterCount;
    private String[] parameters;
    private ArgumentHelp help;

    public Argument() {
        positional = -1;
        parameters = new String[0];
    }

    /**
     * Returns if the parameter was present in the argument list
     *
     * @return
     */
    public boolean getIsPresent() {
        return present;
    }

    public void setIsPresent(boolean present) {
        this.present = present;
    }

    /**
     * @return the position the argument could be present in
     */
    public int getPositional() {
        return positional;
    }

    /**
     * @param positional the position that the argument can be in
     */
    public void setPositional(int positional) {
        if (parameterCount > 1) {
            throw new InvalidParameterException("You cannot set positional and parameter count at the same time");
        }
        this.positional = positional;
    }

    /**
     * @return the value of the parameter, or the first in the parameter list
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value of the parameter
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the command that is prefixed by a '-' usually 1 character long
     */
    public String getShortCommand() {
        return shortCommand;
    }

    /**
     * @param shortCommand the command for this argument usually 1 character
     * long do not include '-'
     */
    public void setShortCommand(String shortCommand) {
        this.shortCommand = shortCommand;
    }

    /**
     * @return the command that is usually prefixed by '--'
     */
    public String getLongCommand() {
        return longCommand;
    }

    /**
     * @param longCommand the command that is longer that 1 character, usually
     * prefixed by '--'. Do not include '--'
     */
    public void setLongCommand(String longCommand) {
        this.longCommand = longCommand;
    }

    /**
     * @return the number of parameters for the argument
     */
    public int getParameterCount() {
        return parameterCount;
    }

    /**
     * The Number of parameters, if this is set you cannot also set positional
     * since those are logically exclusive.
     *
     * @param parameterCount the number of parameters.
     */
    public void setParameterCount(int parameterCount) {
        if (positional > 0) {
            throw new InvalidParameterException("You cannot set positional and parameter count at the same time");
        }
        this.parameterCount = parameterCount;
    }

    /**
     * @return the parameter values passed in the command line
     */
    public String[] getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters passed in via the command line to this
     * argument
     */
    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    /**
     * @return If this argument is required to be a in the given position
     */
    public boolean getIsPositionalRequred() {
        return positionalRequred;
    }

    /**
     * @param positionalRequred if the positional must be met (IE we have to
     * have a value at the position 0)
     */
    public void setPositionalRequred(boolean positionalRequred) {
        this.positionalRequred = positionalRequred;
    }

    /**
     * @return the help object for help message
     */
    public ArgumentHelp getHelp() {
        return help;
    }

    /**
     * @param help the help object to set
     */
    public void setHelp(ArgumentHelp help) {
        this.help = help;
    }

    @Override
    public String toString() {
        String name = getHelp() != null ? getHelp().getCommandLineName() : "unkown";
        return String.format("IsPresent: %s Name: %s Value: %s", getIsPresent(), name, getValue());
    }
}
