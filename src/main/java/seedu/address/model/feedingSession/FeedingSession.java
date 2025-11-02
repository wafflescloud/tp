package seedu.address.model.feedingsession;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a FeedingSession in the address book.
 * A FeedingSession links a Person who fed an Animal at a specific date and time,
 * with optional notes about the feeding.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class FeedingSession {
    private final UUID id;
    private final UUID animalId;
    private final UUID personId;
    private final LocalDateTime dateTime;

    /**
     * Constructs a FeedingSession with auto-generated ID.
     * Every field must be present and not null.
     *
     * @param animalId The UUID of the animal being fed.
     * @param personId The UUID of the person feeding the animal.
     * @param dateTime The date and time when the feeding occurred.
     */
    public FeedingSession(UUID animalId, UUID personId, LocalDateTime dateTime) {
        requireNonNull(animalId);
        requireNonNull(personId);
        requireNonNull(dateTime);
        this.id = UUID.randomUUID();
        this.animalId = animalId;
        this.personId = personId;
        this.dateTime = dateTime;
    }

    /**
     * Constructs a FeedingSession with explicit ID (for deserialization).
     * Every field must be present and not null.
     *
     * @param id The unique identifier for this feeding session.
     * @param animalId The UUID of the animal being fed.
     * @param personId The UUID of the person feeding the animal.
     * @param dateTime The date and time when the feeding occurred.
     */
    public FeedingSession(UUID id, UUID animalId, UUID personId, LocalDateTime dateTime) {
        requireNonNull(id);
        requireNonNull(animalId);
        requireNonNull(personId);
        requireNonNull(dateTime);
        this.id = id;
        this.animalId = animalId;
        this.personId = personId;
        this.dateTime = dateTime;
    }

    /**
     * Returns the unique identifier of this feeding session.
     *
     * @return The UUID of this feeding session.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the UUID of the animal that was fed.
     *
     * @return The UUID of the animal.
     */
    public UUID getAnimalId() {
        return animalId;
    }

    /**
     * Returns the UUID of the person who fed the animal.
     *
     * @return The UUID of the person.
     */
    public UUID getPersonId() {
        return personId;
    }

    /**
     * Returns the date and time when the feeding occurred.
     *
     * @return The LocalDateTime of the feeding.
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Returns the date and time when the feeding occurred.
     * This is an alias for {@link #getDateTime()}.
     *
     * @return The LocalDateTime of the feeding.
     */
    public LocalDateTime getFeedingTime() {
        return dateTime;
    }

    /**
     * Returns true if both feeding sessions have the same identity (ID).
     * This defines a weaker notion of equality between two feeding sessions.
     *
     * @param otherSession The other feeding session to compare with.
     * @return True if both feeding sessions have the same ID.
     */
    public boolean isSameFeedingSession(FeedingSession otherSession) {
        if (otherSession == this) {
            return true;
        }

        return otherSession != null && otherSession.getId().equals(getId());
    }

    /**
     * Returns true if both feeding sessions have the same ID.
     * This defines a stronger notion of equality between two feeding sessions.
     *
     * @param other The other object to compare with.
     * @return True if both objects are FeedingSessions with the same ID.
     */
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

    /**
     * Returns the hash code of this feeding session based on its ID.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns true if this feeding session involves the given person.
     *
     * @param personId The UUID of the person to check.
     * @return True if this feeding session's personId matches the given personId.
     */
    public boolean involvesPerson(UUID personId) {
        return this.personId.equals(personId);
    }

    /**
     * Returns true if this feeding session involves the given animal.
     *
     * @param animalId The UUID of the animal to check.
     * @return True if this feeding session's animalId matches the given animalId.
     */
    public boolean involvesAnimal(UUID animalId) {
        return this.animalId.equals(animalId);
    }

    /**
     * Returns true if this feeding session occurred earlier than the other.
     *
     * @param other The other feeding session to compare with.
     * @return True if this feeding session's dateTime is before the other's dateTime.
     */
    public boolean isEarlierThan(FeedingSession other) {
        return this.dateTime.isBefore(other.dateTime);
    }

    /**
     * Returns a string representation of this feeding session.
     *
     * @return A string containing all fields of this feeding session.
     */
    @Override
    public String toString() {
        return String.format("FeedingSession[id=%s, animalId=%s, personId=%s, dateTime=%s, notes=%s]",
                id, animalId, personId, dateTime);
    }
}
