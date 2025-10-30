package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.Name;
import seedu.address.model.person.Email;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditPersonCommand extends EditContactCommand<Person, EditPersonCommand.EditPersonDescriptor> {

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    /**
     * Creates an EditPersonCommand to edit the person with the specified name.
     *
     * @param name name of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditPersonCommand(PersonName name, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(name);
        requireNonNull(editPersonDescriptor);

    @Override
    protected List<Person> getFilteredList(Model model) {
        return model.getFilteredPersonList();
    }

    @Override
    protected Person createEditedContact(Person personToEdit, EditPersonDescriptor editDescriptor, Model model)
            throws CommandException {
        return createEditedPerson(personToEdit, editDescriptor);
    }

    @Override
    protected boolean isSameContact(Person contact1, Person contact2) {
        return contact1.isSamePerson(contact2);
    }

    @Override
    protected boolean hasContact(Model model, Person contact) {
        return model.hasPerson(contact);
    }

    @Override
    protected void setContact(Model model, Person oldContact, Person newContact) {
        model.setPerson(oldContact, newContact);
    }

    @Override
    protected void updateFilteredList(Model model) {
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    protected String getSuccessMessage() {
        return MESSAGE_EDIT_PERSON_SUCCESS;
    }

    @Override
    protected String getInvalidNameMessage() {
        return Messages.MESSAGE_INVALID_PERSON_DISPLAYED_NAME;
    }

    @Override
    protected String getDuplicateMessage() {
        return MESSAGE_DUPLICATE_PERSON;
    }

    @Override
    protected String formatContact(Person contact) {
        return Messages.format(contact);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        PersonName updatedName = editPersonDescriptor.getName().orElse(personToEdit.getPersonName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(personToEdit.getId(), updatedName, updatedPhone, updatedEmail,
                updatedTags, personToEdit.getFeedingSessionIds());
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Set<Tag> tags;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
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

            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor e = (EditPersonDescriptor) other;
            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getTags().equals(e.getTags());
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(getClass().getCanonicalName())
                    .append("{name=").append(getName().orElse(null))
                    .append(", phone=").append(getPhone().orElse(null))
                    .append(", email=").append(getEmail().orElse(null))
                    .append(", tags=").append(getTags().orElse(null))
                    .append("}");
            return sb.toString();
        }
    }
}
