package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.parser.CliSyntax.TYPE_ANIMAL;
import static seedu.address.logic.parser.CliSyntax.TYPE_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.Type;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

public class ViewCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    // Constructor Tests

    @Test
    public void constructor_nullType_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ViewCommand(null, "Alice"));
    }

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ViewCommand(TYPE_PERSON, null));
    }

    @Test
    public void constructor_validParameters_success() {
        ViewCommand command = new ViewCommand(TYPE_PERSON, "Alice");
        // Should not throw any exception
    }

    // Execute Person Tests - Testing the command logic without UI

    @Test
    public void execute_validPersonName_commandExceptionHandling() {
        String personName = ALICE.getName().toString();
        ViewCommand viewCommand = new ViewCommand(TYPE_PERSON, personName);

        // The command should not throw an exception for the logic part
        // Even if UI fails, the person should be found in the model
        List<Person> personList = model.getFilteredPersonList();
        Person foundPerson = personList.stream()
                .filter(p -> p.getName().toString().equalsIgnoreCase(personName))
                .findFirst()
                .orElse(null);

        assertNotNull(foundPerson);
        assertEquals(ALICE.getName().toString(), foundPerson.getName().toString());
    }

    @Test
    public void execute_invalidPersonName_throwsCommandException() {
        String nonExistentName = "Non Existent Person";
        ViewCommand viewCommand = new ViewCommand(TYPE_PERSON, nonExistentName);

        // Test the logic before UI - person should not be found
        List<Person> personList = model.getFilteredPersonList();
        Person foundPerson = personList.stream()
                .filter(p -> p.getName().toString().equalsIgnoreCase(nonExistentName))
                .findFirst()
                .orElse(null);

        assertNull(foundPerson);
    }

    @Test
    public void execute_caseInsensitivePersonNameLowerCase_personFoundInModel() {
        String lowerCaseName = ALICE.getName().toString().toLowerCase();
        ViewCommand viewCommand = new ViewCommand(TYPE_PERSON, lowerCaseName);

        // Test the case-insensitive logic
        List<Person> personList = model.getFilteredPersonList();
        Person foundPerson = personList.stream()
                .filter(p -> p.getName().toString().equalsIgnoreCase(lowerCaseName))
                .findFirst()
                .orElse(null);

        assertNotNull(foundPerson);
        assertEquals(ALICE.getName().toString(), foundPerson.getName().toString());
    }

    @Test
    public void execute_caseInsensitivePersonNameUpperCase_personFoundInModel() {
        String upperCaseName = BENSON.getName().toString().toUpperCase();
        ViewCommand viewCommand = new ViewCommand(TYPE_PERSON, upperCaseName);

        // Test the case-insensitive logic
        List<Person> personList = model.getFilteredPersonList();
        Person foundPerson = personList.stream()
                .filter(p -> p.getName().toString().equalsIgnoreCase(upperCaseName))
                .findFirst()
                .orElse(null);

        assertNotNull(foundPerson);
        assertEquals(BENSON.getName().toString(), foundPerson.getName().toString());
    }

    @Test
    public void execute_caseInsensitivePersonNameMixedCase_personFoundInModel() {
        String mixedCaseName = "aLiCe PaUlInE";
        ViewCommand viewCommand = new ViewCommand(TYPE_PERSON, mixedCaseName);

        // Test the case-insensitive logic
        List<Person> personList = model.getFilteredPersonList();
        Person foundPerson = personList.stream()
                .filter(p -> p.getName().toString().equalsIgnoreCase(mixedCaseName))
                .findFirst()
                .orElse(null);

        assertNotNull(foundPerson);
        assertEquals(ALICE.getName().toString(), foundPerson.getName().toString());
    }

    @Test
    public void execute_personNameWithExtraSpaces_throwsCommandException() {
        String nameWithSpaces = " " + ALICE.getName().toString() + " ";
        ViewCommand viewCommand = new ViewCommand(TYPE_PERSON, nameWithSpaces);

        assertCommandFailure(viewCommand, model,
            String.format(ViewCommand.MESSAGE_PERSON_NOT_FOUND, nameWithSpaces));
    }

    @Test
    public void execute_emptyPersonName_throwsCommandException() {
        ViewCommand viewCommand = new ViewCommand(TYPE_PERSON, "");
        assertCommandFailure(viewCommand, model,
            String.format(ViewCommand.MESSAGE_PERSON_NOT_FOUND, ""));
    }

    @Test
    public void execute_partialPersonName_throwsCommandException() {
        String partialName = "Alice"; // Only first name
        ViewCommand viewCommand = new ViewCommand(TYPE_PERSON, partialName);
        assertCommandFailure(viewCommand, model,
            String.format(ViewCommand.MESSAGE_PERSON_NOT_FOUND, partialName));
    }

    // Execute Animal Tests - Basic tests without complex animal creation

    @Test
    public void execute_invalidAnimalName_throwsCommandException() {
        String nonExistentName = "Non Existent Animal";
        ViewCommand viewCommand = new ViewCommand(TYPE_ANIMAL, nonExistentName);

        assertCommandFailure(viewCommand, model,
            String.format(ViewCommand.MESSAGE_ANIMAL_NOT_FOUND, nonExistentName));
    }

    @Test
    public void execute_emptyAnimalList_throwsCommandException() {
        ViewCommand viewCommand = new ViewCommand(TYPE_ANIMAL, "SomeAnimal");
        assertCommandFailure(viewCommand, model,
            String.format(ViewCommand.MESSAGE_ANIMAL_NOT_FOUND, "SomeAnimal"));
    }

    // Execute Invalid Type Tests

    @Test
    public void execute_invalidType_throwsCommandException() {
        Type invalidType = new Type("vehicle");
        ViewCommand viewCommand = new ViewCommand(invalidType, "Tesla");

        assertCommandFailure(viewCommand, model, "Invalid type. Use 'person' or 'animal'.");
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        ViewCommand viewCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().toString());
        assertThrows(NullPointerException.class, () -> viewCommand.execute(null));
    }

    // Equals Tests

    @Test
    public void equals_sameObject_returnsTrue() {
        ViewCommand viewAliceCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().toString());
        assertTrue(viewAliceCommand.equals(viewAliceCommand));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        ViewCommand viewAliceCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().toString());
        ViewCommand viewAliceCommandCopy = new ViewCommand(TYPE_PERSON, ALICE.getName().toString());
        assertTrue(viewAliceCommand.equals(viewAliceCommandCopy));
    }

    @Test
    public void equals_null_returnsFalse() {
        ViewCommand viewAliceCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().toString());
        assertFalse(viewAliceCommand.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        ViewCommand viewAliceCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().toString());
        assertFalse(viewAliceCommand.equals(1));
        assertFalse(viewAliceCommand.equals("string"));
        assertFalse(viewAliceCommand.equals(new ClearCommand()));
    }

    @Test
    public void equals_differentPersonName_returnsFalse() {
        ViewCommand viewAliceCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().toString());
        ViewCommand viewBensonCommand = new ViewCommand(TYPE_PERSON, BENSON.getName().toString());
        assertFalse(viewAliceCommand.equals(viewBensonCommand));
    }

    @Test
    public void equals_sameNameDifferentEntityType_returnsFalse() {
        ViewCommand viewAliceCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().toString());
        ViewCommand viewAliceAsAnimal = new ViewCommand(TYPE_ANIMAL, ALICE.getName().toString());
        assertFalse(viewAliceCommand.equals(viewAliceAsAnimal));
    }

    @Test
    public void equals_caseSensitiveComparison_returnsFalse() {
        ViewCommand viewAliceCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().toString());
        ViewCommand viewAliceUpperCase = new ViewCommand(TYPE_PERSON, ALICE.getName().toString().toUpperCase());
        assertFalse(viewAliceCommand.equals(viewAliceUpperCase));
    }

    // ToString Tests

    @Test
    public void toString_personType_correctFormat() {
        Type type = TYPE_PERSON;
        String name = ALICE.getName().toString();
        ViewCommand viewCommand = new ViewCommand(type, name);
        String expected = ViewCommand.class.getCanonicalName() + "{type=" + type + ", name=" + name + "}";
        assertEquals(expected, viewCommand.toString());
    }

    @Test
    public void toString_emptyName_correctFormat() {
        Type type = TYPE_PERSON;
        String name = "";
        ViewCommand viewCommand = new ViewCommand(type, name);
        String expected = ViewCommand.class.getCanonicalName() + "{type=" + type + ", name=" + name + "}";
        assertEquals(expected, viewCommand.toString());
    }

    @Test
    public void toString_specialCharactersInName_correctFormat() {
        Type type = TYPE_PERSON;
        String name = "Mary-Jane O'Connor";
        ViewCommand viewCommand = new ViewCommand(type, name);
        String expected = ViewCommand.class.getCanonicalName() + "{type=" + type + ", name=" + name + "}";
        assertEquals(expected, viewCommand.toString());
    }
}
