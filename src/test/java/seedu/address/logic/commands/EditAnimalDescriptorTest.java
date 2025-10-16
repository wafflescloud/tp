package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_CHOCO;
import static seedu.address.logic.commands.CommandTestUtil.DESC_KITTY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_KITTY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LOCATION_KITTY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_KITTY;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditAnimalCommand.EditAnimalDescriptor;
import seedu.address.testutil.EditAnimalDescriptorBuilder;

public class EditAnimalDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditAnimalDescriptor descriptorWithSameValues = new EditAnimalDescriptor(DESC_CHOCO);
        assertTrue(DESC_CHOCO.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_CHOCO.equals(DESC_CHOCO));

        // null -> returns false
        assertFalse(DESC_CHOCO.equals(null));

        // different types -> returns false
        assertFalse(DESC_CHOCO.equals(5));

        // different values -> returns false
        assertFalse(DESC_CHOCO.equals(DESC_KITTY));

        // different name -> returns false
        EditAnimalDescriptor editedChoco = new EditAnimalDescriptorBuilder(DESC_CHOCO)
                .withName(VALID_NAME_KITTY).build();
        assertFalse(DESC_CHOCO.equals(editedChoco));

        // different description -> returns false
        editedChoco = new EditAnimalDescriptorBuilder(DESC_CHOCO).withDescription(VALID_DESCRIPTION_KITTY).build();
        assertFalse(DESC_CHOCO.equals(editedChoco));

        // different location -> returns false
        editedChoco = new EditAnimalDescriptorBuilder(DESC_CHOCO).withLocation(VALID_LOCATION_KITTY).build();
        assertFalse(DESC_CHOCO.equals(editedChoco));
    }

    @Test
    public void toStringMethod() {
        EditAnimalDescriptor editAnimalDescriptor = new EditAnimalDescriptor();
        String expected = EditAnimalDescriptor.class.getCanonicalName() + "{name="
                + editAnimalDescriptor.getName().orElse(null) + ", description="
                + editAnimalDescriptor.getDescription().orElse(null) + ", location="
                + editAnimalDescriptor.getLocation().orElse(null) + "}";
        assertEquals(expected, editAnimalDescriptor.toString());
    }
}
