package seedu.address.model.feedingsession;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;

import seedu.address.model.animal.Animal;

/**
 * Represents a feeding session in the address book.
 * Contains an animal and its feeding time.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class FeedingSession {
    private final Animal animal;
    private final LocalDateTime feedingTime;

    /**
     * Every field must be present and not null.
     * Creates a new feeding session with the specified animal and feeding time.
     *
     * @param animal The animal to be fed
     * @param feedingTime The date and time when the feeding should occur
     * @throws NullPointerException if any of the fields are null
     */
    public FeedingSession(Animal animal, LocalDateTime feedingTime) {
        requireNonNull(animal);
        requireNonNull(feedingTime);
        this.animal = animal;
        this.feedingTime = feedingTime;
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
        return animal.equals(otherSession.animal)
                && feedingTime.equals(otherSession.feedingTime);
    }

    @Override
    public String toString() {
        return String.format("Feeding Session: %s at %s",
                animal.getName(), feedingTime);
    }
}
