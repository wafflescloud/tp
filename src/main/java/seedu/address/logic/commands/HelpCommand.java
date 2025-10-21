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

    public HelpCommand() {
        this.commandName = null;
        this.isUnrecognizedCommand = false;
    }

    public HelpCommand(String commandName) {
        this.commandName = commandName;
        this.isUnrecognizedCommand = false;
    }

    public HelpCommand(String commandName, boolean isUnrecognizedCommand) {
        this.commandName = commandName;
        this.isUnrecognizedCommand = isUnrecognizedCommand;
    }

    @Override
    public CommandResult execute(Model model) {
        if (commandName == null || isUnrecognizedCommand) {
            // Show general help window for null command or unrecognized command
            String message = isUnrecognizedCommand ? UNRECOGNIZED_COMMAND_MESSAGE : SHOWING_HELP_MESSAGE;
            return new CommandResult(message, true, false);
        } else {
            // Show specific command help window directly
            HelpWindow.openCommandHelp(commandName);
            return new CommandResult("Showing help for " + commandName, false, false);
        }
    }
}
