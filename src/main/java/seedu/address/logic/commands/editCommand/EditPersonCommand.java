package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

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
import seedu.address.model.animal.Animal;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.feedingsession.IntermediateFeedingSession;
import seedu.address.model.person.Email;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonName;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditPersonCommand extends EditCommand {

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";
    public static final String MESSAGE_INVALID_PERSON_NAME = "The person is not found in the address book";

    private final PersonName name;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param name name of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditPersonCommand(PersonName name, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(name);
        requireNonNull(editPersonDescriptor);

        this.name = name;
        this.editPersonDescriptor = editPersonDescriptor;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> list = model.getFilteredPersonList();
        Person personToEdit = list.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_NAME));

        // Convert intermediate feeding sessions to actual feeding sessions
        Set<FeedingSession> feedingSessions = new HashSet<>();
        if (editPersonDescriptor.getIntermediateFeedingSessions().isPresent()) {
            for (IntermediateFeedingSession ifs : editPersonDescriptor.getIntermediateFeedingSessions().get()) {
                // Find the animal in the model
                Animal animal = model.getFilteredAnimalList().stream()
                        .filter(a -> a.getName().fullName.equals(ifs.getAnimalName()))
                        .findFirst()
                        .orElseThrow(() -> new CommandException("Animal not found: " + ifs.getAnimalName()));

                feedingSessions.add(new FeedingSession(animal, ifs.getDateTime()));
            }
        }

        // Update the editPersonDescriptor with resolved feeding sessions
        EditPersonDescriptor resolvedDescriptor = new EditPersonDescriptor(editPersonDescriptor);
        Person editedPerson = createEditedPerson(personToEdit, resolvedDescriptor, feedingSessions);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor} and {@code feedingSessions}.
     */
    private static Person createEditedPerson(Person personToEdit,
            EditPersonDescriptor editPersonDescriptor,
            Set<FeedingSession> feedingSessions) {
        assert personToEdit != null;

        PersonName updatedName = editPersonDescriptor.getName().orElse(personToEdit.getPersonName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        // Merge existing feeding sessions with new ones if no new sessions provided
        Set<FeedingSession> updatedFeedingSessions = feedingSessions.isEmpty()
                ? personToEdit.getFeedingSessions()
                : feedingSessions;

        return new Person(updatedName, updatedPhone, updatedEmail,
                updatedTags, updatedFeedingSessions);
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditPersonCommand)) {
            return false;
        }

        EditPersonCommand otherEditCommand = (EditPersonCommand) other;
        return name.equals(otherEditCommand.name)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private PersonName name;
        private Phone phone;
        private Email email;
        private Set<Tag> tags;
        private Set<IntermediateFeedingSession> intermediateFeedingSessions;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} and {@code intermediateFeedingSessions} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setTags(toCopy.tags);
            setIntermediateFeedingSessions(toCopy.intermediateFeedingSessions);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, tags, intermediateFeedingSessions);
        }

        public void setName(PersonName name) {
            this.name = name;
        }

        public Optional<PersonName> getName() {
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

        /**
         * Sets {@code intermediateFeedingSessions} to this object's {@code intermediateFeedingSessions}.
         * A defensive copy of {@code intermediateFeedingSessions} is used internally.
         */
        public void setIntermediateFeedingSessions(Set<IntermediateFeedingSession> intermediateFeedingSessions) {
            this.intermediateFeedingSessions = (intermediateFeedingSessions != null)
                    ? new HashSet<>(intermediateFeedingSessions)
                    : null;
        }

        /**
         * Returns an unmodifiable intermediate feeding sessions set.
         */
        public Optional<Set<IntermediateFeedingSession>> getIntermediateFeedingSessions() {
            return (intermediateFeedingSessions != null)
                    ? Optional.of(Collections.unmodifiableSet(intermediateFeedingSessions))
                    : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor e = (EditPersonDescriptor) other;
            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getTags().equals(e.getTags())
                    && getIntermediateFeedingSessions().equals(e.getIntermediateFeedingSessions());
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(getClass().getCanonicalName())
                    .append("{name=").append(getName().orElse(null))
                    .append(", phone=").append(getPhone().orElse(null))
                    .append(", email=").append(getEmail().orElse(null))
                    .append(", tags=").append(getTags().orElse(null))
                    .append(", intermediateFeedingSessions=").append(getIntermediateFeedingSessions().orElse(null))
                    .append("}");
            return sb.toString();
        }
    }
}
