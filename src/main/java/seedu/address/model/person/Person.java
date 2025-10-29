package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Contact;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person extends Contact {

    // Identity fields specific to Person (name is now in parent class)
    private final Phone phone;
    private final Email email;

    // Data fields specific to Person
    private final Set<FeedingSession> feedingSessions = new TreeSet<>((a, b) ->
            a.getFeedingTime().compareTo(b.getFeedingTime()));

    /**
     * Every field must be present and not null.
     */
    public Person(PersonName name, Phone phone, Email email, Set<Tag> tags,
            Set<FeedingSession> feedingSession) {
        super(name, tags);
        requireAllNonNull(phone, email);
        this.phone = phone;
        this.email = email;
        this.feedingSessions.addAll(feedingSession);
    }

    /**
     * Returns the specific PersonName for type-specific operations.
     * This method provides access to PersonName-specific methods.
     */
    public PersonName getPersonName() {
        // Safe cast since we passed PersonName to constructor
        return (PersonName) name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    /**
     * Returns an immutable feeding sessions set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<FeedingSession> getFeedingSessions() {
        return Collections.unmodifiableSet(feedingSessions);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        return isSameContact(otherPerson);
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && tags.equals(otherPerson.tags)
                && feedingSessions.equals(otherPerson.feedingSessions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, tags, feedingSessions);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("tags", tags)
                .add("feeding sessions", feedingSessions)
                .toString();
    }
}
