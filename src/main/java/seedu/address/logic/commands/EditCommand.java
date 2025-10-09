package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

/**
 * Represents an edit command that edits contact information of a person or an animal from the address book.
 * <p>
 * This is an abstract class to be extended by specific edit commands such as
 * {@link EditPersonCommand}.
 * </p>
 */
public abstract class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person/animal identified "
            + "by the name used in the displayed person/animal list. "
            + "Existing values will be overwritten by the input values.\n"
            + "For Person, \n"
            + "Parameters: NAME (must < 30 characters) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " John Doe "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com"
            + "\n\n"
            + "For Animal, \n"
            + "Parameters: NAME (must < 30 characters) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION] "
            + "[" + PREFIX_LOCATION + "LOCATION] "
            + "Example: " + COMMAND_WORD + " Kitty "
            + PREFIX_DESCRIPTION + "Grey Cat "
            + PREFIX_LOCATION + "UTOWN";

}
