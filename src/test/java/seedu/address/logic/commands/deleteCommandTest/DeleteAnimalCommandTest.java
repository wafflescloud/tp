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
import seedu.address.model.UserPrefs;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.AnimalName;

/**
 * Contains integration tests (interaction with the Model) for {@code DeleteContactCommand} when deleting animals.
 */
public class DeleteAnimalCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validAnimalName_success() {
        Animal animalToDelete = WHISKERS;
        AnimalName name = (AnimalName) animalToDelete.getName();
        DeleteContactCommand<Animal> deleteAnimalCommand = DeleteContactCommand.forAnimal(name);

        String expectedMessage = String.format(Messages.MESSAGE_DELETED_ANIMAL_SUCCESS,
                Messages.format(animalToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteAnimal(animalToDelete);

        assertCommandSuccess(deleteAnimalCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidAnimalName_throwsCommandException() {
        AnimalName nonExistentName = new AnimalName("Non Existent Animal");
        DeleteContactCommand<Animal> deleteAnimalCommand = DeleteContactCommand.forAnimal(nonExistentName);

        assertCommandFailure(deleteAnimalCommand, model, Messages.MESSAGE_INVALID_ANIMAL_DISPLAYED_NAME);
    }

    @Test
    public void equals() {
        AnimalName firstAnimalName = new AnimalName("First Animal");
        AnimalName secondAnimalName = new AnimalName("Second Animal");

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
        AnimalName animalName = new AnimalName("Test Animal");
        DeleteContactCommand<Animal> deleteCommand = DeleteContactCommand.forAnimal(animalName);
        String expected = DeleteContactCommand.class.getCanonicalName() + "{name=" + animalName + "}";
        assertEquals(expected, deleteCommand.toString());
    }
}
