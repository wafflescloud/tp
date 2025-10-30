package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Function;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Contact;
import seedu.address.model.Model;
import seedu.address.model.Name;
import seedu.address.model.animal.Animal;
import seedu.address.model.person.Person;

/**
 * Generic command for deleting contacts (Person or Animal) from the address book.
 * Uses method references to handle type-specific behavior without requiring subclasses.
 */
public class DeleteContactCommand<T extends Contact> extends DeleteCommand {

    private final Name name;
    private final Function<Model, List<T>> listGetter;
    private final Function<Model, Void> deleteAction;
    private final String successMessageFormat;
    private final String notFoundMessage;

    /**
     * Creates a DeleteContactCommand with the specified name and model operations.
     */
    public DeleteContactCommand(Name name,
                               Function<Model, List<T>> listGetter,
                               Function<Model, Void> deleteAction,
                               String successMessageFormat,
                               String notFoundMessage) {
        requireNonNull(name);
        this.name = name;
        this.listGetter = listGetter;
        this.deleteAction = deleteAction;
        this.successMessageFormat = successMessageFormat;
        this.notFoundMessage = notFoundMessage;
    }

    /**
     * Factory method to create a DeleteContactCommand for Person entities.
     */
    public static DeleteContactCommand<Person> forPerson(Name name) {
        return new DeleteContactCommand<>(
            name,
            Model::getFilteredPersonList,
            null,
            Messages.MESSAGE_DELETED_PERSON_SUCCESS,
            Messages.MESSAGE_INVALID_PERSON_DISPLAYED_NAME
        );
    }

    /**
     * Factory method to create a DeleteContactCommand for Animal entities.
     */
    public static DeleteContactCommand<Animal> forAnimal(Name name) {
        return new DeleteContactCommand<>(
            name,
            Model::getFilteredAnimalList,
            null,
            Messages.MESSAGE_DELETED_ANIMAL_SUCCESS,
            Messages.MESSAGE_INVALID_ANIMAL_DISPLAYED_NAME
        );
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<T> list = listGetter.apply(model);

        T contactToDelete = list.stream()
                .filter(contact -> contact.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CommandException(notFoundMessage));

        String formattedContact;

        if (contactToDelete instanceof Person) {
            model.deletePerson((Person) contactToDelete);
            formattedContact = Messages.format((Person) contactToDelete);
        } else if (contactToDelete instanceof Animal) {
            model.deleteAnimal((Animal) contactToDelete);
            formattedContact = Messages.format((Animal) contactToDelete);
        } else {
            throw new IllegalStateException("Unknown contact type: " + contactToDelete.getClass());
        }

        return new CommandResult(String.format(successMessageFormat, formattedContact));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other == null || !other.getClass().equals(this.getClass())) {
            return false;
        }

        @SuppressWarnings("unchecked")
        DeleteContactCommand<T> otherCommand = (DeleteContactCommand<T>) other;
        return name.equals(otherCommand.name)
                && listGetter.equals(otherCommand.listGetter)
                && successMessageFormat.equals(otherCommand.successMessageFormat)
                && notFoundMessage.equals(otherCommand.notFoundMessage);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .toString();
    }
}
