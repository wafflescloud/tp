package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
// import static seedu.address.testutil.TypicalAnimals.LUNA;
// import static seedu.address.testutil.TypicalAnimals.SIMBA;
// import static seedu.address.testutil.TypicalAnimals.WHISKERS;
import static seedu.address.testutil.TypicalAnimals.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.NameContainsKeywordsPredicateAnimal;

/**
 * Contains integration tests (interaction with the Model) for {@code FindAnimalCommand}.
 */
public class FindAnimalCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicateAnimal firstPredicate =
                new NameContainsKeywordsPredicateAnimal(Collections.singletonList("first"));
        NameContainsKeywordsPredicateAnimal secondPredicate =
                new NameContainsKeywordsPredicateAnimal(Collections.singletonList("second"));

        FindCommand<Animal> findFirstCommand = FindCommand.forAnimal(firstPredicate);
        FindCommand<Animal> findSecondCommand = FindCommand.forAnimal(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand<Animal> findFirstCommandCopy = FindCommand.forAnimal(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different Animal -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noAnimalFound() {
        String expectedMessage = String.format(Messages.MESSAGE_FIND_ANIMAL_SUCCESS, 0);
        NameContainsKeywordsPredicateAnimal predicate = preparePredicate(" ");
        FindCommand<Animal> command = FindCommand.forAnimal(predicate);
        expectedModel.updateFilteredAnimalList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredAnimalList());
    }

    /*
    @Test
    public void execute_multipleKeywords_multipleAnimalsFound() {
        String expectedMessage = String.format(Messages.MESSAGE_FIND_ANIMAL_SUCCESS, 3);
        NameContainsKeywordsPredicateAnimal predicate = preparePredicate("n/Whiskers n/Luna n/Simba");
        FindAnimalCommand command = new FindAnimalCommand(predicate);
        expectedModel.updateFilteredAnimalList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(WHISKERS, LUNA, SIMBA), model.getFilteredAnimalList());
    }*/

    @Test
    public void toStringMethod() {
        NameContainsKeywordsPredicateAnimal predicate =
            new NameContainsKeywordsPredicateAnimal(Arrays.asList("keyword"));
        FindCommand<Animal> findCommand = FindCommand.forAnimal(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate
                + ", successMessageFormat=" + Messages.MESSAGE_FIND_ANIMAL_SUCCESS + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicateAnimal}.
     */
    private NameContainsKeywordsPredicateAnimal preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicateAnimal(Arrays.asList(userInput.split("\\s+")));
    }
}
