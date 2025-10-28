package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Routes to the appropriate parser based on whether it's an animal or person being deleted.
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    @Override
    public DeleteCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        String[] typeAndRest = trimmedArgs.split("\\s+", 2);

        if (typeAndRest.length < 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        Type type = new Type(typeAndRest[0]);
        String rest = " " + typeAndRest[1];

        if (type.equals(CliSyntax.TYPE_PERSON)) {
            return new DeletePersonCommandParser().parse(rest);
        } else if (type.equals(CliSyntax.TYPE_ANIMAL)) {
            return new DeleteAnimalCommandParser().parse(rest);
        } else if (type.equals(CliSyntax.TYPE_FEEDING_SESSION)) {
            return new DeleteFeedCommandParser().parse(rest);
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
    }
}
