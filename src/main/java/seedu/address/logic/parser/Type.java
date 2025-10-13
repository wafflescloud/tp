package seedu.address.logic.parser;

/**
 * A type that marks whether an entry is a person or animal in the address book.
 * E.g. 'person' or 'animal'.
 */
public class Type {
    private final String type;

    public Type(String type) {
        this.type = type.toLowerCase();
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return getType();
    }

    @Override
    public int hashCode() {
        return type == null ? 0 : type.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Type)) {
            return false;
        }

        Type otherType = (Type) other;
        return type.equals(otherType.type);
    }
}
