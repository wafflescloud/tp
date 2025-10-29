package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.animal.Animal;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.person.Person;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the animals list.
     * This list will not contain any duplicate animals.
     */
    ObservableList<Animal> getAnimalList();

    /**
     * Returns an unmodifiable view of the feeding sessions list.
     * This list will not contain any duplicate feeding sessions.
     */
    ObservableList<FeedingSession> getFeedingSessionList();
}
