package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.TYPE_ANIMAL;
import static seedu.address.logic.parser.CliSyntax.TYPE_PERSON;

/**
 * Finds and lists all persons or animals in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public abstract class FindCommand extends Command {
    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons/animals whose names and/or tags "
            + "match the given keywords and displays them as a list with index numbers.\n"
            + "Use n/ for name substring (case-insensitive). Use t/ for tag (exact match, case-insensitive).\n"
            + "At least one of n/ or t/ must be provided. Multiple flags allowed.\n"
            + "1. To find a person:\n"
            + "Example: " + COMMAND_WORD + " "
            + TYPE_PERSON + " n/luna n/bob\n"
            + "Example (tags): " + COMMAND_WORD + " " + TYPE_PERSON + " t/happy t/angry\n\n"
            + "2. To find an animal:\n"
            + "Example: " + COMMAND_WORD + " "
            + TYPE_ANIMAL + " n/ma n/lu\n"
            + "Example (tags): " + COMMAND_WORD + " " + TYPE_ANIMAL + " t/friendly t/small\n\n"
            + "Alternatively, use:\n"
            + FindContactCommand.PERSON_MESSAGE_USAGE + "\n\n"
            + FindContactCommand.ANIMAL_MESSAGE_USAGE;
}
