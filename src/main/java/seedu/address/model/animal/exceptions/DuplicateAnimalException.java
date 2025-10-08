package seedu.address.model.animal.exceptions;

/**
 * Signals that the operation will result in duplicate Animals (Animals are considered duplicates if they have the same
 * identity).
 */
public class DuplicateAnimalException extends RuntimeException {

    /**
     * Creates a new {@code DuplicateAnimalException} with the default error message.
     */
    public DuplicateAnimalException() {
        super("Operation would result in duplicate animals");
    }
}
