package seedu.address.model.animal;

import seedu.address.model.Name;

/**
 * Represents an Animal's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class AnimalName extends Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters and spaces, not be blank, "
            + "and be at most 30 characters long";

    public static final int MAX_LENGTH = 30;

    /**
     * Constructs a {@code AnimalName}.
     *
     * @param name A valid name.
     */
    public AnimalName(String name) {
        super(name);
    }

    /**
     * Standardizes a name by trimming leading/trailing spaces
     * and replacing multiple spaces between words with a single space.
     *
     * @param name The input string to standardize.
     * @return A standardized string with normalized spaces.
     */
    private static String standardiseName(String name) {
        return name.trim().replaceAll("\\s+", " ");
    }

    /**
     * Returns true if a given string is a valid animal name.
     */
    public static boolean isValidAnimalName(String test) {
        return test.matches(BASE_VALIDATION_REGEX) && test.length() <= MAX_LENGTH;
    }

    @Override
    protected String getMessageConstraints() {
        return MESSAGE_CONSTRAINTS;
    }

    @Override
    protected boolean isValidName(String test) {
        return isValidAnimalName(test);
    }
}
