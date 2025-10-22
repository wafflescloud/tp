package seedu.address.logic.parser;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.ui.HelpWindow;

/**
 * Parses input arguments and creates a new {@code HelpCommand} object.
 * Accepts either no arguments (to show the general help window) or a valid command name
 * (to show help for a specific command). For invalid commands, returns a HelpCommand
 * that opens the general help window with an "unrecognized command" message.
 */
public class HelpCommandParser implements Parser<HelpCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the HelpCommand
     * and returns a HelpCommand object for execution.
     * Invalid commands result in opening the general help window.
     */
    @Override
    public HelpCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            return new HelpCommand();
        } else {
            try {
                String commandName = validateAndExtractCommandName(trimmedArgs);

                if (HelpWindow.getDescriptionForCommand(commandName) == null) {
                    return new HelpCommand(null, true);
                }
                return new HelpCommand(commandName);
            } catch (ParseException e) {
                return new HelpCommand(null, true);
            }
        }
    }

    /**
     * Validates the command arguments and extracts the base command name.
     * Only allows specific valid command combinations.
     *
     * @param args The trimmed command arguments
     * @return The base command name if valid
     * @throws ParseException if the command combination is invalid
     */
    private String validateAndExtractCommandName(String args) throws ParseException {
        String[] argParts = args.split("\\s+");
        String baseCommand = argParts[0];

        if (argParts.length == 1) {
            return baseCommand;
        }

        if (argParts.length == 2) {
            switch (baseCommand) {
            case "add":
            case "delete":
            case "edit":
            case "find":
                if (argParts[1].equals("person") || argParts[1].equals("animal")) {
                    return baseCommand;
                }
                break;
            default:
                break;
            }
            throw new ParseException("Invalid command combination");
        }
        throw new ParseException("Invalid command combination");
    }
}
