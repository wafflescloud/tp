package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.parser.exceptions.ParseException;


/**
 * Parses input arguments and creates a new EditPersonCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments and {@code String} of type in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);

        // arguments after "edit"
        if (args.isEmpty()) {
            throw new ParseException(EditCommand.MESSAGE_USAGE);
        }

        String trimmedArgs = args.trim();
        String[] typeAndRest = trimmedArgs.split("\\s+", 2);

        if (typeAndRest.length < 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        Type type = new Type(typeAndRest[0]);
        String rest = " " + typeAndRest[1];

        if (type.equals(CliSyntax.TYPE_PERSON)) {
            return new EditPersonCommandParser().parse(rest);
        } else if (type.equals(CliSyntax.TYPE_ANIMAL)) {
            return null;
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }
    }

}
