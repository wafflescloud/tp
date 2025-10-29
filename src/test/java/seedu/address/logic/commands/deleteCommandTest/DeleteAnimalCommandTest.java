package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalAnimals.LUNA;
import static seedu.address.testutil.TypicalAnimals.WHISKERS;
import static seedu.address.testutil.TypicalAnimals.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.AnimalName;

/**
 * Contains integration tests (interaction with the Model) for {@code DeletePersonCommand}.
 */
public class DeleteAnimalCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validAnimalName_success() {
        Animal animalToDelete = model.getFilteredAnimalList().get(0);
        AnimalName name = animalToDelete.getName();
        DeleteAnimalCommand deletePersonCommand = new DeleteAnimalCommand(name);

        String expectedMessage = String.format(Messages.MESSAGE_DELETED_ANIMAL_SUCCESS,
                Messages.format(animalToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteAnimal(animalToDelete);

        assertCommandSuccess(deletePersonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidAnimalName_throwsCommandException() {
        AnimalName nonExistentName = new AnimalName("Non Existent Name");
        DeleteAnimalCommand deleteAnimalCommand = new DeleteAnimalCommand(nonExistentName);

        assertCommandFailure(deleteAnimalCommand, model, Messages.MESSAGE_INVALID_ANIMAL_DISPLAYED_NAME);
    }

    @Test
    public void equals() {
        DeleteAnimalCommand deleteLunaCommand = new DeleteAnimalCommand(LUNA.getName());
        DeleteAnimalCommand deleteWhiskersCommand = new DeleteAnimalCommand(WHISKERS.getName());

        // same object -> returns true
        assertTrue(deleteLunaCommand.equals(deleteLunaCommand));

        // same values -> returns true
        DeleteAnimalCommand deleteLunaCommandCopy = new DeleteAnimalCommand(LUNA.getName());
        assertTrue(deleteLunaCommand.equals(deleteLunaCommandCopy));

        // different types -> returns false
        assertFalse(deleteLunaCommand.equals(1));

        // null -> returns false
        assertFalse(deleteLunaCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteLunaCommand.equals(deleteWhiskersCommand));
    }

    @Test
    public void toStringMethod() {
        DeleteAnimalCommand deleteCommand = new DeleteAnimalCommand(LUNA.getName());
        String expected = DeleteAnimalCommand.class.getCanonicalName() + "{name=" + LUNA.getName() + "}";
        assertEquals(expected, deleteCommand.toString());
    }
}
