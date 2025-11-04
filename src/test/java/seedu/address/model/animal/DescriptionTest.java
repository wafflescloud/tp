package seedu.address.model.animal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class DescriptionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Description(null));
    }

    @Test
    public void constructor_invalidDescription_throwsIllegalArgumentException() {
        String invalidDescription = "";
        assertThrows(IllegalArgumentException.class, () -> new Description(invalidDescription));
    }

    @Test
    public void isValidDescription() {
        // null description
        assertThrows(NullPointerException.class, () -> Description.isValidDescription(null));

        // invalid descriptions
        assertFalse(Description.isValidDescription("")); // empty string
        assertFalse(Description.isValidDescription(" ")); // spaces only

        // valid descriptions
        assertTrue(Description.isValidDescription("Golden Retriever"));
        assertTrue(Description.isValidDescription("a")); // one character
        assertTrue(Description.isValidDescription("Friendly dog, loves to play")); // with punctuation
        assertTrue(Description.isValidDescription("Large brown dog with white patches")); // longer description
    }

    @Test
    public void equals() {
        Description description = new Description("Golden Retriever");

        // same values -> returns true
        assertTrue(description.equals(new Description("Golden Retriever")));

        // same object -> returns true
        assertTrue(description.equals(description));

        // null -> returns false
        assertFalse(description.equals(null));

        // different types -> returns false
        assertFalse(description.equals(5.0f));

        // different values -> returns false
        assertFalse(description.equals(new Description("Labrador")));
    }

    @Test
    public void hashCode_sameDescription_returnsSameHashCode() {
        Description description1 = new Description("Golden Retriever");
        Description description2 = new Description("Golden Retriever");
        assertEquals(description1.hashCode(), description2.hashCode());
    }

    @Test
    public void toStringMethod() {
        Description description = new Description("Golden Retriever");
        assertEquals("Golden Retriever", description.toString());
    }
}
