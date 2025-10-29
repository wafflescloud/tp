package seedu.address.model.animal;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Contact;
import seedu.address.model.tag.Tag;

/**
 * Represents an Animal in the pet store.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Animal extends Contact {

    // Identity fields specific to Animal (name is now in parent class)
    private final Description description;
    private final Location location;

    /**
     * Every field must be present and not null.
     */
    public Animal(AnimalName name, Description description, Location location, Set<Tag> tags) {
        super(name, tags);
        requireAllNonNull(description, location);
        this.description = description;
        this.location = location;
    }

    /**
     * Returns the specific AnimalName for type-specific operations.
     * This method provides access to AnimalName-specific methods.
     */
    public AnimalName getAnimalName() {
        return (AnimalName) name;  // Safe cast since we passed AnimalName to constructor
    }

    public Description getDescription() {
        return description;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * Returns true if both animals have the same name.
     * This defines a weaker notion of equality between two animals.
     */
    public boolean isSameAnimal(Animal otherAnimal) {
        return isSameContact(otherAnimal);
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
                && tags.equals(otherAnimal.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, location, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("description", description)
                .add("location", location)
                .add("tags", tags)
                .toString();
    }
}
