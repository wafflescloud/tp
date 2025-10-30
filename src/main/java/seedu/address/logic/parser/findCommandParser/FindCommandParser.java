package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.TYPE_ANIMAL;
import static seedu.address.logic.parser.CliSyntax.TYPE_PERSON;

import java.util.List;

import seedu.address.logic.commands.FindAnimalCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindPersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.animal.AnimalMatchesKeywordsPredicate;
import seedu.address.model.animal.NameContainsKeywordsPredicateAnimal;
import seedu.address.model.person.NameContainsKeywordsPredicatePerson;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

/**
 * Unified parser for find commands that handles both Person and Animal types.
 * Automatically detects the contact type from the input arguments.
 */
public class FindCommandParser implements Parser<FindCommand> {

    private enum ContactType {
        PERSON, ANIMAL
    }

    @Override
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        // Determine contact type from the first argument
        ContactType contactType = determineContactType(trimmedArgs);

        // Remove the type keyword from arguments for further parsing
        String remainingArgs = removeTypeKeyword(trimmedArgs, contactType);

        // Add a space at the beginning if it doesn't start with one (needed for ArgumentTokenizer)
        if (!remainingArgs.startsWith(" ")) {
            remainingArgs = " " + remainingArgs;
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(remainingArgs, PREFIX_NAME, PREFIX_TAG);

        List<String> nameValues = argMultimap.getAllValues(PREFIX_NAME);
        List<String> tagValues = argMultimap.getAllValues(PREFIX_TAG);

        boolean hasNames = nameValues != null && !nameValues.isEmpty();
        boolean hasTags = tagValues != null && !tagValues.isEmpty();

        if (!hasNames && !hasTags) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        if (hasNames && !hasTags) {
            return createNameOnlyCommand(nameValues, contactType);
        }

        // Use combined predicate when tags are present (with or without names)
        return createCombinedCommand(nameValues, tagValues, contactType);
    }

    /**
     * Determines the contact type from the input arguments.
     */
    private ContactType determineContactType(String args) throws ParseException {
        if (args.startsWith(TYPE_PERSON.toString())) {
            return ContactType.PERSON;
        } else if (args.startsWith(TYPE_ANIMAL.toString())) {
            return ContactType.ANIMAL;
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Removes the type keyword from the beginning of the arguments string.
     */
    private String removeTypeKeyword(String args, ContactType contactType) {
        switch (contactType) {
        case PERSON:
            return args.substring(TYPE_PERSON.toString().length()).trim();
        case ANIMAL:
            return args.substring(TYPE_ANIMAL.toString().length()).trim();
        default:
            return args;
        }
    }

    private FindCommand createNameOnlyCommand(List<String> nameValues, ContactType contactType) {
        switch (contactType) {
        case PERSON:
            return new FindPersonCommand(new NameContainsKeywordsPredicatePerson(nameValues));
        case ANIMAL:
            return new FindAnimalCommand(new NameContainsKeywordsPredicateAnimal(nameValues));
        default:
            throw new IllegalStateException("Unknown contact type: " + contactType);
        }
    }

    private FindCommand createCombinedCommand(List<String> nameValues, List<String> tagValues,
            ContactType contactType) {
        switch (contactType) {
        case PERSON:
            return new FindPersonCommand(new PersonMatchesKeywordsPredicate(nameValues, tagValues));
        case ANIMAL:
            return new FindAnimalCommand(new AnimalMatchesKeywordsPredicate(nameValues, tagValues));
        default:
            throw new IllegalStateException("Unknown contact type: " + contactType);
        }
    }
}
