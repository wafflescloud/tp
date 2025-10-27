package seedu.address.model.feedingsession;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;

import seedu.address.model.animal.Animal;
import seedu.address.model.person.Person;

/**
 * Represents a feeding session in the address book.
 * An association class between Person and Animal with a feeding time.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class FeedingSession {
    private final Person person;
    private final Animal animal;
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
        this.person = person;
        this.animal = animal;
        this.feedingTime = feedingTime;
    }

    /**
     * Returns the person feeding the animal.
     *
     * @return the person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Returns the animal to be fed.
     *
     * @return the animal
     */
    public Animal getAnimal() {
        return animal;
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
        return person.equals(otherSession.person)
                && animal.equals(otherSession.animal)
                && feedingTime.equals(otherSession.feedingTime);
    }

    @Override
    public String toString() {
        return String.format("Feeding Session: %s feeds %s at %s",
                person.getName(), animal.getName(), feedingTime);
    }
}
