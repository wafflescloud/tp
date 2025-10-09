package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Window that displays detailed help for a specific command.
 */
public class HelpFunctionWindow extends UiPart<Stage> {

    private static final String FXML = "HelpFunctionWindow.fxml";

    private final String commandName;

    @FXML
    private Label commandTitleLabel;

    @FXML
    private Label commandDescriptionLabel;

    /**
     * Constructs a HelpFunctionWindow for the given command name.
     *
     * @param commandName Name of the command.
     */
    public HelpFunctionWindow(String commandName) {
        super(FXML, new Stage());
        this.commandName = commandName;
        populate();
    }

    private void populate() {
        commandTitleLabel.setText(commandName);
        String desc = HelpWindow.getDescriptionForCommand(commandName);
        if (desc == null) {
            commandDescriptionLabel.setText("description: (no description found)");
        } else {
            commandDescriptionLabel.setText("description: " + desc);
        }
    }

    /** Shows the function help window. */
    public void show() {
        getRoot().show();
        getRoot().centerOnScreen();
    }
}
