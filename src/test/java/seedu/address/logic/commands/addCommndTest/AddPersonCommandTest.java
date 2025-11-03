package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddPersonCommand}.
 */
public class AddPersonCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validPerson_success() {
        Person validPerson = new PersonBuilder().withName("Valid Person")
                .withPhone("91234567").withEmail("valid@example.com").build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        AddPersonCommand addPersonCommand = new AddPersonCommand(validPerson);
        String expectedMessage = String.format(AddPersonCommand.MESSAGE_SUCCESS, Messages.format(validPerson));

        assertCommandSuccess(addPersonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getFilteredPersonList().get(0);
        AddPersonCommand addPersonCommand = new AddPersonCommand(personInList);

        assertCommandFailure(addPersonCommand, model, AddPersonCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePhone_throwsCommandException() {
        Person personInList = model.getFilteredPersonList().get(0);
        Person personWithDuplicatePhone = new PersonBuilder().withName("Different Name")
                .withPhone(personInList.getPhone().value).withEmail("different@example.com").build();

        AddPersonCommand addPersonCommand = new AddPersonCommand(personWithDuplicatePhone);

        assertCommandFailure(addPersonCommand, model, AddPersonCommand.MESSAGE_DUPLICATE_PHONE_NUMBER);
    }

    @Test
    public void execute_duplicateEmail_throwsCommandException() {
        Person personInList = model.getFilteredPersonList().get(0);
        Person personWithDuplicateEmail = new PersonBuilder().withName("Different Name")
                .withPhone("91234567").withEmail(personInList.getEmail().value).build();

        AddPersonCommand addPersonCommand = new AddPersonCommand(personWithDuplicateEmail);

        assertCommandFailure(addPersonCommand, model, AddPersonCommand.MESSAGE_DUPLICATE_EMAIL);
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddPersonCommand addAliceCommand = new AddPersonCommand(alice);
        AddPersonCommand addBobCommand = new AddPersonCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddPersonCommand addAliceCommandCopy = new AddPersonCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        Person person = new PersonBuilder().withName("Test Person").build();
        AddPersonCommand addCommand = new AddPersonCommand(person);
        String expected = AddPersonCommand.class.getCanonicalName() + "{toAdd=" + person + "}";
        assertEquals(expected, addCommand.toString());
    }
}
