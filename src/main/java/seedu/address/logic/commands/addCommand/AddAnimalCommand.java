package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.animal.Animal;

/**
 * Adds an animal to the address book.
 */
public class AddAnimalCommand extends AddCommand {

    public static final String MESSAGE_SUCCESS = "New animal added: %1$s";
    public static final String MESSAGE_DUPLICATE_ANIMAL = "This animal already exists in the address book";

    private final Animal toAdd;

    /**
     * Creates an AddAnimalCommand to add the specified {@code Animal}
     */
    public AddAnimalCommand(Animal animal) {
        requireNonNull(animal);
        toAdd = animal;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasAnimal(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_ANIMAL);
        }

        model.addAnimal(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddAnimalCommand)) {
            return false;
        }

        AddAnimalCommand otherCommand = (AddAnimalCommand) other;
        return toAdd.equals(otherCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
