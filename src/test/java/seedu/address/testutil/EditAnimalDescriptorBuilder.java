package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditAnimalCommand.EditAnimalDescriptor;
import seedu.address.model.Name;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.Description;
import seedu.address.model.animal.Location;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building EditAnimalDescriptor objects.
 */
public class EditAnimalDescriptorBuilder {

    private EditAnimalDescriptor descriptor;

    public EditAnimalDescriptorBuilder() {
        descriptor = new EditAnimalDescriptor();
    }

    public EditAnimalDescriptorBuilder(EditAnimalDescriptor descriptor) {
        this.descriptor = new EditAnimalDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditAnimalDescriptor} with fields containing {@code animal}'s details
     */
    public EditAnimalDescriptorBuilder(Animal animal) {
        descriptor = new EditAnimalDescriptor();
        descriptor.setName(animal.getName());
        descriptor.setDescription(animal.getDescription());
        descriptor.setLocation(animal.getLocation());
        descriptor.setTags(animal.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditAnimalDescriptor} that we are building.
     */
    public EditAnimalDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code EditAnimalDescriptor} that we are building.
     */
    public EditAnimalDescriptorBuilder withDescription(String description) {
        descriptor.setDescription(new Description(description));
        return this;
    }

    /**
     * Sets the {@code Location} of the {@code EditAnimalDescriptor} that we are building.
     */
    public EditAnimalDescriptorBuilder withLocation(String location) {
        descriptor.setLocation(new Location(location));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditAnimalDescriptor}
     * that we are building.
     */
    public EditAnimalDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditAnimalDescriptor build() {
        return descriptor;
    }
}
