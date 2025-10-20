package seedu.address.model.feedingsession;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;

/**
 * Represents an intermediate feeding session with animal name and datetime before animal validation.
 * Used during parsing before the actual animal reference can be resolved.
 */
public class IntermediateFeedingSession {
    private final String animalName;
    private final LocalDateTime dateTime;

    /**
     * Creates a new IntermediateFeedingSession.
     *
     * @param animalName The name of the animal to be fed
     * @param dateTime The date and time of the feeding session
     * @throws NullPointerException if any field is null
     */
    public IntermediateFeedingSession(String animalName, LocalDateTime dateTime) {
        requireNonNull(animalName);
        requireNonNull(dateTime);
        this.animalName = animalName;
        this.dateTime = dateTime;
    }

    public String getAnimalName() {
        return animalName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return String.format("%s at %s", animalName, dateTime);
    }
}
