package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void constructor_trimsWhitespace() {
        Phone phone = new Phone("  91234567  ");
        assertEquals("91234567", phone.value);
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers
        assertFalse(Phone.isValidPhone("")); // empty string
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("91")); // less than 3 digits
        assertFalse(Phone.isValidPhone("12")); // only 2 digits
        assertFalse(Phone.isValidPhone("phone")); // non-numeric
        assertFalse(Phone.isValidPhone("9011p041")); // alphabets within digits
        assertFalse(Phone.isValidPhone("9312 1534")); // spaces within digits
        assertFalse(Phone.isValidPhone("93-121534")); // contains dash
        assertFalse(Phone.isValidPhone("+6591234567")); // contains plus sign

        // valid phone numbers
        assertTrue(Phone.isValidPhone("123")); // exactly 3 digits
        assertTrue(Phone.isValidPhone("1234")); // 4 digits
        assertTrue(Phone.isValidPhone("11111111")); // 8 digits starting with 1
        assertTrue(Phone.isValidPhone("61234567")); // 8 digits starting with 6
        assertTrue(Phone.isValidPhone("93121534")); // 8 digits starting with 9
        assertTrue(Phone.isValidPhone("81234444")); // 8 digits starting with 8
        assertTrue(Phone.isValidPhone("999")); // 3 digits starting with 9
        assertTrue(Phone.isValidPhone("12345678901234567890")); // very long number
    }

    @Test
    public void equals() {
        Phone phone = new Phone("91234567");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("91234567")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("99512345")));

        // whitespace differences (trimmed) -> returns true
        assertTrue(phone.equals(new Phone("  91234567  ")));
    }

    @Test
    public void hashCode_samePhone_returnsSameHashCode() {
        Phone phone1 = new Phone("91234567");
        Phone phone2 = new Phone("91234567");
        assertEquals(phone1.hashCode(), phone2.hashCode());
    }

    @Test
    public void toStringMethod() {
        Phone phone = new Phone("91234567");
        assertEquals("91234567", phone.toString());
    }
}
