package seedu.address.ui;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.person.Person;

/**
 * A window that shows detailed contact and feeding session information of a {@link Person}.
 * This window is displayed as a pop-up window containing the person's name, phone, email,
 * tags, and their most recent feeding session (if any).
 */
public class ProfileWindow extends UiPart<Stage> {

    private static final String FXML = "ProfileWindow.fxml";

    /** The person whose profile is being displayed. */
    private final Person person;

    // FXML UI elements
    @FXML private Label name;
    @FXML private Label phone;
    @FXML private Label email;
    @FXML private FlowPane tags;

    @FXML private Label animalName;
    @FXML private Label feedingDate;
    @FXML private Label feedingTime;

    @FXML private Button copyEmailButton;
    @FXML private Button copyPhoneButton;

    /**
     * Creates a new ProfileWindow instance for the specified {@code person}.
     *
     * @param person The person whose profile is to be shown.
     */
    public ProfileWindow(Person person) {
        super(FXML, new Stage());
        this.person = person;

        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        email.setText(person.getEmail().value);

        // Populate tags sorted alphabetically
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        // Show most recent feeding session if available
        if (!person.getFeedingSessions().isEmpty()) {
            FeedingSession session = person.getFeedingSessions().iterator().next();
            animalName.setText(session.getAnimal().getName().fullName);
            feedingDate.setText(session.getFeedingTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            feedingTime.setText(session.getFeedingTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        }
    }

    /**
     * Displays the profile window and centers it on screen.
     */
    public void show() {
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Opens a profile window for the specified {@code person}.
     * This method is a convenience static helper.
     *
     * @param person The person to display in the profile window.
     */
    public static void openProfile(Person person) {
        ProfileWindow window = new ProfileWindow(person);
        window.show();
    }

    /**
     * Copies the person's email address to the system clipboard.
     * Triggered by clicking the email copy button.
     */
    @FXML
    private void copyEmail() {
        ClipboardContent content = new ClipboardContent();
        content.putString(person.getEmail().value);
        Clipboard.getSystemClipboard().setContent(content);
    }

    /**
     * Copies the person's phone number to the system clipboard.
     * Triggered by clicking the phone copy button.
     */
    @FXML
    private void copyPhone() {
        ClipboardContent content = new ClipboardContent();
        content.putString(person.getPhone().value);
        Clipboard.getSystemClipboard().setContent(content);
    }
}
