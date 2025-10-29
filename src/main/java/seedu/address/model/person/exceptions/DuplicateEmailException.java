package seedu.address.model.person.exceptions;

/**
 * Signals that the operation will result in duplicate Emails
 */
public class DuplicateEmailException extends RuntimeException {
  public DuplicateEmailException() {
    super("Operation would result in duplicate emails");
  }
}
