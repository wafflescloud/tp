package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_NAME;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_BELLA;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.LOCATION_DESC_BELLA;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.Name;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Email;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.PersonBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_personNotFound_throwsCommandException() {
        String deleteCommand = "delete person n/NonExistentName";
        assertCommandException(deleteCommand, MESSAGE_INVALID_PERSON_DISPLAYED_NAME);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getPersonList().remove(0));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredPersonList().remove(0));
    }

    @Test
    public void getAnimalList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getAnimalList().remove(0));
    }

    @Test
    public void getFilteredAnimalList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredAnimalList().remove(0));
    }

    @Test
    public void getFeedingSessionList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFeedingSessionList().remove(0));
    }

    @Test
    public void getAddressBook_returnsAddressBook() {
        ReadOnlyAddressBook addressBook = logic.getAddressBook();
        assertNotNull(addressBook);
        assertEquals(model.getAddressBook(), addressBook);
    }

    @Test
    public void getAddressBookFilePath_returnsCorrectPath() {
        Path filePath = logic.getAddressBookFilePath();
        assertNotNull(filePath);
        assertEquals(model.getAddressBookFilePath(), filePath);
    }

    @Test
    public void getGuiSettings_returnsGuiSettings() {
        GuiSettings guiSettings = logic.getGuiSettings();
        assertNotNull(guiSettings);
        assertEquals(model.getGuiSettings(), guiSettings);
    }

    @Test
    public void setGuiSettings_updatesGuiSettings() {
        GuiSettings newGuiSettings = new GuiSettings(800, 600, 100, 100);
        logic.setGuiSettings(newGuiSettings);
        assertEquals(newGuiSettings, model.getGuiSettings());
    }

    @Test
    public void execute_addPersonCommand_success() throws Exception {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person validPerson = new PersonBuilder().withName(VALID_NAME_AMY).build();
        expectedModel.addPerson(validPerson);

        String addCommand = AddCommand.COMMAND_WORD + " person"
                + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY;

        logic.execute(addCommand);

        // Verify person was added (check by name since IDs are auto-generated)
        assertEquals(1, model.getFilteredPersonList().size());
        Person added = model.getFilteredPersonList().get(0);
        assertEquals(new Name("Amy Bee"), added.getName());
    }

    @Test
    public void execute_addAnimalCommand_success() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + " animal n/Bella"
                + DESCRIPTION_DESC_BELLA + LOCATION_DESC_BELLA;

        logic.execute(addCommand);

        // Verify animal was added
        assertEquals(1, model.getFilteredAnimalList().size());
    }

    @Test
    public void execute_listPersonCommand_success() throws Exception {
        // Add a person first
        Person person = new PersonBuilder().build();
        model.addPerson(person);

        String listCommand = "list person";
        CommandResult result = logic.execute(listCommand);

        // Just verify command executes successfully
        assertNotNull(result.getFeedbackToUser());
    }

    @Test
    public void execute_listAnimalCommand_success() throws Exception {
        String listCommand = "list animal";
        CommandResult result = logic.execute(listCommand);

        // Just verify command executes successfully
        assertNotNull(result.getFeedbackToUser());
    }

    @Test
    public void execute_clearCommand_success() throws Exception {
        // Add some data first
        Person person = new PersonBuilder().build();
        model.addPerson(person);

        String clearCommand = "clear";
        logic.execute(clearCommand);

        assertEquals(0, model.getFilteredPersonList().size());
        assertEquals(0, model.getFilteredAnimalList().size());
    }

    @Test
    public void execute_exitCommand_success() throws Exception {
        String exitCommand = "exit";
        CommandResult result = logic.execute(exitCommand);

        assertEquals("Exiting Address Book as requested ...", result.getFeedbackToUser());
    }

    @Test
    public void execute_helpCommand_success() throws Exception {
        String helpCommand = "help";
        CommandResult result = logic.execute(helpCommand);

        assertNotNull(result.getFeedbackToUser());
    }

    @Test
    public void execute_findPersonCommand_success() throws Exception {
        // Add a person first
        Person person = new PersonBuilder().withName("Alice").build();
        model.addPerson(person);

        String findCommand = "find person n/Alice";
        logic.execute(findCommand);

        assertEquals(1, model.getFilteredPersonList().size());
    }

    @Test
    public void execute_findAnimalCommand_success() throws Exception {
        String findCommand = "find animal n/Bella";
        logic.execute(findCommand);

        // Should not throw exception even with empty list
        assertNotNull(model.getFilteredAnimalList());
    }

    @Test
    public void execute_deletePersonCommand_success() throws Exception {
        // Add a person first
        Person person = new PersonBuilder().withName("Alice").build();
        model.addPerson(person);

        String deleteCommand = "delete person n/Alice";
        logic.execute(deleteCommand);

        assertEquals(0, model.getFilteredPersonList().size());
    }

    @Test
    public void deleteAnimal_notFound_throwsCommandException() {
        String deleteCommand = "delete animal n/NonExistentAnimal";
        assertThrows(CommandException.class, () -> logic.execute(deleteCommand));
    }

    @Test
    public void execute_editPersonCommand_success() throws Exception {
        // Add a person first
        Person person = new PersonBuilder().withName("Alice").build();
        model.addPerson(person);

        String editCommand = "edit person Alice p/91234567";
        logic.execute(editCommand);

        Person editedPerson = model.getFilteredPersonList().get(0);
        assertEquals(new Phone("91234567"), editedPerson.getPhone());
    }

    @Test
    public void execute_invalidAddCommandFormat_throwsParseException() {
        String invalidAddCommand = "add"; // Missing type and parameters
        assertThrows(ParseException.class, () -> logic.execute(invalidAddCommand));
    }

    @Test
    public void execute_invalidDeleteCommandFormat_throwsParseException() {
        String invalidDeleteCommand = "delete"; // Missing type and identifier
        assertThrows(ParseException.class, () -> logic.execute(invalidDeleteCommand));
    }

    @Test
    public void execute_commandWithInvalidParameters_throwsParseException() {
        String invalidCommand = "add person n/"; // Empty name
        assertThrows(ParseException.class, () -> logic.execute(invalidCommand));
    }

    @Test
    public void getPersonList_returnsCorrectList() {
        Person person = new PersonBuilder().build();
        model.addPerson(person);

        assertEquals(1, logic.getPersonList().size());
        assertEquals(person, logic.getPersonList().get(0));
    }

    @Test
    public void getAnimalList_returnsCorrectList() {
        assertEquals(0, logic.getAnimalList().size());
        assertNotNull(logic.getAnimalList());
    }

    @Test
    public void getFilteredPersonList_returnsFilteredList() {
        Person person1 = new PersonBuilder().withName("Alice").withPhone("91234567")
                .withEmail("alice@example.com").build();
        Person person2 = new PersonBuilder().withName("Bob").withPhone("81234567")
                .withEmail("bob@example.com").build();
        model.addPerson(person1);
        model.addPerson(person2);

        assertEquals(2, logic.getFilteredPersonList().size());
    }

    @Test
    public void getFilteredAnimalList_returnsFilteredList() {
        assertEquals(0, logic.getFilteredAnimalList().size());
        assertNotNull(logic.getFilteredAnimalList());
    }

    @Test
    public void getFeedingSessionList_returnsCorrectList() {
        assertEquals(0, logic.getFeedingSessionList().size());
        assertNotNull(logic.getFeedingSessionList());
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with an AddressBookStorage that throws the IOException e when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath)
                    throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = AddCommand.COMMAND_WORD + " person"
                + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY;

        // Execute and assert the expected CommandException is thrown
        assertThrows(CommandException.class, expectedMessage, () -> logic.execute(addCommand));

        // Verify model is updated with the new person (ignoring auto-generated IDs)
        assertEquals(1, model.getFilteredPersonList().size());
        Person added = model.getFilteredPersonList().get(0);
        assertEquals(new Name("Amy Bee"), added.getName());
        assertEquals(new Phone("91111111"), added.getPhone());
        assertEquals(new Email("amy@example.com"), added.getEmail());
        // No tags and no feeding sessions were provided
        assertEquals(java.util.Collections.emptySet(), added.getTags());
        assertEquals(java.util.Collections.emptySet(), added.getFeedingSessionIds());
    }
}
