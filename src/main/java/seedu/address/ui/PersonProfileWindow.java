package seedu.address.ui;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.model.animal.Animal;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.person.Person;

/**
 * A window that shows detailed contact and feeding session information of a {@link Person}.
 * This window is displayed as a pop-up window containing the person's name, phone, email,
 * tags, and all their feeding sessions (if any).
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

    @FXML private ScrollPane feedingSessionScrollPane;
    @FXML private VBox feedingSessionsContainer;

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

        displayAllFeedingSessions();
    }

    /**
     * Displays all feeding sessions for this person in the scrollable container.
     * Hides the ScrollPane if there are none.
     */
    private void displayAllFeedingSessions() {
        if (feedingSessionsContainer == null || feedingSessionScrollPane == null) {
            return; // FXML not wired yet; defensive
        }

        // Clear previous entries
        feedingSessionsContainer.getChildren().clear();

        // Filter sessions involving this person and sort by date/time ascending
        List<FeedingSession> sessionsForPerson = feedingSessions.stream()
                .filter(session -> session.involvesPerson(person.getId()))
                .sorted(Comparator.comparing(FeedingSession::getDateTime))
                .collect(Collectors.toList());

        if (sessionsForPerson.isEmpty()) {
            feedingSessionScrollPane.setVisible(false);
            feedingSessionScrollPane.setManaged(false);
            return;
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Populate entries
        for (FeedingSession session : sessionsForPerson) {
            String animalNameText = animals.stream()
                    .filter(animal -> animal.getId().equals(session.getAnimalId()))
                    .findFirst()
                    .map(animal -> animal.getName().fullName)
                    .orElse("Unknown Animal");

            Label dateLabel = new Label(session.getDateTime().format(dateFormatter));
            dateLabel.getStyleClass().add("session-date");

            Label timeLabel = new Label(session.getDateTime().format(timeFormatter));
            timeLabel.getStyleClass().add("session-time");

            Label animalLabel = new Label(animalNameText);
            animalLabel.getStyleClass().add("session-animal");

            HBox row = new HBox(10.0); // spacing between labels
            row.getStyleClass().add("feeding-session-box");
            row.getChildren().addAll(dateLabel, timeLabel, animalLabel);

            // Ensure each row fills available width to avoid abrupt width changes
            row.setFillHeight(true);
            row.setMaxWidth(Double.MAX_VALUE);

            feedingSessionsContainer.getChildren().add(row);
        }

        // Ensure the ScrollPane is visible
        feedingSessionScrollPane.setVisible(true);
        feedingSessionScrollPane.setManaged(true);
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
