package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.animal.Animal;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<Person> personListView;

    private final ObservableList<FeedingSession> feedingSessionList;
    private final ObservableList<Animal> animalList;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList, ObservableList<FeedingSession> feedingSessionList,
                          ObservableList<Animal> animalList) {
        super(FXML);
        this.feedingSessionList = feedingSessionList;
        this.animalList = animalList;
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());

        feedingSessionList.addListener((ListChangeListener<FeedingSession>) c -> {
            personListView.refresh();
        });

        animalList.addListener((ListChangeListener<Animal>) c -> {
            personListView.refresh();
        });

        personListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Person selectedPerson = personListView.getSelectionModel().getSelectedItem();
                if (selectedPerson != null) {
                    PersonProfileWindow.openProfile(selectedPerson, feedingSessionList, animalList);
                }
            }
        });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1,
                    feedingSessionList, animalList).getRoot());
            }
        }
    }

}
