package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Animal;
import seedu.address.model.person.Person;

/**
 * Deletes a person/animal identified using contact's name from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person/animal identified by the name used in the displayed person list.\n"
            + "Parameters: NAME (must be a string)\n"
            + "Example: " + COMMAND_WORD + " ";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private final Name name;
    private final String type;

    public DeleteCommand(Name name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (type.equals(Person.PERSON_TYPE)) {
            return deleteEntity(model.getFilteredPersonList(),
                    model::deletePerson, Messages::format, MESSAGE_DELETE_PERSON_SUCCESS );
        }

        if (type.equals(Animal.ANIMAL_TYPE)){
            return deleteEntity(model.getFilteredAnimalList(),
                                model::deleteAnimal,
                                Messages::format,
                                MESSAGE_DELETE_PERSON_SUCCESS );
        }


    }

    /**
     * Helper method to delete entity from list and return CommandResult.
     *
     * @param list The list to delete from.
     * @param deleteFunc The method to delete an entity.
     * @param successMessage The success message format.
     * @param <T> The type of entity (Person, Animal, etc.)
     * @return CommandResult after deletion.
     * @throws CommandException if name is invalid.
     */
    private <T> CommandResult deleteEntity(List<T> list, Consumer<T> deleteFunc,
                                           Function<T, String> formatter, String successMessage)
            throws CommandException {
        if (name.getZeroBased() >= list.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_NAME);  // Or customize message per type
        }

        T entityToDelete = list.get(name.getZeroBased());
        deleteFunc.accept(entityToDelete);
        return new CommandResult(String.format(successMessage, formatter.apply(entityToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return name.equals(otherDeleteCommand.name);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .toString();
    }
}
