package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.HelpCommand.SHOWING_HELP_MESSAGE;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_help_success() {
        CommandResult expectedCommandResult = new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        assertCommandSuccess(new HelpCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_helpForSpecificCommand_success() {
        String commandName = "add";
        CommandResult expectedCommandResult = new CommandResult(
                "Showing help for " + commandName, false, false, commandName);
        assertCommandSuccess(new HelpCommand(commandName), model, expectedCommandResult, expectedModel);

        commandName = "delete";
        expectedCommandResult = new CommandResult(
                "Showing help for " + commandName, false, false, commandName);
        assertCommandSuccess(new HelpCommand(commandName), model, expectedCommandResult, expectedModel);

        commandName = "list";
        expectedCommandResult = new CommandResult(
                "Showing help for " + commandName, false, false, commandName);
        assertCommandSuccess(new HelpCommand(commandName), model, expectedCommandResult, expectedModel);

        commandName = "help";
        expectedCommandResult = new CommandResult(
                "Showing help for " + commandName, false, false, commandName);
        assertCommandSuccess(new HelpCommand(commandName), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_helpForUnknownCommand_unrecognized() {
        CommandResult expectedCommandResult = new CommandResult(
                HelpCommand.UNRECOGNIZED_COMMAND_MESSAGE, true, false);
        assertCommandSuccess(new HelpCommand("unknown", true), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_helpForEmptyCommand_unrecognized() {
        CommandResult expectedCommandResult = new CommandResult(
                HelpCommand.UNRECOGNIZED_COMMAND_MESSAGE, true, false);
        assertCommandSuccess(new HelpCommand(" ", true), model, expectedCommandResult, expectedModel);
    }
}
