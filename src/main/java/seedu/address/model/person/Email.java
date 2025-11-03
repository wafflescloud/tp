package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's email in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmail(String)}
 */
public class Email {

    public static final String MESSAGE_CONSTRAINTS = "Emails must follow RFC 5322 format (local-part@domain)\n"
            + "and not exceed 998 characters.\n"
            + "The local-part can contain alphanumeric characters and special characters.\n"
            + "The domain must be properly formatted with valid domain labels separated by periods.";

    /**
     * RFC5322 email regex pattern obtained from:
     * https://emailregex.com/
     */
    private static final String VALIDATION_REGEX = "^[a-zA-Z0-9_!#$%&'\"*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";


    public final String value;

    /**
     * Constructs an {@code Email}.
     *
     * @param email A valid email address.
     */
    public Email(String email) {
        requireNonNull(email);
        checkArgument(isValidEmail(email), MESSAGE_CONSTRAINTS);
        value = email;
    }

    /**
     * Returns if a given string is a valid email.
     */
    public static boolean isValidEmail(String test) {
        return test.length() <= 998 && test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Email)) {
            return false;
        }

        Email otherEmail = (Email) other;
        return value.equals(otherEmail.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
