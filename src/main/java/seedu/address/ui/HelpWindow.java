package seedu.address.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

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

    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-w14-3.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "Refer to the user guide: " + USERGUIDE_URL;
    public static final String SUPPORTED_COMMANDS_HEADER_TEXT = "List of supported commands:";

    private static final Map<String, ArrayList<String>> COMMAND_LIST = new HashMap<>();
    private static CommandBox commandBoxReference = null;

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
        ArrayList<String> addDescription = new ArrayList<>();
        addDescription.add("Adds a new person or animal record to the system.");
        addDescription.add("Command format: add animal n/NAME d/DESCRIPTION l/LOCATION"
                          + " OR add person n/NAME p/PHONE e/EMAIL [t/TAG]...");
        addDescription.add("Example: add animal n/Bob d/Brown tabby with stripes l/Near Block 14 canteen");
        addDescription.add("Example: add person n/John Doe p/91234567 e/johndoe@gmail.com t/friend t/funny");
        COMMAND_LIST.put("add", addDescription);

        // delete command with detailed description
        ArrayList<String> addDeleteDescription = new ArrayList<>();
        addDeleteDescription.add("Deletes an existing record from the system.");
        addDeleteDescription.add("Command format: delete person n/NAME OR delete animal n/NAME");
        addDeleteDescription.add("Example: delete person n/John Tan");
        addDeleteDescription.add("Example: delete animal n/Fluffy");
        COMMAND_LIST.put("delete", addDeleteDescription);

        // edit command with detailed description
        ArrayList<String> addEditDescription = new ArrayList<>();
        addEditDescription.add("Edits an existing record from the system.");
        addEditDescription.add("Command format: edit person NAME [n/NAME] [p/PHONE] [e/EMAIL] [t/tag...] "
                              + "[f/ANIMAL_NAME dt/YYYY-MM-DD HH:MM]"
                              + " OR edit animal NAME [n/NAME] [d/DESCRIPTION] [l/LOCATION]");
        addEditDescription.add("Example: edit person John Doe p/98765432 a/321, Jane Street");
        addEditDescription.add("Example: edit animal Bob n/Bobby");
        COMMAND_LIST.put("edit", addEditDescription);

        // find command with detailed description
        ArrayList<String> addFindDescription = new ArrayList<>();
        addFindDescription.add("Searches a record from the system.");
        addFindDescription.add("Command format: find person NAME OR find animal NAME");
        addFindDescription.add("Example: find person Sam");
        addFindDescription.add("Example: find animal Bella");
        COMMAND_LIST.put("find", addFindDescription);

        // help command with detailed description
        ArrayList<String> addHelpDescription = new ArrayList<>();
        addHelpDescription.add("Opens the help window, or shows detailed help for a specified command.");
        addHelpDescription.add("Command format: help [COMMAND]");
        addHelpDescription.add("Example: help");
        addHelpDescription.add("Example: help add person");
        COMMAND_LIST.put("help", addHelpDescription);
    }

    /**
     * Creates individual clickable labels for each command and adds them to the commandLinksContainer.
     */
    private void createCommandLinks() {
        commandLinksContainer.getChildren().clear();

        // Sort commands alphabetically
        COMMAND_LIST.keySet().stream()
                .sorted()
                .forEach(command -> {
                    Label commandLink = new Label(command);
                    commandLink.setStyle("-fx-underline: true; -fx-cursor: hand; -fx-text-fill: black;");
                    commandLink.setOnMouseClicked(event -> openCommandHelp(command));
                    commandLinksContainer.getChildren().add(commandLink);
                });
    }

    /**
     * Returns a string with all command keys separated by a newline in alphabetical order.
     */
    public String getCommands() {
        return COMMAND_LIST.keySet().stream()
                .sorted()
                .reduce((a, b) -> a + '\n' + b)
                .orElse("");
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
     * Sets the CommandBox reference for use in command help windows.
     * @param commandBox The CommandBox instance to reference.
     */
    public static void setCommandBoxReference(CommandBox commandBox) {
        commandBoxReference = commandBox;
    }

    /**
     * Opens a detailed help window for a specific command without showing the main help window.
     * @param commandName The name of the command to show help for.
     */
    public static void openCommandHelp(String commandName) {
        HelpFunctionWindow functionWindow;
        if (commandBoxReference != null) {
            functionWindow = new HelpFunctionWindow(commandName, commandBoxReference::setCommandText);
        } else {
            functionWindow = new HelpFunctionWindow(commandName);
        }
        functionWindow.show();
    }


    /**
     * Exposes the commandList (immutable view suggestion in future). Currently returns backing map.
     */
    public static Map<String, ArrayList<String>> getCommandList() {
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
