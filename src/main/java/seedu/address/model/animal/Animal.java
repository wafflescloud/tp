package seedu.address.model.animal;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents an Animal in the pet store.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Animal {

    // Identify contact type of Animal
    private static final String ANIMAL_TYPE = "animal";

    // Identity fields
    private final AnimalName name;

    // Data fields
    private final Description description;
    private final Location location;

    /**
     * Every field must be present and not null.
     */
    public Animal(AnimalName name, Description description, Location location) {
        requireAllNonNull(name, description, location);
        this.name = name;
        this.description = description;
        this.location = location;
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
                && location.equals(otherAnimal.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, location);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("description", description)
                .add("location", location)
                .toString();
    }
}
