package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.HelpCommand.SHOWING_HELP_MESSAGE;
import static seedu.address.logic.commands.HelpCommand.UNRECOGNIZED_COMMAND_MESSAGE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.ui.HelpWindow;

public class HelpCommandParserTest {

    private final HelpCommandParser parser = new HelpCommandParser();
    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        expectedModel = new ModelManager();
        seedHelpWindowCommands(
                Arrays.asList("add", "delete", "edit", "find", "help", "list", "clear", "exit", "undo", "redo")
        );
    }

    private void seedHelpWindowCommands(List<String> commands) {
        Map<String, ArrayList<String>> map = HelpWindow.getCommandList();
        map.clear();
        for (String cmd : commands) {
            ArrayList<String> details = new ArrayList<>();
            details.add("desc of " + cmd);
            map.put(cmd, details);
        }
    }

    // Only "help" command with no arguments launches general help window
    @Test
    public void parseEmpty_returnsGeneralHelp() throws Exception {
        CommandResult expected = new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        assertCommandSuccess(parser.parse(""), model, expected, expectedModel);
    }

    // Only whitespace after "help" launches general help window
    @Test
    public void parseWhitespace_returnsGeneralHelp() throws Exception {
        CommandResult expected = new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        assertCommandSuccess(parser.parse("   \t  \n"), model, expected, expectedModel);
    }

    // "help add" launches specific help for "add" command
    @Test
    public void parseKnownCommand_returnsSpecificHelp() throws Exception {
        String cmd = "add";
        CommandResult expected = new CommandResult("Showing help for " + cmd, false, false, cmd);
        assertCommandSuccess(parser.parse(cmd), model, expected, expectedModel);
    }

    // "help foo" where foo is not a known command launches general help window
    @Test
    public void parseUnknownCommand_returnsUnrecognized() throws Exception {
        CommandResult expected = new CommandResult(UNRECOGNIZED_COMMAND_MESSAGE, true, false);
        assertCommandSuccess(parser.parse("foo"), model, expected, expectedModel);
    }

    // certain commands have valid second arguments to show just the command's help window
    @Test
    public void parseValidTwoTokens_returnsSpecificHelp() throws Exception {
        for (String base : Arrays.asList("add", "delete", "edit", "find")) {
            for (String obj : Arrays.asList("person", "animal")) {
                String input = base + " " + obj;
                CommandResult expected = new CommandResult("Showing help for " + base, false, false, base);
                assertCommandSuccess(parser.parse(input), model, expected, expectedModel);
            }
        }
    }

    // open general help window if the second argument after the valid base command is invalid
    @Test
    public void parseInvalidSecondToken_returnsUnrecognized() throws Exception {
        for (String base : Arrays.asList("add", "delete", "edit", "find")) {
            String input = base + " somethingElse";
            CommandResult expected = new CommandResult(UNRECOGNIZED_COMMAND_MESSAGE, true, false);
            assertCommandSuccess(parser.parse(input), model, expected, expectedModel);
        }
    }

    // open general help window if the first argument is not a valid base command to accept a second argument
    @Test
    public void parseUnsupportedBaseWithSecondToken_returnsUnrecognized() throws Exception {
        for (String base : Arrays.asList("list", "clear", "exit", "undo", "redo", "help")) {
            String input = base + " person";
            CommandResult expected = new CommandResult(UNRECOGNIZED_COMMAND_MESSAGE, true, false);
            assertCommandSuccess(parser.parse(input), model, expected, expectedModel);
        }
    }

    // any input with three or more tokens should open general help window
    @Test
    public void parseThreeOrMoreTokens_returnsUnrecognized() throws Exception {
        for (String input : Arrays.asList(
                "add person extra", "delete animal trailing", "foo bar baz")) {
            CommandResult expected = new CommandResult(UNRECOGNIZED_COMMAND_MESSAGE, true, false);
            assertCommandSuccess(parser.parse(input), model, expected, expectedModel);
        }
    }

    // any uppercase arguments should open general help window
    @Test
    public void parseCaseSensitivity_returnsUnrecognized() throws Exception {
        for (String input : Arrays.asList("Add", "ADD person", "EdIt animal")) {
            CommandResult expected = new CommandResult(UNRECOGNIZED_COMMAND_MESSAGE, true, false);
            assertCommandSuccess(parser.parse(input), model, expected, expectedModel);
        }
    }

    // whitespace in between tokens should be trimmed correctly and launches specific command's help window if valid
    @Test
    public void parseExtraSpaces_validTwoToken_returnsSpecificHelp() throws Exception {
        String input = "   add    person   ";
        CommandResult expected = new CommandResult("Showing help for add", false, false, "add");
        assertCommandSuccess(parser.parse(input), model, expected, expectedModel);
    }
}
