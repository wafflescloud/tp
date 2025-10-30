package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters and spaces, and it should not be blank. "
            + "Names should not exceed 30 characters.";

    /*
     * The first character of the name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";
    public static final int MAX_LENGTH = 30;

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        String trimmedValue = name.trim();
        checkArgument(isValidName(trimmedValue), MESSAGE_CONSTRAINTS);
        fullName = trimmedValue;
    }

    /**
     * Returns true if a given string is a valid name.
     *
     * @param test The string to test.
     * @return True if the string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX) && test.length() <= MAX_LENGTH;
    }

    /**
     * Returns the full name as a string.
     *
     * @return The full name.
     */
    @Override
    public String toString() {
        return fullName;
    }

    /**
     * Returns true if both names have the same value.
     * This defines a stronger notion of equality between two names.
     *
     * @param other The other object to compare with.
     * @return True if both objects are Names with the same value.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equalsIgnoreCase(otherName.fullName);
    }

    /**
     * Returns the hash code of this name.
     *
     * @return The hash code based on the full name.
     */
    @Override
    public int hashCode() {
        return fullName.hashCode();
    }
}
