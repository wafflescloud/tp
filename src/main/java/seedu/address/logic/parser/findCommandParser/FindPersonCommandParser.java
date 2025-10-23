package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindPersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicatePerson;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindPersonCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TAG);

        List<String> nameValues = argMultimap.getAllValues(PREFIX_NAME);
        List<String> tagValues = argMultimap.getAllValues(PREFIX_TAG);

        boolean hasNames = nameValues != null && !nameValues.isEmpty();
        boolean hasTags = tagValues != null && !tagValues.isEmpty();

        if (!hasNames && !hasTags) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPersonCommand.MESSAGE_USAGE));
        }

        if (hasNames && !hasTags) {
            return new FindPersonCommand(new NameContainsKeywordsPredicatePerson(nameValues));
        }

        // Use combined predicate when tags are present (with or without names)
        return new FindPersonCommand(new PersonMatchesKeywordsPredicate(nameValues, tagValues));
    }

}
