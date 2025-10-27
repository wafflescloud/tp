package seedu.address.model.animal;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.tag.Tag;

/**
 * Represents an Animal in the pet store.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Animal {

    // Identity fields
    private final AnimalName name;
    private final Description description;
    private final Location location;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();
    private final Set<FeedingSession> feedingSessions = new TreeSet<>((a, b) ->
            a.getFeedingTime().compareTo(b.getFeedingTime()));

    /**
     * Every field must be present and not null.
     */
    public Animal(AnimalName name, Description description, Location location, Set<Tag> tags,
                  Set<FeedingSession> feedingSessions) {
        requireAllNonNull(name, description, location);
        this.name = name;
        this.description = description;
        this.location = location;
        this.tags.addAll(tags);
        this.feedingSessions.addAll(feedingSessions);
    }

    public AnimalName getName() {
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
     * Returns an immutable feeding sessions set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<FeedingSession> getFeedingSessions() {
        return Collections.unmodifiableSet(feedingSessions);
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
        return name.equals(otherAnimal.name)
                && description.equals(otherAnimal.description)
                && location.equals(otherAnimal.location)
                && tags.equals(otherAnimal.tags)
                && feedingSessions.equals(otherAnimal.feedingSessions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, location, tags, feedingSessions);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("description", description)
                .add("location", location)
                .add("tags", tags)
                .add("feeding sessions", feedingSessions)
                .toString();
    }
}
