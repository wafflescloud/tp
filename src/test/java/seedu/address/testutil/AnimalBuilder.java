package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.animal.Animal;
import seedu.address.model.animal.AnimalName;
import seedu.address.model.animal.Description;
import seedu.address.model.animal.Location;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Animal objects.
 */
public class AnimalBuilder {

    public static final String DEFAULT_NAME = "Whiskers";
    public static final String DEFAULT_DESCRIPTION = "A playful tabby cat.";
    public static final String DEFAULT_LOCATION = "Shelter Room 2";

    private AnimalName name;
    private Description description;
    private Location location;
    private Set<Tag> tags;
    private Set<FeedingSession> feedingSessions;

    /**
     * Creates an {@code AnimalBuilder} with default details.
     */
    public AnimalBuilder() {
        name = new AnimalName(DEFAULT_NAME);
        description = new Description(DEFAULT_DESCRIPTION);
        location = new Location(DEFAULT_LOCATION);
        tags = new HashSet<>();
        feedingSessions = new HashSet<>();
    }

    /**
     * Initializes the {@code AnimalBuilder} with the data of {@code animalToCopy}.
     *
     * @param animalToCopy The animal object whose data will be copied into the builder.
     */
    public AnimalBuilder(Animal animalToCopy) {
        name = animalToCopy.getName();
        description = animalToCopy.getDescription();
        location = animalToCopy.getLocation();
        tags = new HashSet<>(animalToCopy.getTags());
        feedingSessions = new HashSet<>(animalToCopy.getFeedingSessions());
    }

    /**
     * Sets the {@code AnimalName} of the {@code Animal} that we are building.
     *
     * @param name The name to set.
     * @return This {@code AnimalBuilder} instance for chaining.
     */
    public AnimalBuilder withName(String name) {
        this.name = new AnimalName(name);
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

    public Animal build() {
        return new Animal(name, description, location, tags, feedingSessions);
    }
}
