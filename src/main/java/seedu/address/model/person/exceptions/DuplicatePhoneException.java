package seedu.address.model.person.exceptions;

/**
 * Signals that the operation will result in duplicate Phones
 */
public class DuplicatePhoneException extends RuntimeException {
    public DuplicatePhoneException() {
        super("Operation would result in duplicate phone numbers");
    }
}