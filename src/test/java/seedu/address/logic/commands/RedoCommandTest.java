package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.RedoCommand.MESSAGE_FAILURE;
import static seedu.address.logic.commands.RedoCommand.MESSAGE_SUCCESS;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;g
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the {@code Model})
 * and unit tests for {@link seedu.address.logic.commands.RedoCommand}.
 */
public class RedoCommandTest {

    private Model model;
    private Model expectedModel;

    /**
     * Sets up a fresh {@code ModelManager} and {@code expectedModel} before each test.
     * Both models start empty and are populated during individual tests.
     */
    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        expectedModel = new ModelManager();
    }

    /**
     * Verifies that executing {@code RedoCommand} when there are no redoable states
     * results in a {@link CommandException} being thrown.
     */
    @Test
    public void execute_noRedoableState_throwsCommandException() {
        RedoCommand redoCommand = new RedoCommand();
        assertThrows(CommandException.class, () -> redoCommand.execute(model), MESSAGE_FAILURE);
    }

    /**
     * Tests that {@code RedoCommand} successfully re-applies the most recently undone state.
     *
     * <p>Scenario:
     * <ol>
     *   <li>Add a valid person (this saves the current state).</li>
     *   <li>Delete the same person (this also saves a new state).</li>
     *   <li>Undo the deletion (restores the previous state with the person present).</li>
     *   <li>Redo the deletion (removes the person again).</li>
     * </ol>
     *
     * <p>Expected result: {@code RedoCommand} executes successfully with the message
     * {@code MESSAGE_SUCCESS}, and the model matches the expected model
     * where the person has been deleted again.
     */
    @Test
    public void execute_hasRedoableState_success() throws Exception {
        // Step 1: Add a person (this automatically saves state)
        Person validPerson = new PersonBuilder().withName(VALID_NAME_AMY).build();
        model.addPerson(validPerson);

        // Step 2: Delete the same person (this also saves state)
        model.deletePerson(validPerson);

        // Step 3: Undo -> brings Amy back
        model.undo();

        // Prepare expected model:
        // After redo, Amy should be deleted again.
        expectedModel.addPerson(validPerson);
        expectedModel.deletePerson(validPerson);

        // Step 4: Execute redo
        RedoCommand redoCommand = new RedoCommand();
        assertCommandSuccess(redoCommand, model, MESSAGE_SUCCESS, expectedModel);
    }
}
