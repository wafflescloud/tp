package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ANIMALS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.AnimalName;
import seedu.address.model.animal.Description;
import seedu.address.model.animal.Location;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing animal in the address book.
 */
public class EditAnimalCommand extends EditContactCommand<Animal, EditAnimalCommand.EditAnimalDescriptor> {

    public static final String MESSAGE_EDIT_ANIMAL_SUCCESS = "Edited Animal: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_ANIMAL = "This animal already exists in the address book";

    /**
     * @param name of the animal in the filtered animal list to edit
     * @param editAnimalDescriptor details to edit the animal with
     */
    public EditAnimalCommand(AnimalName name, EditAnimalDescriptor editAnimalDescriptor) {
        super(name, editAnimalDescriptor);
    }

    @Override
    protected List<Animal> getFilteredList(Model model) {
        return model.getFilteredAnimalList();
    }

    @Override
    protected Animal createEditedContact(Animal animalToEdit, EditAnimalDescriptor editDescriptor, Model model)
            throws CommandException {
        return createEditedAnimal(animalToEdit, editDescriptor);
    }

    @Override
    protected boolean isSameContact(Animal contact1, Animal contact2) {
        return contact1.isSameAnimal(contact2);
    }

    @Override
    protected boolean hasContact(Model model, Animal contact) {
        return model.hasAnimal(contact);
    }

    @Override
    protected void setContact(Model model, Animal oldContact, Animal newContact) {
        model.setAnimal(oldContact, newContact);
    }

    @Override
    protected void updateFilteredList(Model model) {
        model.updateFilteredAnimalList(PREDICATE_SHOW_ALL_ANIMALS);
    }

    @Override
    protected String getSuccessMessage() {
        return MESSAGE_EDIT_ANIMAL_SUCCESS;
    }

    @Override
    protected String getInvalidNameMessage() {
        return Messages.MESSAGE_INVALID_ANIMAL_DISPLAYED_NAME;
    }

    @Override
    protected String getDuplicateMessage() {
        return MESSAGE_DUPLICATE_ANIMAL;
    }

    @Override
    protected String formatContact(Animal contact) {
        return Messages.format(contact);
    }

    /**
     * Creates and returns a {@code Animal} with the details of {@code animalToEdit}
     * edited with {@code editAnimalDescriptor}.
     */
    private static Animal createEditedAnimal(Animal animalToEdit, EditAnimalDescriptor editAnimalDescriptor) {
        assert animalToEdit != null;

        AnimalName updatedName = editAnimalDescriptor.getName().orElse(animalToEdit.getAnimalName());
        Description updatedDescription = editAnimalDescriptor.getDescription().orElse(animalToEdit.getDescription());
        Location updatedLocation = editAnimalDescriptor.getLocation().orElse(animalToEdit.getLocation());
        Set<Tag> updatedTags = editAnimalDescriptor.getTags().orElse(animalToEdit.getTags());

        return new Animal(updatedName, updatedDescription, updatedLocation, updatedTags);
    }

    /**
     * Stores the details to edit the animal with. Each non-empty field value will replace the
     * corresponding field value of the animal.
     */
    public static class EditAnimalDescriptor {
        private AnimalName name;
        private Description description;
        private Location location;
        private Set<Tag> tags;

        public EditAnimalDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditAnimalDescriptor(EditAnimalDescriptor toCopy) {
            setName(toCopy.name);
            setDescription(toCopy.description);
            setLocation(toCopy.location);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, description, location, tags);
        }

        public void setName(AnimalName name) {
            this.name = name;
        }

        public Optional<AnimalName> getName() {
            return Optional.ofNullable(name);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public Optional<Location> getLocation() {
            return Optional.ofNullable(location);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditAnimalDescriptor)) {
                return false;
            }

            EditAnimalDescriptor otherEditAnimalDescriptor = (EditAnimalDescriptor) other;
            return getName().equals(otherEditAnimalDescriptor.getName())
                    && getDescription().equals(otherEditAnimalDescriptor.getDescription())
                    && getLocation().equals(otherEditAnimalDescriptor.getLocation())
                    && getTags().equals(otherEditAnimalDescriptor.getTags());
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(getClass().getCanonicalName())
                    .append("{name=").append(getName().orElse(null))
                    .append(", description=").append(getDescription().orElse(null))
                    .append(", location=").append(getLocation().orElse(null))
                    .append(", tags=").append(getTags().orElse(null))
                    .append("}");
            return sb.toString();
        }
    }
}
