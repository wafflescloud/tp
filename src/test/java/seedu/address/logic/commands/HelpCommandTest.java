package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.HelpCommand.SHOWING_HELP_MESSAGE;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    // Test: Executing HelpCommand with no arguments shows the general help window.
    @Test
    public void execute_help_success() {
        CommandResult expectedCommandResult = new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        assertCommandSuccess(new HelpCommand(), model, expectedCommandResult, expectedModel);
    }

    // Test: Executing HelpCommand with a specific command name shows help for that command.
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

    // Test: HelpCommand returns an unrecognized command message for unknown commands.
    @Test
    public void execute_helpForUnknownCommand_unrecognized() {
        CommandResult expectedCommandResult = new CommandResult(
                HelpCommand.UNRECOGNIZED_COMMAND_MESSAGE, true, false);
        assertCommandSuccess(new HelpCommand("unknown", true), model, expectedCommandResult, expectedModel);
    }

    // Test: HelpCommand returns an unrecognized command message for empty command input.
    @Test
    public void execute_helpForEmptyCommand_unrecognized() {
        CommandResult expectedCommandResult = new CommandResult(
                HelpCommand.UNRECOGNIZED_COMMAND_MESSAGE, true, false);
        assertCommandSuccess(new HelpCommand(" ", true), model, expectedCommandResult, expectedModel);
    }

    // Test: HelpCommand constructor throws AssertionError when commandName is null.
    @Test
    public void constructor_nullCommandName_throwsAssertionError() {
        Assertions.assertThrows(AssertionError.class, () -> new HelpCommand(null));
    }

    // Test: HelpCommand constructor throws AssertionError when commandName is an empty string.
    @Test
    public void constructor_emptyCommandName_throwsAssertionError() {
        Assertions.assertThrows(AssertionError.class, () -> new HelpCommand(""));
    }

    // Test: HelpCommand constructor throws AssertionError when commandName is only whitespace.
    @Test
    public void constructor_whitespaceCommandName_throwsAssertionError() {
        Assertions.assertThrows(AssertionError.class, () -> new HelpCommand("   "));
    }

    // Test: HelpCommand.execute throws AssertionError when model is null.
    @Test
    public void execute_nullModel_throwsAssertionError() {
        HelpCommand helpCommand = new HelpCommand();
        Assertions.assertThrows(AssertionError.class, () -> helpCommand.execute(null));
    }

    // Tests with assertions disabled

    private URLClassLoader newIsolatedLoaderWithAssertions(boolean enableAssertions) throws Exception {
        String cp = System.getProperty("java.class.path");
        URL[] urls = Arrays.stream(cp.split(File.pathSeparator))
                .map(p -> new File(p))
                .map(File::toURI)
                .map(u -> {
                    try {
                        return u.toURL();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(URL[]::new);
        URLClassLoader loader = new URLClassLoader(urls, null);
        loader.setDefaultAssertionStatus(enableAssertions);
        return loader;
    }

    // Test: HelpCommand constructors accept null and whitespace command names when assertions are disabled.
    @Test
    public void assertionsDisabled_constructors_acceptNullAndWhitespace() throws Exception {
        try (URLClassLoader loader = newIsolatedLoaderWithAssertions(false)) {
            Class<?> helpCmdClass = loader.loadClass("seedu.address.logic.commands.HelpCommand");
            Constructor<?> strCtor = helpCmdClass.getConstructor(String.class);

            assertDoesNotThrow(() -> {
                try {
                    strCtor.newInstance((Object) null);
                } catch (Exception e) {
                    throw unwrap(e);
                }
            });

            assertDoesNotThrow(() -> {
                try {
                    strCtor.newInstance("");
                } catch (Exception e) {
                    throw unwrap(e);
                }
            });

            assertDoesNotThrow(() -> {
                try {
                    strCtor.newInstance("   ");
                } catch (Exception e) {
                    throw unwrap(e);
                }
            });
        }
    }

    // Test: HelpCommand.execute accepts a null model when assertions are disabled.
    @Test
    public void assertionsDisabled_execute_acceptsNullModel() throws Exception {
        try (URLClassLoader loader = newIsolatedLoaderWithAssertions(false)) {
            Class<?> helpCmdClass = loader.loadClass("seedu.address.logic.commands.HelpCommand");
            Class<?> modelInterface = loader.loadClass("seedu.address.model.Model");
            Constructor<?> defaultCtor = helpCmdClass.getConstructor();
            Method execute = helpCmdClass.getMethod("execute", modelInterface);

            Object instance = defaultCtor.newInstance();

            assertDoesNotThrow(() -> {
                try {
                    execute.invoke(instance, new Object[] { null });
                } catch (Exception e) {
                    throw unwrap(e);
                }
            });
        }
    }

    private static RuntimeException unwrap(Exception e) {
        Throwable cause = e.getCause();
        return new RuntimeException(cause != null ? cause : e);
    }
}
