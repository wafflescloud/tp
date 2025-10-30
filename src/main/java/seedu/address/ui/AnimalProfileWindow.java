package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.model.animal.Animal;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.person.Person;

/**
 * A window that shows detailed information of an {@link Animal}.
 * This window is displayed as a pop-up window containing the animal's name,
 * description, and location.
 */
public class AnimalProfileWindow extends UiPart<Stage> {

    private static final String FXML = "AnimalProfileWindow.fxml";
    private static final List<Stage> openWindows = new ArrayList<>();

    private final Animal animal;

    @FXML
    private Label name;

    @FXML
    private Label description;

    @FXML
    private Label locationLabel;

    /**
     * Creates a new AnimalProfileWindow for the specified {@code animal}.
     */
    public AnimalProfileWindow(Animal animal, List<FeedingSession> feedingSessions, List<Person> persons) {
        super(FXML, new Stage());
        this.animal = animal;

        name.setText(animal.getName().fullName);
        description.setText(animal.getDescription().value);
        locationLabel.setText(animal.getLocation().value);

        openWindows.add(getRoot());
    }

    /**
     * Shows the animal profile window and centers it on screen.
     */
    public void show() {
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Opens an animal profile window for the specified {@code animal}.
     */
    public static void openProfile(Animal animal, List<FeedingSession> feedingSessions, List<Person> persons) {
        AnimalProfileWindow window = new AnimalProfileWindow(animal, feedingSessions, persons);
        window.show();
    }

    /**
     * Hides all open AnimalProfileWindows.
     */
    public static void hideAllProfiles() {
        for (Stage window : openWindows) {
            if (window.isShowing()) {
                window.hide();
            }
        }
    }

    /**
     * Copies the animal's location to the system clipboard.
     * This method is triggered by clicking the location copy button
     * in the animal profile window.
     */
    @FXML
    private void copyLocation() {
        ClipboardContent content = new ClipboardContent();
        content.putString(animal.getLocation().value);
        Clipboard.getSystemClipboard().setContent(content);
    }

}
