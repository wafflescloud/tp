package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.animal.Animal;
import seedu.address.model.person.Person;
import seedu.address.testutil.AnimalBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        assertCommandSuccess(new AddPersonCommand(validPerson), model,
                String.format(AddPersonCommand.MESSAGE_SUCCESS, Messages.format(validPerson)),
                expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        assertCommandFailure(new AddPersonCommand(personInList), model,
                AddPersonCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_newAnimal_success() {
        Animal validAnimal = new AnimalBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addAnimal(validAnimal);

        assertCommandSuccess(new AddAnimalCommand(validAnimal), model,
                String.format(AddAnimalCommand.MESSAGE_SUCCESS, Messages.format(validAnimal)),
                expectedModel);
    }

    @Test
    public void execute_duplicateAnimal_throwsCommandException() {
        Animal validAnimal = new AnimalBuilder().build();
        model.addAnimal(validAnimal);

        assertCommandFailure(new AddAnimalCommand(validAnimal), model,
                AddAnimalCommand.MESSAGE_DUPLICATE_ANIMAL);
    }
}
