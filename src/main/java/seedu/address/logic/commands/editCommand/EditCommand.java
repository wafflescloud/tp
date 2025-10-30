package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

/**
 * Edits an entity identified using its name from the address book.
 */
public abstract class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person/animal identified "
            + "by the name used in the displayed person/animal list. "
            + "Existing values will be overwritten by the input values.\n"
            + "1. To edit a person: \n"
            + "Parameters: NAME "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " person John Doe "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com\n"
            + "\n"
            + "2. To edit an animal: \n"
            + "Parameters: NAME "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION] "
            + "[" + PREFIX_LOCATION + "LOCATION] "
            + "[" + PREFIX_TAG + "TAG]... "
            + "Example: " + COMMAND_WORD + " animal Kitty "
            + PREFIX_DESCRIPTION + "Grey Cat "
            + PREFIX_LOCATION + "UTOWN "
            + PREFIX_TAG + "Alex";
}
