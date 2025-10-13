package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeletePersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonName;

/**
 * Parses input arguments and creates a new DeletePersonCommand object.
 */
public class DeletePersonCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeletePersonCommand
     * and returns a DeletePersonCommand object for execution.
     * @param args the arguments to be parsed
     * @return the command to be executed
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public DeleteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        if (!argMultimap.getValue(PREFIX_NAME).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        PersonName name = ParserUtil.parsePersonName(argMultimap.getValue(PREFIX_NAME).get());
        return new DeletePersonCommand(name);
    }
}
