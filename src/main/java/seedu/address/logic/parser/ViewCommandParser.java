package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.TYPE_ANIMAL;
import static seedu.address.logic.parser.CliSyntax.TYPE_PERSON;

import java.util.stream.Stream;

import seedu.address.logic.commands.ViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ViewCommand object
 */
public class ViewCommandParser implements Parser<ViewCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewCommand
     * and returns a ViewCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        String[] splitArgs = trimmedArgs.split("\\s+", 2);

        if (splitArgs.length < 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
        }

        Type type = parseType(splitArgs[0]);

        String argumentsWithSpace = " " + splitArgs[1];
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argumentsWithSpace, PREFIX_NAME);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME) || !argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
        }

        String name = argMultimap.getValue(PREFIX_NAME).get().trim();

        if (name.isEmpty()) {
            throw new ParseException("Name cannot be empty");
        }

        return new ViewCommand(type, name);
    }

    /**
     * Parses the type string and returns the corresponding Type object.
     *
     * @param typeString The string representation of the type
     * @return The Type object
     * @throws ParseException if the type is invalid
     */
    private Type parseType(String typeString) throws ParseException {
        Type inputType = new Type(typeString);

        if (TYPE_PERSON.equals(inputType)) {
            return TYPE_PERSON;
        } else if (TYPE_ANIMAL.equals(inputType)) {
            return TYPE_ANIMAL;
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
