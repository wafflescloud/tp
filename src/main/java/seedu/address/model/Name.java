package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Abstract class representing a name in the address book.
 * Contains common functionality shared by different name types.
 */
public abstract class Name {

    /*
     * The first character must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    protected static final String BASE_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    protected Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), getMessageConstraints());
        fullName = name;
    }

    /**
     * Returns the validation constraints message for this name type.
     * Must be implemented by subclasses.
     */
    protected abstract String getMessageConstraints();

    /**
     * Returns true if a given string is a valid name for this type.
     * Must be implemented by subclasses.
     */
    protected abstract boolean isValidName(String test);

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // Check if other is the same class (handles different Name subclasses)
        if (other == null || !other.getClass().equals(this.getClass())) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }
}
