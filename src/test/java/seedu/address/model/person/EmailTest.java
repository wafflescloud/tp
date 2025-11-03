package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import static seedu.address.testutil.Assert.assertThrows;

public class EmailTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Email(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }

    @Test
    public void isValidEmail() {
        // null email
        assertThrows(NullPointerException.class, () -> Email.isValidEmail(null));

        // blank email
        assertFalse(Email.isValidEmail("")); // empty string
        assertFalse(Email.isValidEmail(" ")); // spaces only

        // missing parts
        assertFalse(Email.isValidEmail("@example.com")); // missing local part
        assertFalse(Email.isValidEmail("peterjackexample.com")); // missing '@' symbol
        assertFalse(Email.isValidEmail("peterjack@")); // missing domain name

        // invalid parts - spaces not allowed in unquoted local part
        assertFalse(Email.isValidEmail("peter jack@example.com")); // spaces in local part
        assertFalse(Email.isValidEmail(" peterjack@example.com")); // leading space
        assertFalse(Email.isValidEmail("peterjack@example.com ")); // trailing space
        assertFalse(Email.isValidEmail("peterjack@@example.com")); // double '@' symbol
        assertFalse(Email.isValidEmail("peter@jack@example.com")); // '@' symbol in local part

        // invalid domain
        assertFalse(Email.isValidEmail("peterjack@exam ple.com")); // spaces in domain name
        assertFalse(Email.isValidEmail("peterjack@")); // missing domain

        // exceeds 998 character limit
        String longEmail = "a".repeat(990) + "@test.com"; // 999 characters
        assertFalse(Email.isValidEmail(longEmail));

        // valid emails according to RFC 5322
        assertTrue(Email.isValidEmail("-peterjack1190@example.com")); // leading '-'
        assertTrue(Email.isValidEmail("peterjack_1190@example.com")); // underscore in local part
        assertTrue(Email.isValidEmail("peterjack.1190@example.com")); // period in local part
        assertTrue(Email.isValidEmail("peterjack+1190@example.com")); // '+' symbol in local part
        assertTrue(Email.isValidEmail("peterjack-1190@example.com")); // hyphen in local part
        assertTrue(Email.isValidEmail("a@b")); // minimal
        assertTrue(Email.isValidEmail("test@localhost")); // alphabets only
        assertTrue(Email.isValidEmail("123@145")); // numeric local part and domain name
        assertTrue(Email.isValidEmail("a1+be.d@example1.com")); // mixture of alphanumeric and special characters
        assertTrue(Email.isValidEmail("peter_jack@very-very-very-long-example.com")); // long domain name
        assertTrue(Email.isValidEmail("if.you.dream.it_you.can.do.it@example.com")); // long local part
        assertTrue(Email.isValidEmail("e1234567@u.nus.edu")); // more than one period in domain
        assertTrue(Email.isValidEmail("!#$%&'*+-/=?^_`{|}~@example.com")); // special characters in local part
        assertTrue(Email.isValidEmail("\"peter jack\"@example.com")); // quoted string allows spaces
        assertTrue(Email.isValidEmail("user@[192.168.1.1]")); // IP address as domain

        // valid at exactly 998 characters
        String maxLengthEmail = "a".repeat(987) + "@test.com"; // exactly 998 characters
        assertTrue(Email.isValidEmail(maxLengthEmail));
    }

    @Test
    public void equals() {
        Email email = new Email("valid@email");

        // same values -> returns true
        assertTrue(email.equals(new Email("valid@email")));

        // same object -> returns true
        assertTrue(email.equals(email));

        // null -> returns false
        assertFalse(email.equals(null));

        // different types -> returns false
        assertFalse(email.equals(5.0f));

        // different values -> returns false
        assertFalse(email.equals(new Email("other.valid@email")));

        // case insensitive -> returns true
        assertTrue(email.equals(new Email("VALID@EMAIL")));
        assertTrue(email.equals(new Email("Valid@Email")));
    }
}
