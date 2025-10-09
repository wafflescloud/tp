package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.AnimalName;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonName;
import seedu.address.testutil.TypicalAnimals;
import seedu.address.testutil.TypicalPersons;

public class DeletePersonCommandTest {

    private final Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validPersonName_success() {
        Person personToDelete = model.getFilteredPersonList().get(0);
        PersonName name = personToDelete.getName();
        DeletePersonCommand deletePersonCommand = new DeletePersonCommand(name);

        String expectedMessage = String.format(DeletePersonCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deletePersonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonName_throwsCommandException() {
        PersonName invalidName = new PersonName("Non Existent Name");
        DeletePersonCommand deletePersonCommand = new DeletePersonCommand(invalidName);

        assertCommandFailure(deletePersonCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_NAME);
    }

    @Test
    public void execute_validAnimalName_success() {
        // Setup animal model separately
        Model animalModel = new ModelManager(TypicalAnimals.getTypicalAddressBook(), new UserPrefs());
        Animal animalToDelete = animalModel.getFilteredAnimalList().get(0);
        AnimalName name = animalToDelete.getName();
        DeleteAnimalCommand deleteAnimalCommand = new DeleteAnimalCommand(name);

        String expectedMessage = String.format(DeleteAnimalCommand.MESSAGE_DELETED_ANIMAL_SUCCESS,
                Messages.format(animalToDelete));

        Model expectedModel = new ModelManager(animalModel.getAddressBook(), new UserPrefs());
        expectedModel.deleteAnimal(animalToDelete);

        assertCommandSuccess(deleteAnimalCommand, animalModel, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidAnimalName_throwsCommandException() {
        Model animalModel = new ModelManager(TypicalAnimals.getTypicalAddressBook(), new UserPrefs());
        AnimalName invalidName = new AnimalName("Ghost Tiger");
        DeleteAnimalCommand deleteAnimalCommand = new DeleteAnimalCommand(invalidName);

        assertCommandFailure(deleteAnimalCommand, animalModel, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_NAME);
    }

    @Test
    public void equals() {
        PersonName name1 = new PersonName("Alice Pauline");
        PersonName name2 = new PersonName("Benson Meier");

        DeletePersonCommand deleteFirstCommand = new DeletePersonCommand(name1);
        DeletePersonCommand deleteSecondCommand = new DeletePersonCommand(name2);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeletePersonCommand deleteFirstCommandCopy = new DeletePersonCommand(name1);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different name -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        PersonName name = new PersonName("Alice Pauline");
        DeletePersonCommand deletePersonCommand = new DeletePersonCommand(name);
        String expected = "seedu.address.logic.commands.DeletePersonCommand{name=Alice Pauline}";
        assertEquals(expected, deletePersonCommand.toString());
    }
}
