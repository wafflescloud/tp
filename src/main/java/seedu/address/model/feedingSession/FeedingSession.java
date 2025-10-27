package seedu.address.model.feedingsession;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;

import seedu.address.model.animal.Animal;
import seedu.address.model.person.Person;

/**
 * Represents a feeding session in the address book.
 * An association class between Person and Animal with a feeding time.
 * Stores only the names of the person and animal to prevent circular references.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class FeedingSession {
    private final String personName;
    private final String animalName;
    private final LocalDateTime feedingTime;

    /**
     * Every field must be present and not null.
     * Creates a new feeding session with the specified person, animal and feeding time.
     *
     * @param person The person feeding the animal
     * @param animal The animal to be fed
     * @param feedingTime The date and time when the feeding should occur
     * @throws NullPointerException if any of the fields are null
     */
    public FeedingSession(Person person, Animal animal, LocalDateTime feedingTime) {
        requireNonNull(person);
        requireNonNull(animal);
        requireNonNull(feedingTime);
        this.personName = person.getName().toString();
        this.animalName = animal.getName().toString();
        this.feedingTime = feedingTime;
    }

    /**
     * Creates a new feeding session with the specified person name, animal name and feeding time.
     * This constructor is useful when reconstructing from storage.
     *
     * @param personName The name of the person feeding the animal
     * @param animalName The name of the animal to be fed
     * @param feedingTime The date and time when the feeding should occur
     * @throws NullPointerException if any of the fields are null
     */
    public FeedingSession(String personName, String animalName, LocalDateTime feedingTime) {
        requireNonNull(personName);
        requireNonNull(animalName);
        requireNonNull(feedingTime);
        this.personName = personName;
        this.animalName = animalName;
        this.feedingTime = feedingTime;
    }

    /**
     * Returns the name of the person feeding the animal.
     *
     * @return the person's name
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * Returns the name of the animal to be fed.
     *
     * @return the animal's name
     */
    public String getAnimalName() {
        return animalName;
    }

    /**
     * Returns the scheduled feeding time.
     *
     * @return the date and time of the feeding session
     */
    public LocalDateTime getFeedingTime() {
        return feedingTime;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FeedingSession)) {
            return false;
        }

        FeedingSession otherSession = (FeedingSession) other;
        return personName.equals(otherSession.personName)
                && animalName.equals(otherSession.animalName)
                && feedingTime.equals(otherSession.feedingTime);
    }

    @Override
    public String toString() {
        return String.format("Feeding Session: %s feeds %s at %s",
                personName, animalName, feedingTime);
    }
}
