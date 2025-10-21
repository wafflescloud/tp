package seedu.address.model;

/**
 * Represents a snapshot of the address book state.
 */
public class State {
    private final ReadOnlyAddressBook addressBook;

    public State(ReadOnlyAddressBook addressBook) {
        this.addressBook = new AddressBook(addressBook);
    }

    public ReadOnlyAddressBook getAddressBook() {
        return new AddressBook(addressBook);
    }
}
