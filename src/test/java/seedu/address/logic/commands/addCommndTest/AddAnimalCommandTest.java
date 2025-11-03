package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalAnimals.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.animal.Animal;
import seedu.address.testutil.AnimalBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddAnimalCommand}.
 */
public class AddAnimalCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validAnimal_success() {
        Animal validAnimal = new AnimalBuilder().withName("Valid Animal")
                .withDescription("Valid description").withLocation("Valid location").build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addAnimal(validAnimal);

        AddAnimalCommand addAnimalCommand = new AddAnimalCommand(validAnimal);
        String expectedMessage = String.format(AddAnimalCommand.MESSAGE_SUCCESS, Messages.format(validAnimal));

        assertCommandSuccess(addAnimalCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateAnimal_throwsCommandException() {
        Animal animalInList = model.getFilteredAnimalList().get(0);
        AddAnimalCommand addAnimalCommand = new AddAnimalCommand(animalInList);

        assertCommandFailure(addAnimalCommand, model, AddAnimalCommand.MESSAGE_DUPLICATE_ANIMAL);
    }

    @Test
    public void equals() {
        Animal max = new AnimalBuilder().withName("Max").build();
        Animal luna = new AnimalBuilder().withName("Luna").build();
        AddAnimalCommand addMaxCommand = new AddAnimalCommand(max);
        AddAnimalCommand addLunaCommand = new AddAnimalCommand(luna);

        // same object -> returns true
        assertTrue(addMaxCommand.equals(addMaxCommand));

        // same values -> returns true
        AddAnimalCommand addMaxCommandCopy = new AddAnimalCommand(max);
        assertTrue(addMaxCommand.equals(addMaxCommandCopy));

        // different types -> returns false
        assertFalse(addMaxCommand.equals(1));

        // null -> returns false
        assertFalse(addMaxCommand.equals(null));

        // different animal -> returns false
        assertFalse(addMaxCommand.equals(addLunaCommand));
    }

    @Test
    public void toStringMethod() {
        Animal animal = new AnimalBuilder().withName("Test Animal").build();
        AddAnimalCommand addCommand = new AddAnimalCommand(animal);
        String expected = AddAnimalCommand.class.getCanonicalName() + "{toAdd=" + animal + "}";
        assertEquals(expected, addCommand.toString());
    }
}
