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
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;
    private final List<FeedingSession> feedingSessions;
    private final List<Animal> animals;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private Label animalName;
    @FXML
    private Label feedingDate;
    @FXML
    private Label feedingTime;
    @FXML
    private StackPane feedingBox;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex, List<FeedingSession> feedingSessions, List<Animal> animals) {
        super(FXML);
        this.person = person;
        this.feedingSessions = feedingSessions;
        this.animals = animals;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        email.setText(person.getEmail().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        displayEarliestFeedingSession(feedingSessions);
    }

    /**
     * Displays the earliest feeding session in the feeding box.
     */
    private void displayEarliestFeedingSession(List<FeedingSession> feedingSessions) {
        FeedingSession earliest = person.getEarliestFeedingSession(feedingSessions);

        if (earliest != null && feedingBox != null) {
            feedingBox.setVisible(true);
            feedingBox.setManaged(true);

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
        } else if (feedingBox != null) {
            feedingBox.setVisible(false);
            feedingBox.setManaged(false);
        }
    }
}
