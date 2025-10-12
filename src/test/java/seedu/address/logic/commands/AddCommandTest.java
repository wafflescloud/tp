package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.animal.Animal;
import seedu.address.model.person.Person;
import seedu.address.testutil.AnimalBuilder;
import seedu.address.testutil.PersonBuilder;

public class AddCommandTest {

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddPersonCommand(null));
    }

    @Test
    public void constructor_nullAnimal_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddAnimalCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder().build();

        CommandResult commandResult = new AddPersonCommand(validPerson).execute(modelStub);

        assertEquals(String.format(AddPersonCommand.MESSAGE_SUCCESS, Messages.format(validPerson)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_animalAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingAnimalAdded modelStub = new ModelStubAcceptingAnimalAdded();
        Animal validAnimal = new AnimalBuilder().build();

        CommandResult commandResult = new AddAnimalCommand(validAnimal).execute(modelStub);

        assertEquals(String.format(AddAnimalCommand.MESSAGE_SUCCESS, Messages.format(validAnimal)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validAnimal), modelStub.animalsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        AddPersonCommand addCommand = new AddPersonCommand(validPerson);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class, AddPersonCommand.MESSAGE_DUPLICATE_PERSON, (
                ) -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicateAnimal_throwsCommandException() {
        Animal validAnimal = new AnimalBuilder().build();
        AddAnimalCommand addCommand = new AddAnimalCommand(validAnimal);
        ModelStub modelStub = new ModelStubWithAnimal(validAnimal);

        assertThrows(CommandException.class, AddAnimalCommand.MESSAGE_DUPLICATE_ANIMAL, (
                ) -> addCommand.execute(modelStub));
    }

    @Test
    public void equals_person() {
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
    public void equals_animal() {
        Animal max = new AnimalBuilder().withName("Max").build();
        Animal bella = new AnimalBuilder().withName("Bella").build();
        AddAnimalCommand addMaxCommand = new AddAnimalCommand(max);
        AddAnimalCommand addBellaCommand = new AddAnimalCommand(bella);

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
        assertFalse(addMaxCommand.equals(addBellaCommand));
    }

    @Test
    public void toStringMethod_person() {
        Person validPerson = new PersonBuilder().withName("Alice").build();
        AddPersonCommand command = new AddPersonCommand(validPerson);
        String expected = AddPersonCommand.class.getCanonicalName() + "{toAdd=" + validPerson + "}";
        assertEquals(expected, command.toString());
    }

    @Test
    public void toStringMethod_animal() {
        Animal validAnimal = new AnimalBuilder().withName("Max").build();
        AddAnimalCommand command = new AddAnimalCommand(validAnimal);
        String expected = AddAnimalCommand.class.getCanonicalName() + "{toAdd=" + validAnimal + "}";
        assertEquals(expected, command.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasAnimal(Animal animal) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteAnimal(Animal target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addAnimal(Animal animal) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAnimal(Animal target, Animal editedAnimal) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Animal> getFilteredAnimalList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredAnimalList(Predicate<Animal> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that contains a single animal.
     */
    private class ModelStubWithAnimal extends ModelStub {
        private final Animal animal;

        ModelStubWithAnimal(Animal animal) {
            requireNonNull(animal);
            this.animal = animal;
        }

        @Override
        public boolean hasAnimal(Animal animal) {
            requireNonNull(animal);
            return this.animal.isSameAnimal(animal);
        }
    }

    /**
     * A Model stub that always accept the animal being added.
     */
    private class ModelStubAcceptingAnimalAdded extends ModelStub {
        final ArrayList<Animal> animalsAdded = new ArrayList<>();

        @Override
        public boolean hasAnimal(Animal animal) {
            requireNonNull(animal);
            return animalsAdded.stream().anyMatch(animal::isSameAnimal);
        }

        @Override
        public void addAnimal(Animal animal) {
            requireNonNull(animal);
            animalsAdded.add(animal);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
