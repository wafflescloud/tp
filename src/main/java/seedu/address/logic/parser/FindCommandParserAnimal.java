package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindCommandAnimal;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.animal.NameContainsKeywordsPredicateAnimal;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParserAnimal implements Parser<FindCommandAnimal> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommandAnimal parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommandAnimal.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindCommandAnimal(new NameContainsKeywordsPredicateAnimal(Arrays.asList(nameKeywords)));
    }

}
