package seedu.address.model.feedingsession;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a FeedingSession in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class FeedingSession {
    private final UUID id;
    private final UUID animalId;
    private final UUID personId;
    private final LocalDateTime dateTime;
    private final String notes;

    /**
     * Every field must be present and not null.
     * Creates a new feeding session with auto-generated ID.
     */
    public FeedingSession(UUID animalId, UUID personId, LocalDateTime dateTime, String notes) {
        requireNonNull(animalId);
        requireNonNull(personId);
        requireNonNull(dateTime);
        this.id = UUID.randomUUID();
        this.animalId = animalId;
        this.personId = personId;
        this.dateTime = dateTime;
        this.notes = notes != null ? notes : "";
    }

    /**
     * Constructor with explicit ID (for deserialization).
     */
    public FeedingSession(UUID id, UUID animalId, UUID personId, LocalDateTime dateTime, String notes) {
        requireNonNull(id);
        requireNonNull(animalId);
        requireNonNull(personId);
        requireNonNull(dateTime);
        this.id = id;
        this.animalId = animalId;
        this.personId = personId;
        this.dateTime = dateTime;
        this.notes = notes != null ? notes : "";
    }

    public UUID getId() {
        return id;
    }

    public UUID getAnimalId() {
        return animalId;
    }

    public UUID getPersonId() {
        return personId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public LocalDateTime getFeedingTime() {
        return dateTime;
    }

    public String getNotes() {
        return notes;
    }

    /**
     * Returns true if both feeding sessions have the same identity (ID).
     */
    public boolean isSameFeedingSession(FeedingSession otherSession) {
        if (otherSession == this) {
            return true;
        }

        return otherSession != null && otherSession.getId().equals(getId());
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
        return id.equals(otherSession.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns true if this feeding session involves the given person.
     */
    public boolean involvesPerson(UUID personId) {
        return this.personId.equals(personId);
    }

    /**
     * Returns true if this feeding session involves the given animal.
     */
    public boolean involvesAnimal(UUID animalId) {
        return this.animalId.equals(animalId);
    }

    /**
     * Returns true if this feeding session is earlier than the other.
     */
    public boolean isEarlierThan(FeedingSession other) {
        return this.dateTime.isBefore(other.dateTime);
    }

    @Override
    public String toString() {
        return String.format("FeedingSession[id=%s, animalId=%s, personId=%s, dateTime=%s, notes=%s]",
                id, animalId, personId, dateTime, notes);
    }
}
