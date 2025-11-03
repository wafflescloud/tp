package seedu.address.ui;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.animal.Animal;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.person.Person;


/**
 * An UI component that displays information of a {@code Animal}.
 */
public class AnimalCard extends UiPart<Region> {

    private static final String FXML = "AnimalListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Animal animal;
    private final List<FeedingSession> feedingSessions;
    private final List<Person> persons;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label description;
    @FXML
    private Label animalLocation;
    @FXML
    private FlowPane tags;
    @FXML
    private Label feederName;
    @FXML
    private Label feedingDate;
    @FXML
    private Label feedingTime;
    @FXML
    private Label moreCount; // New label to show "+N more..."
    @FXML
    private StackPane feedingBox;

    /**
     * Creates a {@code AnimalCode} with the given {@code Animal} and name to display.
     */
    public AnimalCard(Animal animal, int displayedIndex, List<FeedingSession> feedingSessions, List<Person> persons) {
        super(FXML);
        this.animal = animal;
        this.feedingSessions = feedingSessions;
        this.persons = persons;
        id.setText(displayedIndex + ". ");
        name.setText(animal.getName().fullName);
        description.setText(animal.getDescription().value);
        animalLocation.setText(animal.getLocation().value);
        animal.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        displayEarliestFeedingSession(feedingSessions);
    }

    /**
     * Displays the earliest feeding session in the feeding box.
     */
    private void displayEarliestFeedingSession(List<FeedingSession> feedingSessions) {
        FeedingSession earliest = animal.getEarliestFeedingSession(feedingSessions);

        if (earliest != null && feedingBox != null) {
            feedingBox.setVisible(true);
            feedingBox.setManaged(true);

            if (feederName != null) {
                String personNameText = persons.stream()
                    .filter(person -> person.getId().equals(earliest.getPersonId()))
                    .findFirst()
                    .map(person -> person.getName().fullName)
                    .orElse("Unknown Person");
                feederName.setText(personNameText);
            }

            if (feedingDate != null) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                feedingDate.setText(earliest.getDateTime().format(dateFormatter));
            }

            if (feedingTime != null) {
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                feedingTime.setText(earliest.getDateTime().format(timeFormatter));
            }

            // Set the "+N more..." label if there are additional sessions beyond the earliest one
            if (moreCount != null) {
                long totalSessions = feedingSessions == null ? 0
                        : feedingSessions.stream().filter(s -> s.involvesAnimal(animal.getId())).count();
                long remaining = Math.max(0, totalSessions - 1);
                if (remaining > 0) {
                    moreCount.setText("+" + remaining + " more...");
                    moreCount.setVisible(true);
                    moreCount.setManaged(true);
                } else {
                    moreCount.setText("");
                    moreCount.setVisible(false);
                    moreCount.setManaged(false);
                }
            }
        } else if (feedingBox != null) {
            feedingBox.setVisible(false);
            feedingBox.setManaged(false);
            if (moreCount != null) {
                moreCount.setText("");
                moreCount.setVisible(false);
                moreCount.setManaged(false);
            }
        }
    }
}
