package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.AnimalName;

/**
 * Deletes a animal identified using contact's name from the address book.
 */
public class DeleteAnimalCommand extends DeleteCommand {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the animal identified by the name used in the displayed person list.\n"
            + "Parameters: NAME (must be a string)\n"
            + "Example: " + COMMAND_WORD + " ";

    public static final String MESSAGE_DELETED_ANIMAL_SUCCESS = "Deleted Animal: %1$s";

    private final AnimalName name;

    /**
     * Constructs a {@code DeletePersonCommand} to delete an entity with the given {@code Name} and {@code type}.
     *
     * @param name The name of the entity (person or animal) to delete.
     */
    public DeleteAnimalCommand(AnimalName name) {
        this.name = name;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Animal> list = model.getFilteredAnimalList();
        Animal animalToDelete = list.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_NAME));

        model.deleteAnimal(animalToDelete);
        return new CommandResult(String.format(MESSAGE_DELETED_ANIMAL_SUCCESS, Messages.format(animalToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteAnimalCommand)) {
            return false;
        }

        DeleteAnimalCommand otherDeleteAnimalCommand = (DeleteAnimalCommand) other;
        return name.equals(otherDeleteAnimalCommand.name);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .toString();
    }
}
