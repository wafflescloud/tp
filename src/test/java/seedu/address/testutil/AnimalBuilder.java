package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import seedu.address.model.Name;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.Description;
import seedu.address.model.animal.Location;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Animal objects.
 */
public class AnimalBuilder {

    public static final String DEFAULT_NAME = "Whiskers";
    public static final String DEFAULT_DESCRIPTION = "A playful tabby cat.";
    public static final String DEFAULT_LOCATION = "Shelter Room 2";

    private UUID id;
    private Name name;
    private Description description;
    private Location location;
    private Set<Tag> tags;
    private Set<UUID> feedingSessionIds;

    /**
     * Creates an {@code AnimalBuilder} with default details.
     */
    public AnimalBuilder() {
        id = null; // Will auto-generate when build() is called
        name = new Name(DEFAULT_NAME);
        description = new Description(DEFAULT_DESCRIPTION);
        location = new Location(DEFAULT_LOCATION);
        tags = new HashSet<>();
        feedingSessionIds = new HashSet<>();
    }

    /**
     * Initializes the {@code AnimalBuilder} with the data of {@code animalToCopy}.
     *
     * @param animalToCopy The animal object whose data will be copied into the builder.
     */
    public AnimalBuilder(Animal animalToCopy) {
        id = animalToCopy.getId();
        name = animalToCopy.getName();
        description = animalToCopy.getDescription();
        location = animalToCopy.getLocation();
        tags = new HashSet<>(animalToCopy.getTags());
        feedingSessionIds = new HashSet<>(animalToCopy.getFeedingSessionIds());
    }

    /**
     * Sets the {@code Name} of the {@code Animal} that we are building.
     *
     * @param name The name to set.
     * @return This {@code AnimalBuilder} instance for chaining.
     */
    public AnimalBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code Animal} that we are building.
     *
     * @param description The description to set.
     * @return This {@code AnimalBuilder} instance for chaining.
     */
    public AnimalBuilder withDescription(String description) {
        this.description = new Description(description);
        return this;
    }

    /**
     * Sets the {@code Location} of the {@code Animal} that we are building.
     *
     * @param location The location to set.
     * @return This {@code AnimalBuilder} instance for chaining.
     */
    public AnimalBuilder withLocation(String location) {
        this.location = new Location(location);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Animal} that we are building.
     */
    public AnimalBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Set<UUID>} of the {@code Animal} that we are building.
     */
    public AnimalBuilder withFeedingSessionIds(Set<UUID> feedingSessionIds) {
        this.feedingSessionIds = new HashSet<>(feedingSessionIds);
        return this;
    }

    /**
     * Builds and returns an {@link Animal} using the values configured in this builder.
     * <p>
     * If this builder was initialized from an existing animal (or explicitly provided an ID),
     * the same identifier will be used. Otherwise, the constructed animal will have an
     * auto-generated identifier.
     * <p>
     * The returned animal will include the configured {@code name}, {@code description},
     * {@code location}, {@code tags}, and {@code feedingSessionIds}.
     *
     * @return a new {@link Animal} instance based on the current builder state
     */
    public Animal build() {
        if (id != null) {
            return new Animal(id, name, description, location, tags, feedingSessionIds);
        }
        return new Animal(name, description, location, tags, feedingSessionIds);
    }
}
