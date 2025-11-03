package seedu.address.model.animal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_MAX;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LOCATION_MAX;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_MAX;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIENDLY;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalAnimals.MAX;
import static seedu.address.testutil.TypicalAnimals.WHISKERS;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.AnimalBuilder;

public class AnimalTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Animal animal = new AnimalBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> animal.getTags().remove(0));
    }

    @Test
    public void isSameAnimal() {
        // same object -> returns true
        assertTrue(WHISKERS.isSameAnimal(WHISKERS));

        // null -> returns false
        assertFalse(WHISKERS.isSameAnimal(null));

        // same name, all other attributes different -> returns true
        Animal editedWhiskers = new AnimalBuilder(WHISKERS).withDescription(VALID_DESCRIPTION_MAX)
                .withLocation(VALID_LOCATION_MAX).withTags(VALID_TAG_FRIENDLY).build();
        assertTrue(WHISKERS.isSameAnimal(editedWhiskers));

        // different name, all other attributes same -> returns false
        editedWhiskers = new AnimalBuilder(WHISKERS).withName(VALID_NAME_MAX).build();
        assertFalse(WHISKERS.isSameAnimal(editedWhiskers));

        // name differs in case, all other attributes same -> returns true (names are case-insensitive)
        Animal editedMax = new AnimalBuilder(MAX).withName(VALID_NAME_MAX.toLowerCase()).build();
        assertTrue(MAX.isSameAnimal(editedMax));

        // name has trailing spaces, all other attributes same -> returns true (names are trimmed)
        String nameWithTrailingSpaces = VALID_NAME_MAX + " ";
        editedMax = new AnimalBuilder(MAX).withName(nameWithTrailingSpaces).build();
        assertTrue(MAX.isSameAnimal(editedMax));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Animal whiskersCopy = new AnimalBuilder(WHISKERS).build();
        assertTrue(WHISKERS.equals(whiskersCopy));

        // same object -> returns true
        assertTrue(WHISKERS.equals(WHISKERS));

        // null -> returns false
        assertFalse(WHISKERS.equals(null));

        // different type -> returns false
        assertFalse(WHISKERS.equals(5));

        // different animal -> returns false
        assertFalse(WHISKERS.equals(MAX));

        // different name -> returns false
        Animal editedWhiskers = new AnimalBuilder(WHISKERS).withName(VALID_NAME_MAX).build();
        assertFalse(WHISKERS.equals(editedWhiskers));

        // different description -> returns false
        editedWhiskers = new AnimalBuilder(WHISKERS).withDescription(VALID_DESCRIPTION_MAX).build();
        assertFalse(WHISKERS.equals(editedWhiskers));

        // different location -> returns false
        editedWhiskers = new AnimalBuilder(WHISKERS).withLocation(VALID_LOCATION_MAX).build();
        assertFalse(WHISKERS.equals(editedWhiskers));

        // different tags -> returns false
        editedWhiskers = new AnimalBuilder(WHISKERS).withTags(VALID_TAG_FRIENDLY).build();
        assertFalse(WHISKERS.equals(editedWhiskers));
    }

    @Test
    public void toStringMethod() {
        String expected = Animal.class.getCanonicalName() + "{id=" + WHISKERS.getId()
                + ", name=" + WHISKERS.getName()
                + ", description=" + WHISKERS.getDescription()
                + ", location=" + WHISKERS.getLocation()
                + ", tags=" + WHISKERS.getTags()
                + ", feeding session IDs=" + WHISKERS.getFeedingSessionIds() + "}";
        assertEquals(expected, WHISKERS.toString());
    }

    @Test
    public void hashCode_sameAnimal_returnsSameHashCode() {
        Animal whiskersCopy = new AnimalBuilder(WHISKERS).build();
        assertEquals(WHISKERS.hashCode(), whiskersCopy.hashCode());
    }

    @Test
    public void getName_returnsCorrectName() {
        assertEquals(WHISKERS.getName(), new AnimalBuilder(WHISKERS).build().getName());
    }

    @Test
    public void getDescription_returnsCorrectDescription() {
        assertEquals(WHISKERS.getDescription(), new AnimalBuilder(WHISKERS).build().getDescription());
    }

    @Test
    public void getLocation_returnsCorrectLocation() {
        assertEquals(WHISKERS.getLocation(), new AnimalBuilder(WHISKERS).build().getLocation());
    }

    @Test
    public void getTags_returnsCorrectTags() {
        assertEquals(WHISKERS.getTags(), new AnimalBuilder(WHISKERS).build().getTags());
    }

    @Test
    public void getId_returnsNonNullId() {
        Animal animal = new AnimalBuilder().build();
        assertTrue(animal.getId() != null);
    }

    @Test
    public void getFeedingSessionIds_returnsEmptyListForNewAnimal() {
        Animal animal = new AnimalBuilder().build();
        assertTrue(animal.getFeedingSessionIds().isEmpty());
    }
}
