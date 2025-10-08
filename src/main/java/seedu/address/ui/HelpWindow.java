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
        String[] commands = {"addAnimal", "addPerson", "delete", "edit", "find", "help"};
        for (String cmd : commands) {
            String trimmed = cmd.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            ArrayList<String> details = new ArrayList<>();
            details.add(trimmed); // description placeholder
            COMMAND_LIST.put(trimmed, details);
        }
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
     * Retrieves the first description line for a command, or null if absent.
     */
    public static String getDescriptionForCommand(String command) {
        ArrayList<String> list = COMMAND_LIST.get(command);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
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
