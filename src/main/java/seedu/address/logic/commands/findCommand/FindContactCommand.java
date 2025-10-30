package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Contact;
import seedu.address.model.Model;

/**
 * Abstract base class for finding contacts (Person or Animal) in the address book.
 * Contains common functionality shared by FindPersonCommand and FindAnimalCommand.
 */
public abstract class FindContactCommand<T extends Contact> extends FindCommand {

    protected final Predicate<T> predicate;

    public FindContactCommand(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        updateFilteredList(model);
        return new CommandResult(getSuccessMessage(getFilteredListSize(model)));
    }

    /**
     * Updates the filtered list in the model based on the predicate.
     * Must be implemented by subclasses.
     */
    protected abstract void updateFilteredList(Model model);

    /**
     * Gets the size of the filtered list after applying the predicate.
     * Must be implemented by subclasses.
     */
    protected abstract int getFilteredListSize(Model model);

    /**
     * Gets the success message with the number of found contacts.
     * Must be implemented by subclasses.
     */
    protected abstract String getSuccessMessage(int count);

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // Check if other is the same class
        if (other == null || !other.getClass().equals(this.getClass())) {
            return false;
        }

        @SuppressWarnings("unchecked")
        FindContactCommand<T> otherCommand = (FindContactCommand<T>) other;
        return predicate.equals(otherCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
