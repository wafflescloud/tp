package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.animal.Animal;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.person.Person;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_ANIMAL = "Animals list contains duplicate animal(s).";
    public static final String MESSAGE_DUPLICATE_FEEDING_SESSION =
            "Feeding sessions list contains duplicate session(s).";
    public static final String MESSAGE_ORPHANED_FEEDING_SESSION =
            "Feeding session references non-existent animal or person.";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedAnimal> animals = new ArrayList<>();
    private final List<JsonAdaptedFeedingSession> feedingSessions = new ArrayList<>();

    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                       @JsonProperty("animals") List<JsonAdaptedAnimal> animals,
                                       @JsonProperty("feedingSessions")
                                       List<JsonAdaptedFeedingSession> feedingSessions) {
        if (persons != null) {
            this.persons.addAll(persons);
        }
        if (animals != null) {
            this.animals.addAll(animals);
        }
        if (feedingSessions != null) {
            this.feedingSessions.addAll(feedingSessions);
        }
    }

    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream()
                .map(JsonAdaptedPerson::new)
                .collect(Collectors.toList()));
        animals.addAll(source.getAnimalList().stream()
                .map(JsonAdaptedAnimal::new)
                .collect(Collectors.toList()));
        feedingSessions.addAll(source.getFeedingSessionList().stream()
                .map(JsonAdaptedFeedingSession::new)
                .collect(Collectors.toList()));
    }

    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();

        boolean hasExplicitSessions = !feedingSessions.isEmpty();

        for (JsonAdaptedFeedingSession jsonAdaptedSession : feedingSessions) {
            FeedingSession session = jsonAdaptedSession.toModelType();
            if (addressBook.hasFeedingSession(session)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_FEEDING_SESSION);
            }
            addressBook.addFeedingSession(session);
        }

        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }

            if (hasExplicitSessions) {
                for (String sessionId : jsonAdaptedPerson.getFeedingSessionIds()) {
                    UUID uuid = UUID.fromString(sessionId);
                    if (!addressBook.hasFeedingSessionById(uuid)) {
                        throw new IllegalValueException(MESSAGE_ORPHANED_FEEDING_SESSION);
                    }
                }
            } else {
                person = new seedu.address.model.person.Person(person.getId(), person.getPersonName(),
                        person.getPhone(), person.getEmail(), person.getTags(), new java.util.HashSet<>());
            }

            addressBook.addPerson(person);
        }

        for (JsonAdaptedAnimal jsonAdaptedAnimal : animals) {
            Animal animal = jsonAdaptedAnimal.toModelType();
            if (addressBook.hasAnimal(animal)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_ANIMAL);
            }

            if (hasExplicitSessions) {
                for (String sessionId : jsonAdaptedAnimal.getFeedingSessionIds()) {
                    UUID uuid = UUID.fromString(sessionId);
                    if (!addressBook.hasFeedingSessionById(uuid)) {
                        throw new IllegalValueException(MESSAGE_ORPHANED_FEEDING_SESSION);
                    }
                }
            } else {
                animal = new seedu.address.model.animal.Animal(animal.getId(), animal.getAnimalName(),
                        animal.getDescription(), animal.getLocation(), animal.getTags(), new java.util.HashSet<>());
            }

            addressBook.addAnimal(animal);
        }

        return addressBook;
    }

}
