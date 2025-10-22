package seedu.address.logic.commands;

import seedu.address.model.Model;
import seedu.address.ui.HelpWindow;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";
    public static final String UNRECOGNIZED_COMMAND_MESSAGE = "Unrecognized command. Opened help window.";

    private final String commandName;
    private final boolean isUnrecognizedCommand;

    /**
     * Creates a HelpCommand that shows the general help window.
     * This constructor is used when no specific command is specified.
     */
    public HelpCommand() {
        this.commandName = null;
        this.isUnrecognizedCommand = false;
    }

    /**
     * Creates a HelpCommand that shows help for a specific command.
     *
     * @param commandName The name of the command to show help for.
     */
    public HelpCommand(String commandName) {
        this.commandName = commandName;
        this.isUnrecognizedCommand = false;
    }

    /**
     * Creates a HelpCommand with the specified command name and recognition status.
     * This constructor is used when handling unrecognized commands.
     *
     * @param commandName The name of the command (may be unrecognized).
     * @param isUnrecognizedCommand Whether the command was recognized or not.
     */
    public HelpCommand(String commandName, boolean isUnrecognizedCommand) {
        this.commandName = commandName;
        this.isUnrecognizedCommand = isUnrecognizedCommand;
    }

    @Override
    public CommandResult execute(Model model) {
        if (commandName == null || isUnrecognizedCommand) {
            String message = isUnrecognizedCommand ? UNRECOGNIZED_COMMAND_MESSAGE : SHOWING_HELP_MESSAGE;
            return new CommandResult(message, true, false);
        } else {
            HelpWindow.openCommandHelp(commandName);
            return new CommandResult("Showing help for " + commandName, false, false);
        }
    }
}
