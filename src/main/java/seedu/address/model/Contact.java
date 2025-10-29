package seedu.address.model;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Abstract class for different contact types in the address book.
 * (Specifically Person and Animal only)
 * Contains attributes and methods shared by Person and Animal.
 */
public abstract class Contact {

    // Common data fields
    protected final Name name;
    protected final Set<Tag> tags = new HashSet<>();

    /**
     * Constructor for Contact.
     * @param name The name of this contact
     * @param tags Set of tags for this contact
     */
    public Contact(Name name, Set<Tag> tags) {
        requireAllNonNull(name, tags);
        this.name = name;
        this.tags.addAll(tags);
    }

    /**
     * Returns the name of this contact.
     */
    public Name getName() {
        return name;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both contacts have the same name.
     * This defines a weaker notion of equality between two contacts.
     */
    public boolean isSameContact(Contact otherContact) {
        if (otherContact == this) {
            return true;
        }

        return otherContact != null
                && otherContact.getClass().equals(this.getClass())
                && otherContact.getName().equals(getName());
    }

    /**
     * Abstract method for type-specific equality check.
     * Must be implemented by subclasses for their specific equality logic.
     */
    @Override
    public abstract boolean equals(Object other);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}
