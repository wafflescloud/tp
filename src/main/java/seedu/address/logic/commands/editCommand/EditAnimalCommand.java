package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ANIMALS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.AnimalName;
import seedu.address.model.animal.Description;
import seedu.address.model.animal.Location;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditAnimalCommand extends EditCommand {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_EDIT_ANIMAL_SUCCESS = "Edited Animal: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_ANIMAL = "This animal already exists in the address book";
    public static final String MESSAGE_INVALID_ANIMAL_NAME = "The animal is not found in the address book";

    private final AnimalName name;
    private final EditAnimalDescriptor editAnimalDescriptor;

    /**
     * @param name of the person in the filtered animal list to edit
     * @param editAnimalDescriptor details to edit the animal with
     */
    public EditAnimalCommand(AnimalName name, EditAnimalDescriptor editAnimalDescriptor) {
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
                .filter(animal -> animal.getName().equals(name))
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

        AnimalName updatedName = editAnimalDescriptor.getName().orElse(animalToEdit.getName());
        Description updatedDescription = editAnimalDescriptor.getDescription().orElse(animalToEdit.getDescription());
        Location updatedLocation = editAnimalDescriptor.getLocation().orElse(animalToEdit.getLocation());

        return new Animal(updatedName, updatedDescription, updatedLocation);
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditAnimalCommand)) {
            return false;
        }

        EditAnimalCommand otherEditAnimalCommand = (EditAnimalCommand) other;
        return name.equals(otherEditAnimalCommand.name)
                && editAnimalDescriptor.equals(otherEditAnimalCommand.editAnimalDescriptor);
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
        private AnimalName name;
        private Description description;
        private Location location;

        public EditAnimalDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditAnimalDescriptor(EditAnimalDescriptor toCopy) {
            setName(toCopy.name);
            setDescription(toCopy.description);
            setLocation(toCopy.location);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, description, location);
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

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditAnimalDescriptor)) {
                return false;
            }

            EditAnimalDescriptor otherEditAnimalDescriptor = (EditAnimalDescriptor) other;
            return Objects.equals(name, otherEditAnimalDescriptor.name)
                    && Objects.equals(description, otherEditAnimalDescriptor.description)
                    && Objects.equals(location, otherEditAnimalDescriptor.location);
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
}


