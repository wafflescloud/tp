package seedu.address.logic.commands;

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
            + PREFIX_NAME + "Max";
}
