package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
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

        removeFeedingSessionsForDeletedPerson(target.getName().toString());

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

        boolean nameChanged = !target.getName().equals(editedPerson.getName());

        addressBook.setPerson(target, editedPerson);

        if (nameChanged) {
            updateFeedingSessionsForPersonNameChange(
                    target.getName().toString(),
                    editedPerson.getName().toString()
            );
        }
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

        removeFeedingSessionsForDeletedAnimal(target.getName().toString());

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

        boolean nameChanged = !target.getName().equals(editedAnimal.getName());

        addressBook.setAnimal(target, editedAnimal);

        if (nameChanged) {
            updateFeedingSessionsForAnimalNameChange(
                    target.getName().toString(),
                    editedAnimal.getName().toString()
            );
        }
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

    // =========== Utility Methods ==========================================================================

    /**
     * Updates all feeding sessions in all animals when a person's name changes.
     * Creates new Animal objects with updated feeding sessions.
     *
     * @param oldName The old name of the person
     * @param newName The new name of the person
     */
    private void updateFeedingSessionsForPersonNameChange(String oldName, String newName) {
        ObservableList<Animal> animalList = addressBook.getAnimalList();

        for (Animal animal : animalList) {
            Set<FeedingSession> oldSessions = animal.getFeedingSessions();
            Set<FeedingSession> newSessions = new HashSet<>();
            boolean hasChanges = false;

            for (FeedingSession session : oldSessions) {
                if (session.getPersonName().equals(oldName)) {
                    newSessions.add(new FeedingSession(newName, session.getAnimalName(),
                            session.getFeedingTime()));
                    hasChanges = true;
                } else {
                    newSessions.add(session);
                }
            }

            if (hasChanges) {
                Animal updatedAnimal = new Animal(
                        animal.getName(),
                        animal.getDescription(),
                        animal.getLocation(),
                        animal.getTags(),
                        newSessions
                );
                addressBook.setAnimal(animal, updatedAnimal);
            }
        }
    }

    /**
     * Updates all feeding sessions in all persons when an animal's name changes.
     * Creates new Person objects with updated feeding sessions.
     *
     * @param oldName The old name of the animal
     * @param newName The new name of the animal
     */
    private void updateFeedingSessionsForAnimalNameChange(String oldName, String newName) {
        ObservableList<Person> personList = addressBook.getPersonList();

        for (Person person : personList) {
            Set<FeedingSession> oldSessions = person.getFeedingSessions();
            Set<FeedingSession> newSessions = new HashSet<>();
            boolean hasChanges = false;

            for (FeedingSession session : oldSessions) {
                if (session.getAnimalName().equals(oldName)) {
                    newSessions.add(new FeedingSession(session.getPersonName(), newName,
                            session.getFeedingTime()));
                    hasChanges = true;
                } else {
                    newSessions.add(session);
                }
            }

            if (hasChanges) {
                Person updatedPerson = new Person(
                        person.getName(),
                        person.getPhone(),
                        person.getEmail(),
                        person.getTags(),
                        newSessions
                );
                addressBook.setPerson(person, updatedPerson);
            }
        }
    }

    /**
     * Removes all feeding sessions involving a deleted person from all animals.
     * Creates new Animal objects without the deleted person's feeding sessions.
     *
     * @param deletedPersonName The name of the deleted person
     */
    private void removeFeedingSessionsForDeletedPerson(String deletedPersonName) {
        ObservableList<Animal> animalList = addressBook.getAnimalList();

        for (Animal animal : animalList) {
            Set<FeedingSession> oldSessions = animal.getFeedingSessions();
            Set<FeedingSession> newSessions = new HashSet<>();
            boolean hasChanges = false;

            for (FeedingSession session : oldSessions) {
                if (session.getPersonName().equals(deletedPersonName)) {
                    hasChanges = true;
                } else {
                    newSessions.add(session);
                }
            }

            if (hasChanges) {
                Animal updatedAnimal = new Animal(
                        animal.getName(),
                        animal.getDescription(),
                        animal.getLocation(),
                        animal.getTags(),
                        newSessions
                );
                addressBook.setAnimal(animal, updatedAnimal);
            }
        }
    }

    /**
     * Removes all feeding sessions involving a deleted animal from all persons.
     * Creates new Person objects without the deleted animal's feeding sessions.
     *
     * @param deletedAnimalName The name of the deleted animal
     */
    private void removeFeedingSessionsForDeletedAnimal(String deletedAnimalName) {
        ObservableList<Person> personList = addressBook.getPersonList();

        for (Person person : personList) {
            Set<FeedingSession> oldSessions = person.getFeedingSessions();
            Set<FeedingSession> newSessions = new HashSet<>();
            boolean hasChanges = false;

            for (FeedingSession session : oldSessions) {
                if (session.getAnimalName().equals(deletedAnimalName)) {
                    hasChanges = true;
                } else {
                    newSessions.add(session);
                }
            }

            if (hasChanges) {
                Person updatedPerson = new Person(
                        person.getName(),
                        person.getPhone(),
                        person.getEmail(),
                        person.getTags(),
                        newSessions
                );
                addressBook.setPerson(person, updatedPerson);
            }
        }
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
