package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.UndoCommand.MESSAGE_FAILURE;
import static seedu.address.logic.commands.UndoCommand.MESSAGE_SUCCESS;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Integration and unit tests for {@code UndoCommand}.
 */
public class UndoCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        expectedModel = new ModelManager();
    }

    @Test
    public void execute_noUndoableState_throwsCommandException() {
        UndoCommand undoCommand = new UndoCommand();
        assertThrows(CommandException.class, () -> undoCommand.execute(model), MESSAGE_FAILURE);
    }

    @Test
    public void execute_hasUndoableState_success() throws Exception {
        // Step 1: Add a person (automatically saves state)
        Person validPerson = new PersonBuilder().withName(VALID_NAME_AMY).build();
        model.addPerson(validPerson);

        // Prepare expected model:
        // After undo, Amy should no longer exist.
        expectedModel.addPerson(validPerson);
        expectedModel.undo();

        // Step 2: Run undo command
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, model, MESSAGE_SUCCESS, expectedModel);
    }
}
