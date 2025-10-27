package seedu.address.ui;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.animal.Animal;
import seedu.address.model.feedingsession.FeedingSession;


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
    private StackPane feedingBox;

    /**
     * Creates a {@code AnimalCode} with the given {@code Animal} and name to display.
     */
    public AnimalCard(Animal animal, int displayedIndex) {
        super(FXML);
        this.animal = animal;
        id.setText(displayedIndex + ". ");
        name.setText(animal.getName().fullName);
        description.setText(animal.getDescription().value);
        animalLocation.setText(animal.getLocation().value);
        animal.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        // Set feeding session details if available
        if (!animal.getFeedingSessions().isEmpty()) {
            FeedingSession firstSession = animal.getFeedingSessions().iterator().next();
            feederName.setText(firstSession.getPerson().getName().fullName);
            feedingDate.setText(firstSession.getFeedingTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            feedingTime.setText(firstSession.getFeedingTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            feedingBox.setVisible(true);
            feedingBox.setManaged(true);
        } else {
            feedingBox.setVisible(false);
            feedingBox.setManaged(false);
        }
    }
}
