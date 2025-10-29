package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Stack;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.animal.Animal;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Animal> filteredAnimals;

    private final Stack<State> undoStack;
    private final Stack<State> redoStack;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);
        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);
        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        this.filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        this.filteredAnimals = new FilteredList<>(this.addressBook.getAnimalList());
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    // =========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    // =========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    // =========== State ===============================================================================

    @Override
    public void saveState() {
        undoStack.push(new State(addressBook));
        redoStack.clear();
    }

    @Override
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    @Override
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    @Override
    public void undo() {
        assert canUndo() : "Implementation error: undo() called when canUndo() is false";
        redoStack.push(new State(addressBook));
        addressBook.resetData(undoStack.pop().getAddressBook());
    }

    @Override
    public void redo() {
        assert canRedo() : "Implementation error: redo() called when canRedo() is false";
        undoStack.push(new State(addressBook));
        addressBook.resetData(redoStack.pop().getAddressBook());
    }

    // =========== Person Operations =========================================================================

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        saveState();

        for (UUID sessionId : target.getFeedingSessionIds()) {
            FeedingSession session = addressBook.getFeedingSessionById(sessionId);
            if (session != null) {
                Animal animal = addressBook.getAnimalById(session.getAnimalId());
                if (animal != null) {
                    Animal updatedAnimal = animal.removeFeedingSessionId(sessionId);
                    addressBook.setAnimal(animal, updatedAnimal);
                }
            } else {
                for (Animal animal : addressBook.getAnimalList()) {
                    if (animal.getFeedingSessionIds().contains(sessionId)) {
                        Animal updatedAnimal = animal.removeFeedingSessionId(sessionId);
                        addressBook.setAnimal(animal, updatedAnimal);
                    }
                }
            }
        }

        removeFeedingSessionsForPerson(target.getId());

        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        saveState();
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        saveState();
        requireAllNonNull(target, editedPerson);
        addressBook.setPerson(target, editedPerson);
    }

    // =========== Animal Operations =========================================================================

    @Override
    public boolean hasAnimal(Animal animal) {
        requireNonNull(animal);
        return addressBook.hasAnimal(animal);
    }

    @Override
    public void deleteAnimal(Animal target) {
        saveState();

        for (UUID sessionId : target.getFeedingSessionIds()) {
            FeedingSession session = addressBook.getFeedingSessionById(sessionId);
            if (session != null) {
                Person person = addressBook.getPersonById(session.getPersonId());
                if (person != null) {
                    Person updatedPerson = person.removeFeedingSessionId(sessionId);
                    addressBook.setPerson(person, updatedPerson);
                }
            } else {
                for (Person person : addressBook.getPersonList()) {
                    if (person.getFeedingSessionIds().contains(sessionId)) {
                        Person updatedPerson = person.removeFeedingSessionId(sessionId);
                        addressBook.setPerson(person, updatedPerson);
                    }
                }
            }
        }

        removeFeedingSessionsForAnimal(target.getId());

        addressBook.removeAnimal(target);
    }

    @Override
    public void addAnimal(Animal animal) {
        saveState();
        addressBook.addAnimal(animal);
        updateFilteredAnimalList(PREDICATE_SHOW_ALL_ANIMALS);
    }

    @Override
    public void setAnimal(Animal target, Animal editedAnimal) {
        saveState();
        requireAllNonNull(target, editedAnimal);
        addressBook.setAnimal(target, editedAnimal);
    }

    // =========== Filtered Person List Accessors =============================================================

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    // =========== Filtered Animal List Accessors =============================================================

    @Override
    public ObservableList<Animal> getFilteredAnimalList() {
        return filteredAnimals;
    }

    @Override
    public void updateFilteredAnimalList(Predicate<Animal> predicate) {
        requireNonNull(predicate);
        filteredAnimals.setPredicate(predicate);
    }

    // =========== FeedingSession Operations ================================================================

    @Override
    public void addFeedingSession(FeedingSession feedingSession) {
        requireNonNull(feedingSession);
        addressBook.addFeedingSession(feedingSession);
    }

    @Override
    public void deleteFeedingSession(FeedingSession feedingSession) {
        requireNonNull(feedingSession);
        saveState();
        addressBook.removeFeedingSession(feedingSession);
    }

    @Override
    public boolean hasFeedingSession(FeedingSession feedingSession) {
        requireNonNull(feedingSession);
        return addressBook.hasFeedingSession(feedingSession);
    }

    @Override
    public FeedingSession getFeedingSessionById(UUID id) {
        requireNonNull(id);
        return addressBook.getFeedingSessionById(id);
    }

    @Override
    public Person getPersonById(UUID id) {
        requireNonNull(id);
        return addressBook.getPersonById(id);
    }

    @Override
    public Animal getAnimalById(UUID id) {
        requireNonNull(id);
        return addressBook.getAnimalById(id);
    }

    @Override
    public void removeFeedingSessionsForAnimal(UUID animalId) {
        requireNonNull(animalId);
        addressBook.removeFeedingSessionsForAnimal(animalId);
    }

    @Override
    public void removeFeedingSessionsForPerson(UUID personId) {
        requireNonNull(personId);
        addressBook.removeFeedingSessionsForPerson(personId);
    }

    // =========== Feeding Session Composite Operations =====================================================

    @Override
    public void addFeedingSessionWithUpdates(FeedingSession feedingSession, Person person, Person updatedPerson,
                                             Animal animal, Animal updatedAnimal) {
        requireAllNonNull(feedingSession, person, updatedPerson, animal, updatedAnimal);
        saveState();
        addressBook.addFeedingSession(feedingSession);
        addressBook.setPerson(person, updatedPerson);
        addressBook.setAnimal(animal, updatedAnimal);
    }

    @Override
    public void deleteFeedingSessionWithUpdates(FeedingSession feedingSession, Person person, Person updatedPerson,
                                                Animal animal, Animal updatedAnimal) {
        requireAllNonNull(feedingSession, person, updatedPerson, animal, updatedAnimal);
        saveState();
        addressBook.removeFeedingSession(feedingSession);
        addressBook.setPerson(person, updatedPerson);
        addressBook.setAnimal(animal, updatedAnimal);
    }

    // =========== Feeding Session List Accessors ===========================================================

    @Override
    public ObservableList<FeedingSession> getFeedingSessionList() {
        return addressBook.getFeedingSessionList();
    }

    // =========== Utility Methods ==========================================================================

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && filteredAnimals.equals(otherModelManager.filteredAnimals);
    }
}
