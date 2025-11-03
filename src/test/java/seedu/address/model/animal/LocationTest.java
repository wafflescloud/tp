package seedu.address.model.animal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class LocationTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Location(null));
    }

    @Test
    public void constructor_invalidLocation_throwsIllegalArgumentException() {
        String invalidLocation = "";
        assertThrows(IllegalArgumentException.class, () -> new Location(invalidLocation));
    }

    @Test
    public void isValidLocation() {
        // null location
        assertThrows(NullPointerException.class, () -> Location.isValidLocation(null));

        // invalid locations
        assertFalse(Location.isValidLocation("")); // empty string
        assertFalse(Location.isValidLocation(" ")); // spaces only

        // valid locations
        assertTrue(Location.isValidLocation("Block A, Kennel 1"));
        assertTrue(Location.isValidLocation("A")); // one character
        assertTrue(Location.isValidLocation("Shelter Wing B, Room 5")); // with comma
        assertTrue(Location.isValidLocation("Cage 123")); // with numbers
    }

    @Test
    public void equals() {
        Location location = new Location("Block A, Kennel 1");

        // same values -> returns true
        assertTrue(location.equals(new Location("Block A, Kennel 1")));

        // same object -> returns true
        assertTrue(location.equals(location));

        // null -> returns false
        assertFalse(location.equals(null));

        // different types -> returns false
        assertFalse(location.equals(5.0f));

        // different values -> returns false
        assertFalse(location.equals(new Location("Block B, Kennel 2")));
    }

    @Test
    public void hashCode_sameLocation_returnsSameHashCode() {
        Location location1 = new Location("Block A, Kennel 1");
        Location location2 = new Location("Block A, Kennel 1");
        assertEquals(location1.hashCode(), location2.hashCode());
    }

    @Test
    public void toStringMethod() {
        Location location = new Location("Block A, Kennel 1");
        assertEquals("Block A, Kennel 1", location.toString());
    }
}

















