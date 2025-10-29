package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_CHOCO;
import static seedu.address.logic.commands.CommandTestUtil.DESC_KITTY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_KITTY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LOCATION_KITTY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_CHOCO;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_KITTY;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showAnimalAtIndex;
import static seedu.address.testutil.TypicalAnimals.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ANIMAL;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ANIMAL;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditAnimalCommand.EditAnimalDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.AnimalName;
import seedu.address.testutil.AnimalBuilder;
import seedu.address.testutil.EditAnimalDescriptorBuilder;

/**
 * * Contains integration tests (interaction with the Model) and unit tests for EditPersonCommand.
 */
public class EditAnimalCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        // Build a descriptor with new values using a fresh Animal
        Animal descriptorSource = new AnimalBuilder().build();
        AnimalName animalName = model.getFilteredAnimalList().get(0).getName();
        EditAnimalDescriptor descriptor = new EditAnimalDescriptorBuilder(descriptorSource).build();
        EditAnimalCommand editCommand = new EditAnimalCommand(animalName, descriptor);

        // Preserve the original animal's ID when constructing the expected animal
        Animal original = model.getFilteredAnimalList().get(0);
        Animal expectedEdited = new Animal(
                original.getId(),
                descriptorSource.getName(),
                descriptorSource.getDescription(),
                descriptorSource.getLocation(),
                descriptorSource.getTags(),
                original.getFeedingSessionIds());

        String expectedMessage = String.format(EditAnimalCommand.MESSAGE_EDIT_ANIMAL_SUCCESS,
                Messages.format(expectedEdited));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setAnimal(original, expectedEdited);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastAnimal = Index.fromOneBased(model.getFilteredAnimalList().size());
        Animal lastAnimal = model.getFilteredAnimalList().get(indexLastAnimal.getZeroBased());
        AnimalName lastAnimalName = lastAnimal.getName();

        AnimalBuilder animalInList = new AnimalBuilder(lastAnimal);
        Animal editedAnimal = animalInList.withName(VALID_NAME_KITTY)
                                          .withDescription(VALID_DESCRIPTION_KITTY)
                                          .withLocation(VALID_LOCATION_KITTY)
                                          .build();

        EditAnimalDescriptor descriptor = new EditAnimalDescriptorBuilder().withName(VALID_NAME_KITTY)
                .withDescription(VALID_DESCRIPTION_KITTY).withLocation(VALID_LOCATION_KITTY).build();
        EditAnimalCommand editCommand = new EditAnimalCommand(lastAnimalName, descriptor);

        String expectedMessage = String.format(EditAnimalCommand.MESSAGE_EDIT_ANIMAL_SUCCESS,
                Messages.format(editedAnimal));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setAnimal(lastAnimal, editedAnimal);
        System.out.println(model.getFilteredAnimalList());
        System.out.println(expectedModel.getFilteredAnimalList());
        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        AnimalName animalName = model.getFilteredAnimalList().get(0).getName();

        EditAnimalCommand editCommand = new EditAnimalCommand(animalName, new EditAnimalDescriptor());
        Animal editedAnimal = model.getFilteredAnimalList().get(0);

        String expectedMessage = String.format(EditAnimalCommand.MESSAGE_EDIT_ANIMAL_SUCCESS,
                Messages.format(editedAnimal));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showAnimalAtIndex(model, INDEX_FIRST_ANIMAL);

        Animal animalInFilteredList = model.getFilteredAnimalList().get(INDEX_FIRST_ANIMAL.getZeroBased());
        Animal editedAnimal = new AnimalBuilder(animalInFilteredList).withName(VALID_NAME_KITTY).build();
        AnimalName animalName = model.getFilteredAnimalList().get(INDEX_FIRST_ANIMAL.getZeroBased()).getName();

        EditAnimalCommand editCommand = new EditAnimalCommand(animalName,
                new EditAnimalDescriptorBuilder().withName(VALID_NAME_KITTY).build());

        String expectedMessage = String.format(EditAnimalCommand.MESSAGE_EDIT_ANIMAL_SUCCESS,
                Messages.format(editedAnimal));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setAnimal(model.getFilteredAnimalList().get(0), editedAnimal);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateAnimalUnfilteredList_failure() {
        Animal firstAnimal = model.getFilteredAnimalList().get(INDEX_FIRST_ANIMAL.getZeroBased());
        EditAnimalDescriptor descriptor = new EditAnimalDescriptorBuilder(firstAnimal).build();

        Animal secondAnimal = model.getFilteredAnimalList().get(INDEX_SECOND_ANIMAL.getZeroBased());
        AnimalName secondAnimalName = secondAnimal.getName();
        EditAnimalCommand editCommand = new EditAnimalCommand(secondAnimalName, descriptor);

        assertCommandFailure(editCommand, model, EditAnimalCommand.MESSAGE_DUPLICATE_ANIMAL);
    }

    @Test
    public void execute_duplicateAnimalFilteredList_failure() {
        showAnimalAtIndex(model, INDEX_FIRST_ANIMAL);

        // edit animal in filtered list into a duplicate in address book
        Animal animalInList = model.getAddressBook().getAnimalList().get(INDEX_SECOND_ANIMAL.getZeroBased());
        Animal animal = model.getFilteredAnimalList().get(INDEX_FIRST_ANIMAL.getZeroBased());
        AnimalName animalName = animal.getName();
        EditAnimalCommand editCommand = new EditAnimalCommand(animalName,
                new EditAnimalDescriptorBuilder(animalInList).build());

        assertCommandFailure(editCommand, model, EditAnimalCommand.MESSAGE_DUPLICATE_ANIMAL);
    }

    @Test
    public void equals_animal() {
        AnimalName firstAnimalName = new AnimalName(VALID_NAME_CHOCO);
        AnimalName secondAnimalName = new AnimalName(VALID_NAME_KITTY);

        final EditAnimalCommand standardCommand = new EditAnimalCommand(firstAnimalName, DESC_CHOCO);

        // same values -> returns true
        EditAnimalDescriptor copyDescriptor = new EditAnimalDescriptor(DESC_CHOCO);
        EditAnimalCommand commandWithSameValues = new EditAnimalCommand(firstAnimalName, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditAnimalCommand(secondAnimalName, DESC_CHOCO)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditAnimalCommand(secondAnimalName, DESC_KITTY)));
    }

    @Test
    public void toStringMethod_animal() {
        AnimalName animalName = new AnimalName(VALID_NAME_CHOCO);
        EditAnimalDescriptor editAnimalDescriptor = new EditAnimalDescriptor();
        EditAnimalCommand editCommand = new EditAnimalCommand(animalName, editAnimalDescriptor);
        String expected = EditAnimalCommand.class.getCanonicalName() + "{name=" + animalName
                + ", editAnimalDescriptor=" + editAnimalDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
