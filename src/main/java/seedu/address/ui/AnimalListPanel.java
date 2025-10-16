package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.animal.Animal;

/**
 * Panel containing the list of animals.
 */
public class AnimalListPanel extends UiPart<Region> {
    private static final String FXML = "AnimalListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(AnimalListPanel.class);

    @FXML
    private ListView<Animal> animalListView;

    /**
     * Creates a {@code AnimalListPanel} with the given {@code ObservableList}.
     */
    public AnimalListPanel(ObservableList<Animal> personList) {
        super(FXML);
        animalListView.setItems(personList);
        animalListView.setCellFactory(listView -> new AnimalListViewCell());
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
                setGraphic(new AnimalCard(animal, getIndex() + 1).getRoot());
            }
        }
    }

}
