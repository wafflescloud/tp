package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.UniqueAnimalList;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.feedingsession.UniqueFeedingSessionList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueAnimalList animals;
    private final UniqueFeedingSessionList feedingSessions;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        animals = new UniqueAnimalList();
        feedingSessions = new UniqueFeedingSessionList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the animal list with {@code animals}.
     * {@code animals} must not contain duplicate animals.
     */
    public void setAnimals(List<Animal> animals) {
        this.animals.setAnimals(animals);
    }

    /**
     * Replaces the contents of the feeding session list with {@code feedingSessions}.
     * {@code feedingSessions} must not contain duplicate feeding sessions.
     */
    public void setFeedingSessions(List<FeedingSession> feedingSessions) {
        this.feedingSessions.setFeedingSessions(feedingSessions);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setFeedingSessions(newData.getFeedingSessionList());
        setPersons(newData.getPersonList());
        setAnimals(newData.getAnimalList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    /**
     * Returns a person by their ID.
     * Returns null if not found.
     */
    public Person getPersonById(UUID id) {
        return persons.asUnmodifiableObservableList().stream()
                .filter(person -> person.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    //// animal-level operations

    /**
     * Returns true if an animal with the same identity as {@code animal} exists in the address book.
     */
    public boolean hasAnimal(Animal animal) {
        requireNonNull(animal);
        return animals.contains(animal);
    }

    /**
     * Adds an animal to the address book.
     * The animal must not already exist in the address book.
     */
    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    /**
     * Replaces the given animal {@code target} in the list with {@code editedAnimal}.
     * {@code target} must exist in the address book.
     * The animal identity of {@code editedAnimal} must not be the same as another existing animal in the address book.
     */
    public void setAnimal(Animal target, Animal editedAnimal) {
        requireNonNull(editedAnimal);

        animals.setAnimal(target, editedAnimal);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeAnimal(Animal key) {
        animals.remove(key);
    }

    /**
     * Returns an animal by their ID.
     * Returns null if not found.
     */
    public Animal getAnimalById(UUID id) {
        return animals.asUnmodifiableObservableList().stream()
                .filter(animal -> animal.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    //// feeding session operations

    /**
     * Adds a feeding session to the address book.
     * The feeding session must not already exist in the address book.
     */
    public void addFeedingSession(FeedingSession session) {
        feedingSessions.add(session);
    }

    /**
     * Removes the feeding session from the address book.
     * The feeding session must exist in the address book.
     */
    public void removeFeedingSession(FeedingSession key) {
        feedingSessions.remove(key);
    }

    /**
     * Returns true if a feeding session with the same identity exists in the address book.
     */
    public boolean hasFeedingSession(FeedingSession session) {
        requireNonNull(session);
        return feedingSessions.contains(session);
    }

    /**
     * Returns true if a feeding session with the given ID exists in the address book.
     */
    public boolean hasFeedingSessionById(UUID id) {
        requireNonNull(id);
        return feedingSessions.contains(id);
    }

    /**
     * Returns a feeding session by its ID.
     * Returns null if not found.
     */
    public FeedingSession getFeedingSessionById(UUID id) {
        return feedingSessions.getById(id);
    }

    /**
     * Replaces the feeding session {@code target} with {@code editedSession}.
     * {@code target} must exist in the address book.
     */
    public void setFeedingSession(FeedingSession target, FeedingSession editedSession) {
        requireNonNull(editedSession);
        feedingSessions.setFeedingSession(target, editedSession);
    }

    /**
     * Removes all feeding sessions associated with the given animal.
     */
    public void removeFeedingSessionsForAnimal(UUID animalId) {
        List<FeedingSession> sessionsToRemove = feedingSessions.asUnmodifiableObservableList().stream()
                .filter(session -> session.getAnimalId().equals(animalId))
                .collect(Collectors.toList());
        sessionsToRemove.forEach(feedingSessions::remove);
    }

    /**
     * Removes all feeding sessions associated with the given person.
     */
    public void removeFeedingSessionsForPerson(UUID personId) {
        List<FeedingSession> sessionsToRemove = feedingSessions.asUnmodifiableObservableList().stream()
                .filter(session -> session.getPersonId().equals(personId))
                .collect(Collectors.toList());
        sessionsToRemove.forEach(feedingSessions::remove);
    }

    //// util methods

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Animal> getAnimalList() {
        return animals.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<FeedingSession> getFeedingSessionList() {
        return feedingSessions.asUnmodifiableObservableList();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("animals", animals)
                .add("feedingSessions", feedingSessions)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons)
                && animals.equals(otherAddressBook.animals)
                && feedingSessions.equals(otherAddressBook.feedingSessions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persons, animals, feedingSessions);
    }
}
