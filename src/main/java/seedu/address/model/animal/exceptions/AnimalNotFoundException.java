package seedu.address.model.animal.exceptions;

/**
 * Signals that the requested animal could not be found in the list.
 */
public class AnimalNotFoundException extends RuntimeException {

    /**
     * Creates a new {@code AnimalNotFoundException} with the default error message.
     */
    public AnimalNotFoundException() {
        super("Animal not found");
    }
}
