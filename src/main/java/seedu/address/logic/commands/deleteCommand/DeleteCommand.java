package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DATETIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FEEDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

/**
 * Deletes an entity identified using its name from the address book.
 */
public abstract class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes an entity from the address book.\n"
            + "1. To delete a person:\n"
            + COMMAND_WORD + " person "
            + PREFIX_NAME + "NAME\n"
            + "Example: " + COMMAND_WORD + " person "
            + PREFIX_NAME + "John Doe\n"
            + "\n"
            + "2. To delete an animal:\n"
            + COMMAND_WORD + " animal "
            + PREFIX_NAME + "NAME\n"
            + "Example: " + COMMAND_WORD + " animal "
            + PREFIX_NAME + "Max\n"
            + "\n"
            + "3. To delete a feeding session:\n"
            + COMMAND_WORD + " feed "
            + PREFIX_NAME + "ANIMAL_NAME "
            + PREFIX_FEEDER + "PERSON_NAME "
            + PREFIX_DATETIME + "DATETIME\n"
            + "Example: " + COMMAND_WORD + " feed "
            + PREFIX_NAME + "Max "
            + PREFIX_FEEDER + "Matt "
            + PREFIX_DATETIME + "2025-12-25 09:00";
}
