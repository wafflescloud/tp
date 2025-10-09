package seedu.address.logic.commands;

/**
 * Represents a delete command that removes a person or an animal from the system.
 * <p>
 * This is an abstract class to be extended by specific delete commands like
 * {@link DeletePersonCommand} and {@link DeleteAnimalCommand}.
 * </p>
 */
public abstract class DeleteCommand extends Command {

    /** The command word used to invoke the delete command from the CLI. */
    public static final String COMMAND_WORD = "delete";

    /**
     * Usage instructions for the delete command.
     * <p>
     * This message should be overridden by subclasses to specify
     * whether it deletes a person or an animal.
     * </p>
     */
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the animal identified by the name used in the displayed person list.\n"
            + "Parameters: NAME (must be a string)\n"
            + "Example: " + COMMAND_WORD + " n/Whiskers";
}
