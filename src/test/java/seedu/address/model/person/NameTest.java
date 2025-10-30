package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PersonName(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new PersonName(invalidName));
    }

    @Test
    public void isValidName() {
        // Create a PersonName instance to call non-static method
        PersonName testName = new PersonName("test");

        // null name
        assertThrows(NullPointerException.class, () -> testName.isValidName(null));

        // invalid name
        assertFalse(testName.isValidName("")); // empty string
        assertFalse(testName.isValidName(" ")); // spaces only
        assertFalse(testName.isValidName("^")); // only non-alphanumeric characters
        assertFalse(testName.isValidName("peter*")); // contains non-alphanumeric characters

        // valid name
        assertTrue(testName.isValidName("peter jack")); // alphabets only
        assertTrue(testName.isValidName("12345")); // numbers only
        assertTrue(testName.isValidName("peter the 2nd")); // alphanumeric characters
        assertTrue(testName.isValidName("Capital Tan")); // with capital letters
        assertTrue(testName.isValidName("David Roger Jackson Ray Jr 2nd")); // long names
    }

    @Test
    public void equals() {
        PersonName name = new PersonName("Valid PersonName");

        // same values -> returns true
        assertTrue(name.equals(new PersonName("Valid PersonName")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new PersonName("Other Valid PersonName")));
    }
}
