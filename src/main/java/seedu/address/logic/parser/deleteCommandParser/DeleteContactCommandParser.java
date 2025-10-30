package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteContactCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.animal.AnimalName;
import seedu.address.model.person.PersonName;

/**
 * Unified parser for delete contact commands that handles both Person and Animal types.
 * Automatically detects the contact type from the input arguments.
 */
public class DeleteContactCommandParser implements Parser<DeleteCommand> {

    private enum ContactType {
        PERSON, ANIMAL
    }

    @Override
    public DeleteCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        // Determine contact type from the first argument
        ContactType contactType = determineContactType(trimmedArgs);

        // Remove the type keyword from arguments for further parsing
        String remainingArgs = removeTypeKeyword(trimmedArgs, contactType);

        // Add a space at the beginning if it doesn't start with one (needed for ArgumentTokenizer)
        if (!remainingArgs.startsWith(" ")) {
            remainingArgs = " " + remainingArgs;
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(remainingArgs, PREFIX_NAME);

        if (!argMultimap.getValue(PREFIX_NAME).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return createDeleteCommand(contactType, argMultimap.getValue(PREFIX_NAME).get());
    }

    /**
     * Determines the contact type from the input arguments.
     */
    private ContactType determineContactType(String args) throws ParseException {
        if (args.startsWith(CliSyntax.TYPE_PERSON.toString())) {
            return ContactType.PERSON;
        } else if (args.startsWith(CliSyntax.TYPE_ANIMAL.toString())) {
            return ContactType.ANIMAL;
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Removes the type keyword from the beginning of the arguments string.
     */
    private String removeTypeKeyword(String args, ContactType contactType) {
        switch (contactType) {
        case PERSON:
            return args.substring(CliSyntax.TYPE_PERSON.toString().length()).trim();
        case ANIMAL:
            return args.substring(CliSyntax.TYPE_ANIMAL.toString().length()).trim();
        default:
            return args;
        }
    }

    /**
     * Creates the appropriate delete command based on contact type.
     */
    private DeleteCommand createDeleteCommand(ContactType contactType, String nameValue) throws ParseException {
        switch (contactType) {
        case PERSON:
            PersonName personName = ParserUtil.parsePersonName(nameValue);
            return DeleteContactCommand.forPerson(personName);
        case ANIMAL:
            AnimalName animalName = ParserUtil.parseAnimalName(nameValue);
            return DeleteContactCommand.forAnimal(animalName);
        default:
            throw new IllegalStateException("Unknown contact type: " + contactType);
        }
    }
}
