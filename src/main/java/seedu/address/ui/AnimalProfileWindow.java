package seedu.address.ui;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.model.animal.Animal;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.person.Person;

/**
 * A window that shows detailed information of an {@link Animal}, including
 * its name, description, location, tags, and related feeding sessions.
 */
public class AnimalProfileWindow extends UiPart<Stage> {

    private static final String FXML = "AnimalProfileWindow.fxml";
    private static final List<Stage> openWindows = new ArrayList<>();

    private final Animal animal;
    private final List<FeedingSession> feedingSessions;
    private final List<Person> persons;

    @FXML private Label name;
    @FXML private Label description;
    @FXML private Label locationLabel;
    @FXML private FlowPane tags;

    @FXML private ScrollPane feedingSessionScrollPane;
    @FXML private VBox feedingSessionsContainer;

    /**
     * Creates a new AnimalProfileWindow for the specified {@code animal}.
     */
    public AnimalProfileWindow(Animal animal, List<FeedingSession> feedingSessions, List<Person> persons) {
        super(FXML, new Stage());
        this.animal = animal;
        this.feedingSessions = feedingSessions;
        this.persons = persons;

        name.setText(animal.getName().fullName);
        description.setText(animal.getDescription().value);
        locationLabel.setText(animal.getLocation().value);

        // Ensure description behaves like person details: allow wrapping and no vertical oversize
        description.setWrapText(true);
        HBox.setHgrow(description, Priority.ALWAYS);
        description.setMaxWidth(Double.MAX_VALUE);

        locationLabel.setWrapText(true);
        HBox.setHgrow(locationLabel, Priority.ALWAYS);
        locationLabel.setMaxWidth(Double.MAX_VALUE);

        // Populate tags sorted alphabetically
        animal.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        displayAllFeedingSessions();
        openWindows.add(getRoot());
    }

    /**
     * Displays all feeding sessions for this animal in the scrollable container.
     * Hides the ScrollPane if there are none.
     */
    private void displayAllFeedingSessions() {
        if (feedingSessionsContainer == null || feedingSessionScrollPane == null) {
            return;
        }

        feedingSessionsContainer.getChildren().clear();

        List<FeedingSession> sessionsForAnimal = feedingSessions.stream()
                .filter(session -> session.involvesAnimal(animal.getId()))
                .sorted(Comparator.comparing(FeedingSession::getDateTime))
                .collect(Collectors.toList());

        if (sessionsForAnimal.isEmpty()) {
            feedingSessionScrollPane.setVisible(false);
            feedingSessionScrollPane.setManaged(false);
            return;
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (FeedingSession session : sessionsForAnimal) {
            String personNameText = persons.stream()
                    .filter(p -> p.getId().equals(session.getPersonId()))
                    .findFirst()
                    .map(p -> p.getName().fullName)
                    .orElse("Unknown Person");

            Label dateLabel = new Label(session.getDateTime().format(dateFormatter));
            dateLabel.getStyleClass().add("session-date");
            // Keep date fully visible: size to content and do not shrink
            dateLabel.setMinWidth(Region.USE_PREF_SIZE);
            dateLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);
            dateLabel.setMaxWidth(Region.USE_PREF_SIZE);
            HBox.setHgrow(dateLabel, Priority.NEVER);

            Label timeLabel = new Label(session.getDateTime().format(timeFormatter));
            timeLabel.getStyleClass().add("session-time");
            // Keep time fully visible: size to content and do not shrink
            timeLabel.setMinWidth(Region.USE_PREF_SIZE);
            timeLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);
            timeLabel.setMaxWidth(Region.USE_PREF_SIZE);
            HBox.setHgrow(timeLabel, Priority.NEVER);

            Label personLabel = new Label(personNameText);
            personLabel.getStyleClass().add("session-animal");
            personLabel.setWrapText(true);
            HBox.setHgrow(personLabel, javafx.scene.layout.Priority.ALWAYS);
            personLabel.setMaxWidth(Double.MAX_VALUE);

            HBox row = new HBox(5.0);
            row.getStyleClass().add("feeding-session-box");
            row.getChildren().addAll(dateLabel, timeLabel, personLabel);

            row.setFillHeight(true);
            row.setMaxWidth(Double.MAX_VALUE);

            feedingSessionsContainer.getChildren().add(row);
        }

        feedingSessionScrollPane.setVisible(true);
        feedingSessionScrollPane.setManaged(true);
    }

    /**
     * Shows the animal profile window and centers it on screen.
     */
    public void show() {
        Stage stage = getRoot();
        stage.setWidth(400);
        stage.setHeight(300);
        stage.show();
        stage.centerOnScreen();
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
     * This method is triggered by clicking the location copy button.
     */
    @FXML
    private void copyLocation() {
        Clipboard.getSystemClipboard().setContent(
                new ClipboardContent() {
                    {
                        putString(animal.getLocation().value);
                    }
                });
    }
}
