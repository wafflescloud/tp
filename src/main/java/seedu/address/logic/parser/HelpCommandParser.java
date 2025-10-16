package seedu.address.logic.parser;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.ui.HelpWindow;

/**
 * Parses input arguments and creates a new {@code HelpCommand} object.
 * Accepts either no arguments (to show the general help window) or a single valid command name
 * (to show help for a specific command). Throws a {@code ParseException} if the command name is invalid.
 */
public class HelpCommandParser implements Parser<HelpCommand> {

    public static final String MESSAGE_INVALID_COMMAND = "The command '%s' does not exist. "
            + "Use 'help' to see all available commands.";

    /**
     * Parses the given {@code String} of arguments in the context of the HelpCommand
     * and returns a HelpCommand object for execution.
     * @throws ParseException if the command name provided does not exist
     */
    @Override
    public HelpCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            // No arguments provided, show general help window
            return new HelpCommand();
        } else {
            // Validate that the command exists
            if (HelpWindow.getDescriptionForCommand(trimmedArgs) == null) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND, trimmedArgs));
            }
            // Command name provided and is valid, show specific command help
            return new HelpCommand(trimmedArgs);
        }
    }
}
