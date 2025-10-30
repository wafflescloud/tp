package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalAnimals.WHISKERS;
import static seedu.address.testutil.TypicalAnimals.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.Name;
import seedu.address.model.UserPrefs;
import seedu.address.model.animal.Animal;

/**
 * Contains integration tests (interaction with the Model) for {@code DeleteContactCommand} when deleting animals.
 */
public class DeleteAnimalCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validAnimalName_success() {
        Animal animalToDelete = WHISKERS;
        Name name = animalToDelete.getName();
        DeleteContactCommand<Animal> deleteAnimalCommand = DeleteContactCommand.forAnimal(name);

        String expectedMessage = String.format(Messages.MESSAGE_DELETED_ANIMAL_SUCCESS,
                Messages.format(animalToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteAnimal(animalToDelete);

        assertCommandSuccess(deleteAnimalCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidAnimalName_throwsCommandException() {
        Name nonExistentName = new Name("Non Existent Animal");
        DeleteContactCommand<Animal> deleteAnimalCommand = DeleteContactCommand.forAnimal(nonExistentName);

        assertCommandFailure(deleteAnimalCommand, model, Messages.MESSAGE_INVALID_ANIMAL_DISPLAYED_NAME);
    }

    @Test
    public void equals() {
        Name firstAnimalName = new Name("First Animal");
        Name secondAnimalName = new Name("Second Animal");

        DeleteContactCommand<Animal> deleteFirstCommand = DeleteContactCommand.forAnimal(firstAnimalName);
        DeleteContactCommand<Animal> deleteSecondCommand = DeleteContactCommand.forAnimal(secondAnimalName);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteContactCommand<Animal> deleteFirstCommandCopy = DeleteContactCommand.forAnimal(firstAnimalName);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different animal -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Name animalName = new Name("Test Animal");
        DeleteContactCommand<Animal> deleteCommand = DeleteContactCommand.forAnimal(animalName);
        String expected = DeleteContactCommand.class.getCanonicalName() + "{name=" + animalName + "}";
        assertEquals(expected, deleteCommand.toString());
    }
}
