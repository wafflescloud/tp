package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.ContactContainsKeywordsPredicate;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand} when finding persons.
 */
public class FindPersonCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        ContactContainsKeywordsPredicate<Person> firstPredicate =
                new ContactContainsKeywordsPredicate<>(Collections.singletonList("first"));
        ContactContainsKeywordsPredicate<Person> secondPredicate =
                new ContactContainsKeywordsPredicate<>(Collections.singletonList("second"));

        FindCommand<Person> findFirstCommand = FindCommand.forPerson(firstPredicate);
        FindCommand<Person> findSecondCommand = FindCommand.forPerson(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand<Person> findFirstCommandCopy = FindCommand.forPerson(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(Messages.MESSAGE_FIND_PERSON_SUCCESS, 0);
        ContactContainsKeywordsPredicate<Person> predicate = preparePredicate("NonExistentPersonName12345");
        FindCommand<Person> command = FindCommand.forPerson(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(Messages.MESSAGE_FIND_PERSON_SUCCESS, 3);
        ContactContainsKeywordsPredicate<Person> predicate = preparePredicate("Kurz Elle Kunz");
        FindCommand<Person> command = FindCommand.forPerson(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        ContactContainsKeywordsPredicate<Person> predicate =
            new ContactContainsKeywordsPredicate<>(Arrays.asList("keyword"));
        FindCommand<Person> findCommand = FindCommand.forPerson(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate
                + ", successMessageFormat=" + Messages.MESSAGE_FIND_PERSON_SUCCESS + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code ContactContainsKeywordsPredicate}.
     */
    private ContactContainsKeywordsPredicate<Person> preparePredicate(String userInput) {
        return new ContactContainsKeywordsPredicate<>(Arrays.asList(userInput.split("\\s+")));
    }
}
