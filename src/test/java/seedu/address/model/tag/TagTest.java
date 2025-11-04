package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // invalid tag names
        assertFalse(Tag.isValidTagName("")); // empty string
        assertFalse(Tag.isValidTagName(" ")); // spaces only
        assertFalse(Tag.isValidTagName("^")); // only non-alphanumeric characters
        assertFalse(Tag.isValidTagName("friend*")); // contains non-alphanumeric characters
        assertFalse(Tag.isValidTagName("a".repeat(31))); // exceeds max length of 30
        assertFalse(Tag.isValidTagName("This is a very long tag name that exceeds thirty characters"));
        assertFalse(Tag.isValidTagName("tag!")); // contains special character
        assertFalse(Tag.isValidTagName("tag@home")); // contains @ symbol
        assertFalse(Tag.isValidTagName("tag#1")); // contains # symbol

        // valid tag names
        assertTrue(Tag.isValidTagName("friend")); // alphabets only
        assertTrue(Tag.isValidTagName("Friend")); // with capital letters
        assertTrue(Tag.isValidTagName("friend123")); // alphanumeric
        assertTrue(Tag.isValidTagName("123friend")); // starts with number
        assertTrue(Tag.isValidTagName("best friend")); // with space
        assertTrue(Tag.isValidTagName("Best Friend")); // multiple words with capitals
        assertTrue(Tag.isValidTagName("a")); // one character
        assertTrue(Tag.isValidTagName("1")); // one digit
        assertTrue(Tag.isValidTagName("a".repeat(30))); // exactly 30 characters (max length)
        assertTrue(Tag.isValidTagName("friend 1")); // alphanumeric with space
        assertTrue(Tag.isValidTagName("best friend ever")); // multiple words
    }

    @Test
    public void equals() {
        Tag tag = new Tag("friend");

        // same values -> returns true
        assertTrue(tag.equals(new Tag("friend")));

        // same object -> returns true
        assertTrue(tag.equals(tag));

        // null -> returns false
        assertFalse(tag.equals(null));

        // different types -> returns false
        assertFalse(tag.equals(5.0f));

        // different values -> returns false
        assertFalse(tag.equals(new Tag("enemy")));

        // different case -> returns false (case-sensitive)
        assertFalse(tag.equals(new Tag("Friend")));
    }

    @Test
    public void hashCode_sameTag_returnsSameHashCode() {
        Tag tag1 = new Tag("friend");
        Tag tag2 = new Tag("friend");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void hashCode_differentTag_returnsDifferentHashCode() {
        Tag tag1 = new Tag("friend");
        Tag tag2 = new Tag("enemy");
        assertFalse(tag1.hashCode() == tag2.hashCode());
    }

    @Test
    public void toStringMethod() {
        Tag tag = new Tag("friend");
        assertEquals("[friend]", tag.toString());
    }

    @Test
    public void toStringMethod_multipleWords() {
        Tag tag = new Tag("best friend");
        assertEquals("[best friend]", tag.toString());
    }

    @Test
    public void toStringMethod_alphanumeric() {
        Tag tag = new Tag("friend123");
        assertEquals("[friend123]", tag.toString());
    }
}
