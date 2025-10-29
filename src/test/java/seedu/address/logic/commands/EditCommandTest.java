package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.DESC_CHOCO;
import static seedu.address.logic.commands.CommandTestUtil.DESC_KITTY;
// import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_CHOCO;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_KITTY;
// import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
// import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditAnimalCommand.EditAnimalDescriptor;
import seedu.address.logic.commands.EditPersonCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.animal.AnimalName;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonName;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * * Contains integration tests (interaction with the Model) and unit tests for EditPersonCommand.
 */
public class EditCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        // Build a descriptor with new values using a fresh Person
        Person descriptorSource = new PersonBuilder().build();
        PersonName personName = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getName();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(descriptorSource).build();
        EditPersonCommand editCommand = new EditPersonCommand(personName, descriptor);

        // Preserve the original person's ID when constructing the expected Person
        Person original = model.getFilteredPersonList().get(0);
        Person expectedEdited = new Person(
                original.getId(),
                descriptorSource.getName(),
                descriptorSource.getPhone(),
                descriptorSource.getEmail(),
                descriptorSource.getTags(),
                original.getFeedingSessionIds());

        String expectedMessage = String.format(EditPersonCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(expectedEdited));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(original, expectedEdited);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    // @Test
    // public void execute_someFieldsSpecifiedUnfilteredList_success() {
    //     Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
    //     Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());
    //     PersonName lastPersonName = lastPerson.getName();

    //     PersonBuilder personInList = new PersonBuilder(lastPerson);
    //     Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
    //             .withTags(VALID_TAG_HUSBAND).build();

    //     EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
    //             .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
    //     EditPersonCommand editCommand = new EditPersonCommand(lastPersonName, descriptor);

    //     String expectedMessage = String.format(EditPersonCommand.MESSAGE_EDIT_PERSON_SUCCESS,
    //             Messages.format(editedPerson));

    //     Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
    //     expectedModel.setPerson(lastPerson, editedPerson);

    //     assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    // }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        PersonName personName = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getName();

        EditPersonCommand editCommand = new EditPersonCommand(personName, new EditPersonDescriptor());
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditPersonCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    // @Test
    // public void execute_filteredList_success() {
    //     showPersonAtIndex(model, INDEX_FIRST_PERSON);

    //     Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
    //     Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
    //     PersonName personName = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getName();

    //     EditPersonCommand editCommand = new EditPersonCommand(personName,
    //             new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

    //     String expectedMessage = String.format(EditPersonCommand.MESSAGE_EDIT_PERSON_SUCCESS,
    //             Messages.format(editedPerson));

    //     Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
    //     expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

    //     assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    // }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();

        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        PersonName secondPersonName = secondPerson.getName();
        EditPersonCommand editCommand = new EditPersonCommand(secondPersonName, descriptor);

        assertCommandFailure(editCommand, model, EditPersonCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PersonName personName = person.getName();
        EditPersonCommand editCommand = new EditPersonCommand(personName,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, EditPersonCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void equals_person() {
        PersonName firstPersonName = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getName();
        PersonName secondPersonName = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased())
                .getName();
        final EditPersonCommand standardCommand = new EditPersonCommand(firstPersonName, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditPersonCommand commandWithSameValues = new EditPersonCommand(firstPersonName, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditPersonCommand(secondPersonName, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditPersonCommand(secondPersonName, DESC_BOB)));
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
    public void toStringMethod_person() {
        Index index = Index.fromOneBased(1);
        PersonName personName = model.getFilteredPersonList().get(index.getZeroBased()).getName();
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditPersonCommand editCommand = new EditPersonCommand(personName, editPersonDescriptor);
        String expected = EditPersonCommand.class.getCanonicalName() + "{name=" + personName
                + ", editPersonDescriptor=" + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
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
