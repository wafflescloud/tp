package seedu.address.logic.commands;

import seedu.address.model.Model;

import static seedu.address.logic.parser.CliSyntax.TYPE_ANIMAL;
import static seedu.address.logic.parser.CliSyntax.TYPE_PERSON;

public abstract class FindCommand extends Command {
    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons/animals whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "1. To delete a person:\n"
            + "Example: " + COMMAND_WORD + " " 
            + TYPE_PERSON + "luna bob"
            + "\n\n"
            + "2. To delete an animal:\n"
            + "Example: " + COMMAND_WORD + " " 
            + TYPE_PERSON + "alice peter";
}
