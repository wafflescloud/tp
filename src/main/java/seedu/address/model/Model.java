package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.animal.Animal;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;
    Predicate<Animal> PREDICATE_SHOW_ALL_ANIMALS = unused -> true;

    // =========== UserPrefs ==================================================================================

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    // =========== AddressBook ===============================================================================

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    // =========== State ===============================================================================

    /**
     *  Returns true if the model has previous address book states to restore.
     */
    boolean canUndo();

    /**
     * Returns true if the model has undone address book states to restore.
     */
    boolean canRedo();

    /**
     * Restores the model's address book to its previous state.
     */
    void undo();

    /**
     * Restores the model's address book to its previously undone state.
     */
    void redo();

    /**
     * Saves the current address book state for undo/redo.
     */
    void saveState();

    // =========== Person Operations =========================================================================

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    // =========== Filtered Person List Accessors ============================================================

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    // =========== Animal Operations ========================================================================

    /**
     * Returns true if an animal with the same identity as {@code animal} exists in the address book.
     */
    boolean hasAnimal(Animal animal);

    /**
     * Deletes the given animal.
     * The animal must exist in the address book.
     */
    void deleteAnimal(Animal target);

    /**
     * Adds the given animal.
     * {@code animal} must not already exist in the address book.
     */
    void addAnimal(Animal animal);

    /**
     * Replaces the given animal {@code target} with {@code editedAnimal}.
     * {@code target} must exist in the address book.
     * The animal identity of {@code editedAnimal} must not be the same as another existing animal in the address book.
     */
    void setAnimal(Animal target, Animal editedAnimal);

    // =========== Filtered Animal List Accessors ===========================================================

    /** Returns an unmodifiable view of the filtered animal list */
    ObservableList<Animal> getFilteredAnimalList();

    /**
     * Updates the filter of the filtered animal list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredAnimalList(Predicate<Animal> predicate);
}
