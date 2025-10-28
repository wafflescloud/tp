package seedu.address.ui;

import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Window that displays detailed help for a specific command.
 */
public class HelpFunctionWindow extends UiPart<Stage> {

    private static final String FXML = "HelpFunctionWindow.fxml";

    private final String commandName;
    private Consumer<String> commandTextSetter;

    @FXML
    private Label commandTitleLabel;

    @FXML
    private Label commandDescriptionLabel;

    @FXML
    private VBox contentContainer;

    /**
     * Constructs a HelpFunctionWindow for the given command name.
     * Displays help for the specified command.
     *
     * @param commandName Name of the command.
     */
    public HelpFunctionWindow(String commandName) {
        super(FXML, new Stage());
        this.commandName = commandName;
        populate();
    }

    /**
     * Constructs a HelpFunctionWindow for the given command name with a command text setter.
     * Displays help for the specified command and allows setting text in the command box.
     *
     * @param commandName Name of the command.
     * @param commandTextSetter Function to set text in the command box.
     */
    public HelpFunctionWindow(String commandName, Consumer<String> commandTextSetter) {
        super(FXML, new Stage());
        this.commandName = commandName;
        this.commandTextSetter = commandTextSetter;
        populate();
    }

    /**
     * Populates the window with command title, description, formats, and examples.
     */
    private void populate() {
        commandTitleLabel.setText(commandName);
        commandTitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        String desc = HelpWindow.getDescriptionForCommand(commandName);
        if (desc == null) {
            commandDescriptionLabel.setText("Description: (no description found)");
        } else {
            // Only show the first line (description) in the description label
            String[] lines = desc.split("\n");
            if (lines.length > 0) {
                commandDescriptionLabel.setText("Description: " + lines[0]);
            }
        }
        commandDescriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

        populateContent();
    }

    /**
     * Populates the content container with command formats and examples in the desired order.
     */
    private void populateContent() {
        if (contentContainer == null) {
            return;
        }

        contentContainer.getChildren().clear();

        ArrayList<String> commandDetails = HelpWindow.getCommandList().get(commandName);
        if (commandDetails == null) {
            return;
        }

        for (String line : commandDetails) {
            if (line.startsWith("Command format:")) {
                createCommandFormatSection(line);
            } else if (line.startsWith("Example:")) {
                Label exampleLabel = new Label(line);
                exampleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
                exampleLabel.setWrapText(true);
                contentContainer.getChildren().add(exampleLabel);
            }
        }
    }

    /**
     * Creates a command format section with inline buttons for each format.
     * Clicking a format sets the command text in the command box and closes the window.
     *
     * @param formatLine The format line string.
     */
    private void createCommandFormatSection(String formatLine) {
        // Extract the format part after "Command format: "
        String format = formatLine.substring("Command format: ".length());

        // Add the "command format (click to copy):" label
        Label formatLabel = new Label("Command format (click to copy):");
        formatLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: black;");
        contentContainer.getChildren().add(formatLabel);

        // Handle multiple formats separated by "OR"
        String[] formats = format.split(" OR ");

        for (String singleFormat : formats) {
            Label formatLink = new Label(singleFormat.trim());
            formatLink.setStyle("-fx-underline: true; -fx-cursor: hand; -fx-text-fill: black; "
                              + "-fx-font-size: 14px; -fx-padding: 4px 0;");

            formatLink.setOnMouseClicked(event -> {
                if (commandTextSetter != null) {
                    commandTextSetter.accept(singleFormat.trim());
                }
                getRoot().close();
            });

            contentContainer.getChildren().add(formatLink);
        }
    }

    /**
     * Shows the function help window.
     */
    public void show() {
        getRoot().show();
        getRoot().centerOnScreen();
    }
}
