package seedu.address.testutil;

import seedu.address.logic.commands.EditAnimalCommand.EditAnimalDescriptor;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.AnimalName;
import seedu.address.model.animal.Description;
import seedu.address.model.animal.Location;

/**
 * A utility class to help with building EditPersonDescriptor objects.
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
    }

    /**
     * Sets the {@code AnimalName} of the {@code EditAnimalDescriptor} that we are building.
     */
    public EditAnimalDescriptorBuilder withName(String name) {
        descriptor.setName(new AnimalName(name));
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


    public EditAnimalDescriptor build() {
        return descriptor;
    }
}
