package seedu.address.ui;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
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

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
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
    private Rectangle feedingBox;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        // Set feeding session details if available
        if (!person.getFeedingSessions().isEmpty()) {
            // Get the first feeding session (can be enhanced to show multiple later)
            FeedingSession firstSession = person.getFeedingSessions().iterator().next();
            animalName.setText(firstSession.getAnimal().getName().fullName);
            feedingDate.setText(firstSession.getFeedingTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            feedingTime.setText(firstSession.getFeedingTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            feedingBox.setVisible(true);
        } else {
            // Hide feeding session labels if no sessions exist
            animalName.setVisible(false);
            feedingDate.setVisible(false);
            feedingTime.setVisible(false);
            feedingBox.setVisible(false);
        }
    }
}
