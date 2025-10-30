package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Contact;
import seedu.address.model.Name;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person extends Contact {

    // Identity fields specific to Person
    private final UUID id;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Set<UUID> feedingSessionIds = new HashSet<>();

    /**
     * Every field must be present and not null.
     * Creates a new Person with auto-generated ID.
     */
    public Person(Name name, Phone phone, Email email, Set<Tag> tags,
            Set<UUID> feedingSessionIds) {
        super(name, tags);
        requireAllNonNull(phone, email);
        this.id = UUID.randomUUID();
        this.phone = phone;
        this.email = email;
        this.feedingSessionIds.addAll(feedingSessionIds);
    }

    /**
     * Constructor with explicit ID (for deserialization).
     */
    public Person(UUID id, Name name, Phone phone, Email email, Set<Tag> tags,
            Set<UUID> feedingSessionIds) {
        super(name, tags);
        requireAllNonNull(id, phone, email);
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.feedingSessionIds.addAll(feedingSessionIds);
    }

    public UUID getId() {
        return id;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    /**
     * Returns an immutable feeding session IDs set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<UUID> getFeedingSessionIds() {
        return Collections.unmodifiableSet(feedingSessionIds);
    }

    /**
     * Returns a new Person with the given feeding session ID added.
     */
    public Person addFeedingSessionId(UUID sessionId) {
        Set<UUID> updatedSessions = new HashSet<>(feedingSessionIds);
        updatedSessions.add(sessionId);
        return new Person(id, getName(), phone, email, getTags(), updatedSessions);
    }

    /**
     * Returns a new Person with the given feeding session ID removed.
     */
    public Person removeFeedingSessionId(UUID sessionId) {
        Set<UUID> updatedSessions = new HashSet<>(feedingSessionIds);
        updatedSessions.remove(sessionId);
        return new Person(id, getName(), phone, email, getTags(), updatedSessions);
    }

    /**
     * Returns the earliest feeding session this person is involved in from the given list.
     * @param allSessions List of all feeding sessions to search from
     * @return The earliest feeding session, or null if none found
     */
    public FeedingSession getEarliestFeedingSession(List<FeedingSession> allSessions) {
        if (allSessions == null || allSessions.isEmpty()) {
            return null;
        }

        return allSessions.stream()
                .filter(session -> session.involvesPerson(this.id))
                .min(Comparator.comparing(FeedingSession::getDateTime))
                .orElse(null);
    }

    /**
     * Returns a formatted string of the earliest feeding session, or empty string if none.
     * @param allSessions List of all feeding sessions to search from
     * @return Formatted string showing next feeding time, or empty string
     */
    public String getEarliestFeedingSessionDisplay(List<FeedingSession> allSessions) {
        FeedingSession earliest = getEarliestFeedingSession(allSessions);
        if (earliest == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        return String.format("Next feeding: %s", earliest.getDateTime().format(formatter));
    }

    /**
     * Returns true if both persons have the same name (case-insensitive and whitespace-normalized).
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
        return id.equals(otherPerson.id)
                && getName().equals(otherPerson.getName())
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && getTags().equals(otherPerson.getTags())
                && feedingSessionIds.equals(otherPerson.feedingSessionIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName(), phone, email, getTags(), feedingSessionIds);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("id", id)
                .add("name", getName())
                .add("phone", phone)
                .add("email", email)
                .add("tags", getTags())
                .add("feeding session IDs", feedingSessionIds)
                .toString();
    }
}
