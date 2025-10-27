package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATETIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FEEDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import seedu.address.logic.commands.FeedCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonName;

/**
 * Parses input arguments and creates a new FeedCommand object.
 */
public class FeedCommandParser implements Parser<FeedCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FeedCommand
     * and returns a FeedCommand object for execution.
     *
     * @param args the arguments to be parsed
     * @return the FeedCommand to be executed
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public FeedCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_FEEDER, PREFIX_NAME, PREFIX_DATETIME);

        if (!arePrefixesPresent(argMultimap, PREFIX_FEEDER, PREFIX_NAME, PREFIX_DATETIME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FeedCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_FEEDER, PREFIX_NAME, PREFIX_DATETIME);

        PersonName personName = ParserUtil.parsePersonName(argMultimap.getValue(PREFIX_FEEDER).get());
        String animalName = argMultimap.getValue(PREFIX_NAME).get().trim();
        LocalDateTime feedingTime = ParserUtil.parseDateTime(argMultimap.getValue(PREFIX_DATETIME).get());

        return new FeedCommand(personName, animalName, feedingTime);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
