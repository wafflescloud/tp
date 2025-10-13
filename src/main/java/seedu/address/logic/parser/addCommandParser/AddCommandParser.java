package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Routes to the appropriate parser based on whether it's an animal or person being added.
 */
public class AddCommandParser implements Parser<AddCommand> {

    @Override
    public AddCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        String[] typeAndRest = trimmedArgs.split("\\s+", 2);

        if (typeAndRest.length < 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        Type type = new Type(typeAndRest[0]);
        String rest = " " + typeAndRest[1];

        if (type.equals(CliSyntax.TYPE_PERSON)) {
            return new AddPersonCommandParser().parse(rest);
        } else if (type.equals(CliSyntax.TYPE_ANIMAL)) {
            return new AddAnimalCommandParser().parse(rest);
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
    }
}
