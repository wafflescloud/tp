package seedu.address.logic.parser;

import seedu.address.logic.commands.HelpCommand;

public class HelpCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the HelpCommand
     * and returns a HelpCommand object for execution.
     */
    public HelpCommand parse(String args) {
        return new HelpCommand(args);
    }
}
