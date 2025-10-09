package seedu.address.ui;

import java.util.logging.Logger;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://se-education.org/addressbook-level3/UserGuide.html";
    public static final String HELP_MESSAGE = "Refer to the user guide: " + USERGUIDE_URL;
    public static final String SUPPORTED_COMMANDS_HEADER_TEXT = "List of supported commands:";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    @FXML
    private Label supportedCommandsHeader;

    @FXML
    private VBox commandLinksContainer;

    private static final Map<String, ArrayList<String>> COMMAND_LIST = new HashMap<>();

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        populateCommandList();
        helpMessage.setText(HELP_MESSAGE);
        supportedCommandsHeader.setText(SUPPORTED_COMMANDS_HEADER_TEXT);
        createCommandLinks();
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Populates the command list if empty. Each command maps to a list whose first element is a simple description
     * placeholder (currently identical to the command name). Extend later with syntax/examples etc.
     */
    private void populateCommandList() {
        if (!COMMAND_LIST.isEmpty()) {
            return;
        }

        // TODO: Make this info editable outside of the program (e.g. store these descriptions in a JSON or text file)
        // addAnimal command with detailed description
        ArrayList<String> addAnimalDescription = new ArrayList<>();
        addAnimalDescription.add("Adds a new animal record to the system.");
        addAnimalDescription.add("Command format: addAnimal /name <animal name> /description <animal description> /location <animal location>");
        addAnimalDescription.add("Example: addAnimal /name Bob /description Brown tabby with stripes /location Near Block 14 canteen");
        COMMAND_LIST.put("addAnimal", addAnimalDescription);

        // addPerson command with detailed description
        ArrayList<String> addPersonDescription = new ArrayList<>();
        addPersonDescription.add("Adds a new person record to the system.");
        addPersonDescription.add("Command format: addPerson /name <contact name> /phoneNumber <8-digit> /email <address>");
        addPersonDescription.add("Example: addPerson /name John Doe /phoneNumber 91234567 /email johndoe@gmail.com");
        COMMAND_LIST.put("addPerson", addPersonDescription);

        // delete command with detailed description
        ArrayList<String> addDeleteDescription = new ArrayList<>();
        addDeleteDescription.add("Deletes an existing record from the system.");
        addDeleteDescription.add("Command format: delete /person <name> OR delete /animal <name>");
        addDeleteDescription.add("Example: delete /person John Tan");
        addDeleteDescription.add("Example: delete /animal Hehehehehaw");
        COMMAND_LIST.put("delete", addDeleteDescription);

        // edit command with detailed description
        ArrayList<String> addEditDescription = new ArrayList<>();
        addEditDescription.add("Edits an existing record from the system.");
        addEditDescription.add("Command format: edit <contact name> /<field name> <edited detail>");
        addEditDescription.add("Example: edit John Doe /person John Tan");
        COMMAND_LIST.put("edit", addEditDescription);

        // find command with detailed description
        ArrayList<String> addFindDescription = new ArrayList<>();
        addFindDescription.add("Searches a record from the system.");
        addFindDescription.add("Command format: find <keyword>");
        addFindDescription.add("Example: find Sam");
        COMMAND_LIST.put("find", addFindDescription);

        // help command with detailed description
        ArrayList<String> addHelpDescription = new ArrayList<>();
        addHelpDescription.add("Opens the help window.");
        addHelpDescription.add("Command format: help OR help <command name>");
        addHelpDescription.add("Example: help");
        addHelpDescription.add("Example: help addPerson");
        COMMAND_LIST.put("help", addHelpDescription);
    }

    /**
     * Creates individual clickable labels for each command and adds them to the commandLinksContainer.
     */
    private void createCommandLinks() {
        commandLinksContainer.getChildren().clear(); // Clear any existing children

        for (String command : COMMAND_LIST.keySet()) {
            Label commandLink = new Label(command);
            commandLink.setStyle("-fx-underline: true; -fx-cursor: hand; -fx-text-fill: #FFFFFF;");
            commandLink.setOnMouseClicked(event -> openCommandHelp(command));
            commandLinksContainer.getChildren().add(commandLink);
        }
    }

    /**
     * Returns a string with all command keys separated by a newline. Order is unspecified.
     */
    public String getCommands() {
        StringBuilder sb = new StringBuilder();
        for (String key : COMMAND_LIST.keySet()) {
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(key);
        }
        return sb.toString();
    }

    /**
     * Retrieves all description lines for a command joined by newlines, or null if absent.
     */
    public static String getDescriptionForCommand(String command) {
        ArrayList<String> list = COMMAND_LIST.get(command);
        if (list == null || list.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append('\n');
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    /**
     * Opens a detailed help window for a specific command.
     * @param commandName The name of the command to show help for.
     */
    private void openCommandHelp(String commandName) {
        HelpFunctionWindow functionWindow = new HelpFunctionWindow(commandName);
        functionWindow.show();
    }

    /**
     * Exposes the commandList (immutable view suggestion in future). Currently returns backing map.
     */
    public Map<String, ArrayList<String>> getCommandList() {
        return COMMAND_LIST;
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }
}
