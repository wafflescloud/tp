package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Contact;
import seedu.address.model.Model;
import seedu.address.model.Name;

/**
 * Abstract base class for editing contacts (Person or Animal) in the address book.
 * Contains common functionality shared by EditPersonCommand and EditAnimalCommand.
 */
public abstract class EditContactCommand<T extends Contact, D> extends EditCommand {

    protected final Name name;
    protected final D editDescriptor;

    /**
     * @param name name of the contact to edit
     * @param editDescriptor details to edit the contact with
     */
    public EditContactCommand(Name name, D editDescriptor) {
        requireNonNull(name);
        requireNonNull(editDescriptor);

        this.name = name;
        this.editDescriptor = editDescriptor;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<T> list = getFilteredList(model);
        T contactToEdit = list.stream()
                .filter(contact -> contact.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CommandException(getInvalidNameMessage()));

        T editedContact = createEditedContact(contactToEdit, editDescriptor, model);

        if (!isSameContact(contactToEdit, editedContact) && hasContact(model, editedContact)) {
            throw new CommandException(getDuplicateMessage());
        }

        setContact(model, contactToEdit, editedContact);
        updateFilteredList(model);
        return new CommandResult(String.format(getSuccessMessage(), formatContact(editedContact)));
    }

    /**
     * Gets the filtered list of contacts from the model.
     * Must be implemented by subclasses.
     */
    protected abstract List<T> getFilteredList(Model model);

    /**
     * Creates an edited contact with the given descriptor.
     * Must be implemented by subclasses.
     */
    protected abstract T createEditedContact(T contactToEdit, D editDescriptor, Model model) throws CommandException;

    /**
     * Checks if two contacts are the same (identity check).
     * Must be implemented by subclasses.
     */
    protected abstract boolean isSameContact(T contact1, T contact2);

    /**
     * Checks if the model already has the given contact.
     * Must be implemented by subclasses.
     */
    protected abstract boolean hasContact(Model model, T contact);

    /**
     * Sets/replaces a contact in the model.
     * Must be implemented by subclasses.
     */
    protected abstract void setContact(Model model, T oldContact, T newContact);

    /**
     * Updates the filtered list in the model.
     * Must be implemented by subclasses.
     */
    protected abstract void updateFilteredList(Model model);

    /**
     * Gets the success message template.
     * Must be implemented by subclasses.
     */
    protected abstract String getSuccessMessage();

    /**
     * Gets the invalid name error message.
     * Must be implemented by subclasses.
     */
    protected abstract String getInvalidNameMessage();

    /**
     * Gets the duplicate contact error message.
     * Must be implemented by subclasses.
     */
    protected abstract String getDuplicateMessage();

    /**
     * Formats the contact for display in success message.
     * Must be implemented by subclasses.
     */
    protected abstract String formatContact(T contact);

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other == null || !other.getClass().equals(this.getClass())) {
            return false;
        }

        @SuppressWarnings("unchecked")
        EditContactCommand<T, D> otherCommand = (EditContactCommand<T, D>) other;
        return name.equals(otherCommand.name)
                && editDescriptor.equals(otherCommand.editDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("editDescriptor", editDescriptor)
                .toString();
    }
}
