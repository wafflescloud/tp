package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ANIMALS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.Name;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.Description;
import seedu.address.model.animal.Location;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing animal in the address book.
 */
public class EditAnimalCommand extends EditCommand {

    public static final String MESSAGE_EDIT_ANIMAL_SUCCESS = "Edited Animal: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_ANIMAL = "This animal already exists in the address book.";

    private final Name name;
    private final EditAnimalDescriptor editAnimalDescriptor;

    /**
     * Creates an EditAnimalCommand to edit the animal with the specified name.
     *
     * @param name name of the animal in the filtered animal list to edit
     * @param editAnimalDescriptor details to edit the animal with
     */
    public EditAnimalCommand(Name name, EditAnimalDescriptor editAnimalDescriptor) {
        requireNonNull(name);
        requireNonNull(editAnimalDescriptor);

        this.name = name;
        this.editAnimalDescriptor = editAnimalDescriptor;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Animal> list = model.getFilteredAnimalList();
        Animal animalToEdit = list.stream()
                .filter(a -> a.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CommandException(Messages.MESSAGE_INVALID_ANIMAL_DISPLAYED_NAME));

        Animal editedAnimal = createEditedAnimal(animalToEdit, editAnimalDescriptor);

        if (!animalToEdit.isSameAnimal(editedAnimal) && model.hasAnimal(editedAnimal)) {
            throw new CommandException(MESSAGE_DUPLICATE_ANIMAL);
        }

        model.setAnimal(animalToEdit, editedAnimal);
        model.updateFilteredAnimalList(PREDICATE_SHOW_ALL_ANIMALS);
        return new CommandResult(String.format(MESSAGE_EDIT_ANIMAL_SUCCESS, Messages.format(editedAnimal)));
    }

    /**
     * Creates and returns a {@code Animal} with the details of {@code animalToEdit}
     * edited with {@code editAnimalDescriptor}.
     */
    private static Animal createEditedAnimal(Animal animalToEdit, EditAnimalDescriptor editAnimalDescriptor) {
        assert animalToEdit != null;

        Name updatedName = editAnimalDescriptor.getName().orElse(animalToEdit.getName());
        Description updatedDescription = editAnimalDescriptor.getDescription().orElse(animalToEdit.getDescription());
        Location updatedLocation = editAnimalDescriptor.getLocation().orElse(animalToEdit.getLocation());
        Set<Tag> updatedTags = editAnimalDescriptor.getTags().orElse(animalToEdit.getTags());

        return new Animal(animalToEdit.getId(), updatedName, updatedDescription, updatedLocation, updatedTags,
                animalToEdit.getFeedingSessionIds());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditAnimalCommand)) {
            return false;
        }

        EditAnimalCommand otherEditCommand = (EditAnimalCommand) other;
        return name.equals(otherEditCommand.name)
                && editAnimalDescriptor.equals(otherEditCommand.editAnimalDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("editAnimalDescriptor", editAnimalDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the animal with. Each non-empty field value will replace the
     * corresponding field value of the animal.
     */
    public static class EditAnimalDescriptor {
        private Name name;
        private Description description;
        private Location location;
        private Set<Tag> tags;

        public EditAnimalDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
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

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
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
