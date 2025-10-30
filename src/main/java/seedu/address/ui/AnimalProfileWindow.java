package seedu.address.ui;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
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

        // Ensure description behaves like person details: single-line ellipsis, no extra vertical growth
        description.setWrapText(false);
        description.setTextOverrun(OverrunStyle.ELLIPSIS);
        description.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(description, Priority.ALWAYS);
        description.setMinHeight(Region.USE_PREF_SIZE);
        description.setPrefHeight(Region.USE_COMPUTED_SIZE);
        description.setMaxHeight(Region.USE_PREF_SIZE);
        // Ensure parent row does not expand vertically
        if (description.getParent() instanceof HBox) {
            ((HBox) description.getParent()).setFillHeight(true);
        }

        // Populate tags sorted alphabetically
        animal.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        displayAllFeedingSessions();
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

            Label timeLabel = new Label(session.getDateTime().format(timeFormatter));
            timeLabel.getStyleClass().add("session-time");

            Label personLabel = new Label(personNameText);
            personLabel.getStyleClass().add("session-animal");

            HBox row = new HBox(10.0);
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
     * Copies the animal's location to the system clipboard.
     * This method is triggered by clicking the location copy button.
     */
    @FXML
    private void copyLocation() {
        Clipboard.getSystemClipboard().setContent(new ClipboardContent() {{
            putString(animal.getLocation().value);
        }});
    }
}
