package seedu.address.ui;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import seedu.address.model.animal.Animal;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.person.Person;

/**
 * A window that shows detailed contact and feeding session information of a {@link Person}.
 * This window is displayed as a pop-up window containing the person's name, phone, email,
 * tags, and their earliest upcoming feeding session (if any).
 */
public class PersonProfileWindow extends UiPart<Stage> {

    private static final String FXML = "PersonProfileWindow.fxml";

    /** The person whose profile is being displayed. */
    private final Person person;
    private final List<FeedingSession> feedingSessions;
    private final List<Animal> animals;

    // FXML UI elements
    @FXML private Label name;
    @FXML private Label phone;
    @FXML private Label email;
    @FXML private FlowPane tags;

    @FXML private Label animalName;
    @FXML private Label feedingDate;
    @FXML private Label feedingTime;
    @FXML private ScrollPane feedingSessionScrollPane;

    @FXML private Button copyEmailButton;
    @FXML private Button copyPhoneButton;

    /**
     * Creates a new PersonProfileWindow instance for the specified {@code person}.
     *
     * @param person The person whose profile is to be shown.
     * @param feedingSessions List of all feeding sessions.
     * @param animals List of all animals.
     */
    public PersonProfileWindow(Person person, List<FeedingSession> feedingSessions, List<Animal> animals) {
        super(FXML, new Stage());
        this.person = person;
        this.feedingSessions = feedingSessions;
        this.animals = animals;

        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        email.setText(person.getEmail().value);

        // Populate tags sorted alphabetically
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        displayEarliestFeedingSession();
    }

    /**
     * Displays the earliest feeding session in the feeding box.
     */
    private void displayEarliestFeedingSession() {
        FeedingSession earliest = person.getEarliestFeedingSession(feedingSessions);

        if (earliest != null) {
            if (feedingSessionScrollPane != null) {
                feedingSessionScrollPane.setVisible(true);
                feedingSessionScrollPane.setManaged(true);
            }

            if (animalName != null) {
                String animalNameText = animals.stream()
                        .filter(animal -> animal.getId().equals(earliest.getAnimalId()))
                        .findFirst()
                        .map(animal -> animal.getName().fullName)
                        .orElse("Unknown Animal");
                animalName.setText(animalNameText);
            }

            if (feedingDate != null) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                feedingDate.setText(earliest.getDateTime().format(dateFormatter));
            }

            if (feedingTime != null) {
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                feedingTime.setText(earliest.getDateTime().format(timeFormatter));
            }
        } else {
            if (feedingSessionScrollPane != null) {
                feedingSessionScrollPane.setVisible(false);
                feedingSessionScrollPane.setManaged(false);
            }
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
     * @param feedingSessions List of all feeding sessions.
     * @param animals List of all animals.
     */
    public static void openProfile(Person person, List<FeedingSession> feedingSessions, List<Animal> animals) {
        PersonProfileWindow window = new PersonProfileWindow(person, feedingSessions, animals);
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
