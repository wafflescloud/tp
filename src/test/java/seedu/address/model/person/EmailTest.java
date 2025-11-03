package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

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
        assertFalse(Email.isValidEmail("peterjack@-example.com")); // domain starts with hyphen
        assertFalse(Email.isValidEmail("peterjack@example-.com")); // domain label ends with hyphen
        assertFalse(Email.isValidEmail("peterjack@.example.com")); // domain starts with period
        assertFalse(Email.isValidEmail("peterjack@example..com")); // consecutive periods in domain
        assertFalse(Email.isValidEmail("a@b")); // domain without dot or brackets
        assertFalse(Email.isValidEmail("test@localhost")); // single label domain without dot

        // invalid local part
        assertFalse(Email.isValidEmail(".peterjack@example.com")); // starts with period
        assertFalse(Email.isValidEmail("peterjack.@example.com")); // ends with period
        assertFalse(Email.isValidEmail("peter..jack@example.com")); // consecutive periods

        // exceeds 998 character limit
        String longEmail = "a".repeat(990) + "@test.com"; // 999 characters
        assertFalse(Email.isValidEmail(longEmail));

        // valid emails - basic alphanumeric
        assertTrue(Email.isValidEmail("user@example.com")); // simple valid email
        assertTrue(Email.isValidEmail("a@b.c")); // minimal with dot
        assertTrue(Email.isValidEmail("123@145.678")); // numeric local part and domain

        // valid emails - special characters in local part (RFC 5322 allowed)
        assertTrue(Email.isValidEmail("user.name@example.com")); // period in local part
        assertTrue(Email.isValidEmail("user+tag@example.com")); // plus sign
        assertTrue(Email.isValidEmail("user-name@example.com")); // hyphen
        assertTrue(Email.isValidEmail("user_name@example.com")); // underscore
        assertTrue(Email.isValidEmail("!user@example.com")); // exclamation mark
        assertTrue(Email.isValidEmail("#user@example.com")); // hash
        assertTrue(Email.isValidEmail("$user@example.com")); // dollar sign
        assertTrue(Email.isValidEmail("%user@example.com")); // percent
        assertTrue(Email.isValidEmail("&user@example.com")); // ampersand
        assertTrue(Email.isValidEmail("'user@example.com")); // apostrophe
        assertTrue(Email.isValidEmail("*user@example.com")); // asterisk
        assertTrue(Email.isValidEmail("/user@example.com")); // forward slash
        assertTrue(Email.isValidEmail("=user@example.com")); // equals
        assertTrue(Email.isValidEmail("?user@example.com")); // question mark
        assertTrue(Email.isValidEmail("^user@example.com")); // caret
        assertTrue(Email.isValidEmail("`user@example.com")); // backtick
        assertTrue(Email.isValidEmail("{user@example.com")); // left brace
        assertTrue(Email.isValidEmail("|user@example.com")); // pipe
        assertTrue(Email.isValidEmail("}user@example.com")); // right brace
        assertTrue(Email.isValidEmail("~user@example.com")); // tilde
        assertTrue(Email.isValidEmail("-user@example.com")); // leading hyphen

        // valid emails - combination of special characters
        assertTrue(Email.isValidEmail("!#$%&'*+-/=?^_`{|}~@example.com")); // all special characters
        assertTrue(Email.isValidEmail("a1+be.d@example1.com")); // mixture of alphanumeric and special
        assertTrue(Email.isValidEmail("first.last+tag@example.com")); // multiple special characters

        // valid emails - domain variations
        assertTrue(Email.isValidEmail("user@example.co.uk")); // multiple domain labels
        assertTrue(Email.isValidEmail("user@subdomain.example.com")); // subdomain
        assertTrue(Email.isValidEmail("user@sub.domain.example.com")); // multiple subdomains
        assertTrue(Email.isValidEmail("user@example-site.com")); // hyphen in domain
        assertTrue(Email.isValidEmail("user@123.456.789.012")); // numeric domain labels
        assertTrue(Email.isValidEmail("user@very-long-domain-name-example.com")); // long domain label

        // valid emails - IP address domain
        assertTrue(Email.isValidEmail("user@[192.168.1.1]")); // IPv4 address
        assertTrue(Email.isValidEmail("user@[255.255.255.255]")); // max IPv4
        assertTrue(Email.isValidEmail("user@[0.0.0.0]")); // min IPv4

        // valid emails - complex local parts
        assertTrue(Email.isValidEmail("if.you.dream.it_you.can.do.it@example.com")); // long local part
        assertTrue(Email.isValidEmail("user+tag1+tag2@example.com")); // multiple tags
        assertTrue(Email.isValidEmail("a.b.c.d.e@example.com")); // multiple periods

        // valid emails - case variations (converted to lowercase)
        assertTrue(Email.isValidEmail("user@example.com"));
        assertTrue(Email.isValidEmail("User@Example.Com"));
        assertTrue(Email.isValidEmail("USER@EXAMPLE.COM"));

        // valid at exactly 998 characters
        String maxLengthEmail = "a".repeat(987) + "@test.com"; // exactly 998 characters
        assertTrue(Email.isValidEmail(maxLengthEmail));

        // valid - edge cases from original tests
        assertTrue(Email.isValidEmail("e1234567@u.nus.edu")); // educational domain
        assertTrue(Email.isValidEmail("peter_jack@very-very-very-long-example.com")); // long domain
    }

    @Test
    public void equals() {
        Email email = new Email("valid@email.com");

        // same values -> returns true
        assertTrue(email.equals(new Email("valid@email.com")));

        // same object -> returns true
        assertTrue(email.equals(email));

        // null -> returns false
        assertFalse(email.equals(null));

        // different types -> returns false
        assertFalse(email.equals(5.0f));

        // different values -> returns false
        assertFalse(email.equals(new Email("other.valid@email.com")));

        // case insensitive -> returns true
        assertTrue(email.equals(new Email("VALID@EMAIL.COM")));
        assertTrue(email.equals(new Email("Valid@Email.com")));
    }
}
