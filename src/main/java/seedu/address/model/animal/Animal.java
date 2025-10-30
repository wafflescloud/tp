package seedu.address.model.animal;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Name;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.tag.Tag;

/**
 * Represents an Animal in the pet store.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Animal {

    // Identity fields
    private final UUID id;
    private final Name name;
    private final Description description;
    private final Location location;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();
    private final Set<UUID> feedingSessionIds = new HashSet<>();

    /**
     * Every field must be present and not null.
     * Creates a new Animal with auto-generated ID.
     */
    public Animal(Name name, Description description, Location location, Set<Tag> tags,
                  Set<UUID> feedingSessionIds) {
        requireAllNonNull(name, description, location);
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.location = location;
        this.tags.addAll(tags);
        this.feedingSessionIds.addAll(feedingSessionIds);
    }

    /**
     * Constructor with explicit ID (for deserialization).
     */
    public Animal(UUID id, Name name, Description description, Location location, Set<Tag> tags,
                  Set<UUID> feedingSessionIds) {
        requireAllNonNull(id, name, description, location);
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.tags.addAll(tags);
        this.feedingSessionIds.addAll(feedingSessionIds);
    }

    public UUID getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable feeding session IDs set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<UUID> getFeedingSessionIds() {
        return Collections.unmodifiableSet(feedingSessionIds);
    }

    /**
     * Returns a new Animal with the given feeding session ID added.
     */
    public Animal addFeedingSessionId(UUID sessionId) {
        Set<UUID> updatedSessions = new HashSet<>(feedingSessionIds);
        updatedSessions.add(sessionId);
        return new Animal(id, name, description, location, tags, updatedSessions);
    }

    /**
     * Returns a new Animal with the given feeding session ID removed.
     */
    public Animal removeFeedingSessionId(UUID sessionId) {
        Set<UUID> updatedSessions = new HashSet<>(feedingSessionIds);
        updatedSessions.remove(sessionId);
        return new Animal(id, name, description, location, tags, updatedSessions);
    }

    /**
     * Returns the earliest feeding session this animal is involved in from the given list.
     * @param allSessions List of all feeding sessions to search from
     * @return The earliest feeding session, or null if none found
     */
    public FeedingSession getEarliestFeedingSession(List<FeedingSession> allSessions) {
        if (allSessions == null || allSessions.isEmpty()) {
            return null;
        }

        return allSessions.stream()
                .filter(session -> session.involvesAnimal(this.id))
                .min(Comparator.comparing(FeedingSession::getDateTime))
                .orElse(null);
    }

    /**
     * Returns a formatted string of the earliest feeding session, or empty string if none.
     * @param allSessions List of all feeding sessions to search from
     * @return Formatted string showing next feeding time, or empty string
     */
    public String getEarliestFeedingSessionDisplay(List<FeedingSession> allSessions) {
        FeedingSession earliest = getEarliestFeedingSession(allSessions);
        if (earliest == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        return String.format("Next feeding: %s", earliest.getDateTime().format(formatter));
    }

    /**
     * Returns true if both animals have the same name.
     * This defines a weaker notion of equality between two animals.
     */
    public boolean isSameAnimal(Animal otherAnimal) {
        if (otherAnimal == this) {
            return true;
        }

        return otherAnimal != null
                && otherAnimal.getName().equals(getName());
    }

    /**
     * Returns true if both animals have the same identity and data fields.
     * This defines a stronger notion of equality between two animals.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Animal)) {
            return false;
        }

        Animal otherAnimal = (Animal) other;
        return id.equals(otherAnimal.id)
                && name.equals(otherAnimal.name)
                && description.equals(otherAnimal.description)
                && location.equals(otherAnimal.location)
                && tags.equals(otherAnimal.tags)
                && feedingSessionIds.equals(otherAnimal.feedingSessionIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, location, tags, feedingSessionIds);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("id", id)
                .add("name", name)
                .add("description", description)
                .add("location", location)
                .add("tags", tags)
                .add("feeding session IDs", feedingSessionIds)
                .toString();
    }
}
