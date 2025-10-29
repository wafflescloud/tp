package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.parser.CliSyntax.TYPE_ANIMAL;
import static seedu.address.logic.parser.CliSyntax.TYPE_PERSON;
import static seedu.address.testutil.TypicalAnimals.BELLA;
import static seedu.address.testutil.TypicalAnimals.FLUFFY;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.Type;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

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

    // Execute Person Tests

    // @Test
    // public void execute_validPersonName_success() {
    //     String personName = ALICE.getName().fullName;
    //     ViewCommand viewCommand = new ViewCommand(TYPE_PERSON, personName);

    //     String expectedMessage = String.format(ViewCommand.MESSAGE_VIEW_PERSON_SUCCESS, personName);
    //     assertCommandSuccess(viewCommand, model, expectedMessage, expectedModel);
    // }

    @Test
    public void execute_invalidPersonName_throwsCommandException() {
        String nonExistentName = "Non Existent Person";
        ViewCommand viewCommand = new ViewCommand(TYPE_PERSON, nonExistentName);

        assertCommandFailure(viewCommand, model,
            String.format(ViewCommand.MESSAGE_PERSON_NOT_FOUND, nonExistentName));
    }

    // @Test
    // public void execute_caseInsensitivePersonNameLowerCase_success() {
    //     String lowerCaseName = ALICE.getName().fullName.toLowerCase();
    //     ViewCommand viewCommand = new ViewCommand(TYPE_PERSON, lowerCaseName);
    //     String expectedMessage = String.format(ViewCommand.MESSAGE_VIEW_PERSON_SUCCESS,
    //         ALICE.getName().fullName);
    //     assertCommandSuccess(viewCommand, model, expectedMessage, expectedModel);
    // }

    // @Test
    // public void execute_caseInsensitivePersonNameUpperCase_success() {
    //     String upperCaseName = BENSON.getName().fullName.toUpperCase();
    //     ViewCommand viewCommand = new ViewCommand(TYPE_PERSON, upperCaseName);
    //     String expectedMessage = String.format(ViewCommand.MESSAGE_VIEW_PERSON_SUCCESS,
    //         BENSON.getName().fullName);
    //     assertCommandSuccess(viewCommand, model, expectedMessage, expectedModel);
    // }

    // @Test
    // public void execute_caseInsensitivePersonNameMixedCase_success() {
    //     String mixedCaseName = "aLiCe PaUlInE";
    //     ViewCommand viewCommand = new ViewCommand(TYPE_PERSON, mixedCaseName);
    //     String expectedMessage = String.format(ViewCommand.MESSAGE_VIEW_PERSON_SUCCESS,
    //         ALICE.getName().fullName);
    //     assertCommandSuccess(viewCommand, model, expectedMessage, expectedModel);
    // }

    @Test
    public void execute_personNameWithExtraSpaces_throwsCommandException() {
        String nameWithSpaces = " " + ALICE.getName().fullName + " ";
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

    // Execute Animal Tests

    // @Test
    // public void execute_validAnimalName_success() {
    //     model.addAnimal(BELLA);
    //     expectedModel.addAnimal(BELLA);

    //     String animalName = BELLA.getName().fullName;
    //     ViewCommand viewCommand = new ViewCommand(TYPE_ANIMAL, animalName);

    //     String expectedMessage = String.format(ViewCommand.MESSAGE_VIEW_ANIMAL_SUCCESS, animalName);
    //     assertCommandSuccess(viewCommand, model, expectedMessage, expectedModel);
    // }

    @Test
    public void execute_invalidAnimalName_throwsCommandException() {
        String nonExistentName = "Non Existent Animal";
        ViewCommand viewCommand = new ViewCommand(TYPE_ANIMAL, nonExistentName);

        assertCommandFailure(viewCommand, model,
            String.format(ViewCommand.MESSAGE_ANIMAL_NOT_FOUND, nonExistentName));
    }

    // @Test
    // public void execute_caseInsensitiveAnimalNameLowerCase_success() {
    //     model.addAnimal(BELLA);
    //     expectedModel.addAnimal(BELLA);

    //     String lowerCaseName = BELLA.getName().fullName.toLowerCase();
    //     ViewCommand viewCommand = new ViewCommand(TYPE_ANIMAL, lowerCaseName);
    //     String expectedMessage = String.format(ViewCommand.MESSAGE_VIEW_ANIMAL_SUCCESS,
    //         BELLA.getName().fullName);
    //     assertCommandSuccess(viewCommand, model, expectedMessage, expectedModel);
    // }

    // @Test
    // public void execute_caseInsensitiveAnimalNameUpperCase_success() {
    //     model.addAnimal(FLUFFY);
    //     expectedModel.addAnimal(FLUFFY);

    //     String upperCaseName = FLUFFY.getName().fullName.toUpperCase();
    //     ViewCommand viewCommand = new ViewCommand(TYPE_ANIMAL, upperCaseName);
    //     String expectedMessage = String.format(ViewCommand.MESSAGE_VIEW_ANIMAL_SUCCESS,
    //         FLUFFY.getName().fullName);
    //     assertCommandSuccess(viewCommand, model, expectedMessage, expectedModel);
    // }

    @Test
    public void execute_emptyAnimalList_throwsCommandException() {
        ViewCommand viewCommand = new ViewCommand(TYPE_ANIMAL, BELLA.getName().fullName);
        assertCommandFailure(viewCommand, model,
            String.format(ViewCommand.MESSAGE_ANIMAL_NOT_FOUND, BELLA.getName().fullName));
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
        ViewCommand viewCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().fullName);
        assertThrows(NullPointerException.class, () -> viewCommand.execute(null));
    }

    // Equals Tests

    @Test
    public void equals_sameObject_returnsTrue() {
        ViewCommand viewAliceCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().fullName);
        assertTrue(viewAliceCommand.equals(viewAliceCommand));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        ViewCommand viewAliceCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().fullName);
        ViewCommand viewAliceCommandCopy = new ViewCommand(TYPE_PERSON, ALICE.getName().fullName);
        assertTrue(viewAliceCommand.equals(viewAliceCommandCopy));
    }

    @Test
    public void equals_null_returnsFalse() {
        ViewCommand viewAliceCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().fullName);
        assertFalse(viewAliceCommand.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        ViewCommand viewAliceCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().fullName);
        assertFalse(viewAliceCommand.equals(1));
        assertFalse(viewAliceCommand.equals("string"));
        assertFalse(viewAliceCommand.equals(new ClearCommand()));
    }

    @Test
    public void equals_differentPersonName_returnsFalse() {
        ViewCommand viewAliceCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().fullName);
        ViewCommand viewBensonCommand = new ViewCommand(TYPE_PERSON, BENSON.getName().fullName);
        assertFalse(viewAliceCommand.equals(viewBensonCommand));
    }

    @Test
    public void equals_differentEntityType_returnsFalse() {
        ViewCommand viewAliceCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().fullName);
        ViewCommand viewBellaCommand = new ViewCommand(TYPE_ANIMAL, BELLA.getName().fullName);
        assertFalse(viewAliceCommand.equals(viewBellaCommand));
    }

    @Test
    public void equals_sameNameDifferentEntityType_returnsFalse() {
        ViewCommand viewAliceCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().fullName);
        ViewCommand viewAliceAsAnimal = new ViewCommand(TYPE_ANIMAL, ALICE.getName().fullName);
        assertFalse(viewAliceCommand.equals(viewAliceAsAnimal));
    }

    @Test
    public void equals_caseSensitiveComparison_returnsFalse() {
        ViewCommand viewAliceCommand = new ViewCommand(TYPE_PERSON, ALICE.getName().fullName);
        ViewCommand viewAliceUpperCase = new ViewCommand(TYPE_PERSON, ALICE.getName().fullName.toUpperCase());
        assertFalse(viewAliceCommand.equals(viewAliceUpperCase));
    }

    // ToString Tests

    @Test
    public void toString_personType_correctFormat() {
        Type type = TYPE_PERSON;
        String name = ALICE.getName().fullName;
        ViewCommand viewCommand = new ViewCommand(type, name);
        String expected = ViewCommand.class.getCanonicalName() + "{type=" + type + ", name=" + name + "}";
        assertEquals(expected, viewCommand.toString());
    }

    @Test
    public void toString_animalType_correctFormat() {
        Type type = TYPE_ANIMAL;
        String name = BELLA.getName().fullName;
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
