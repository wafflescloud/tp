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

    private final String commandName;

    public HelpCommand() {
        this.commandName = null;
    }

    public HelpCommand(String commandName) {
        this.commandName = commandName;
    }

    @Override
    public CommandResult execute(Model model) {
        if (commandName == null) {
            // Show general help window
            return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        } else {
            // Show specific command help window directly
            HelpWindow.openCommandHelp(commandName);
            return new CommandResult("Showing help for " + commandName, false, false);
        }
    }
}
