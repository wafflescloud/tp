package seedu.address.model.person;

import seedu.address.model.Name;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class PersonName extends Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters and spaces, and it should not be blank";

    /**
     * Constructs a {@code PersonName}.
     *
     * @param name A valid name.
     */
    public PersonName(String name) {
        super(name);
    }

    /**
     * Returns true if a given string is a valid person name.
     */
    public static boolean isValidPersonName(String test) {
        return test.matches(BASE_VALIDATION_REGEX);
    }

    @Override
    protected String getMessageConstraints() {
        return MESSAGE_CONSTRAINTS;
    }

    @Override
    protected boolean isValidName(String test) {
        return PersonName.isValidPersonName(test);
    }
}
