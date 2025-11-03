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
 * Panel containing the list of animals.
 */
public class AnimalListPanel extends UiPart<Region> {
    private static final String FXML = "AnimalListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(AnimalListPanel.class);

    @FXML
    private ListView<Animal> animalListView;

    private final ObservableList<FeedingSession> feedingSessionList;
    private final ObservableList<Person> personList;

    /**
     * Creates a {@code AnimalListPanel} with the given {@code ObservableList}.
     */
    public AnimalListPanel(ObservableList<Animal> animalList, ObservableList<FeedingSession> feedingSessionList,
                          ObservableList<Person> personList) {
        super(FXML);
        this.feedingSessionList = feedingSessionList;
        this.personList = personList;
        animalListView.setItems(animalList);
        animalListView.setCellFactory(listView -> new AnimalListViewCell());

        feedingSessionList.addListener((ListChangeListener<FeedingSession>) c -> {
            animalListView.refresh();
        });

        personList.addListener((ListChangeListener<Person>) c -> {
            animalListView.refresh();
        });

        animalListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Animal selectedAnimal = animalListView.getSelectionModel().getSelectedItem();
                if (selectedAnimal != null) {
                    AnimalProfileWindow.openProfile(selectedAnimal, feedingSessionList, personList);
                }
            }
        });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Animal} using a {@code AnimalCard}.
     */
    class AnimalListViewCell extends ListCell<Animal> {
        @Override
        protected void updateItem(Animal animal, boolean empty) {
            super.updateItem(animal, empty);

            if (empty || animal == null) {
                setGraphic(null);
                setText(null);
            } else {
                AnimalCard card = new AnimalCard(animal, getIndex() + 1, feedingSessionList, personList);
                Region root = card.getRoot();
                root.setMinWidth(0);
                root.setMaxWidth(Double.MAX_VALUE);
                root.prefWidthProperty().bind(animalListView.widthProperty().subtract(16));
                setGraphic(root);
            }
        }
    }

}
