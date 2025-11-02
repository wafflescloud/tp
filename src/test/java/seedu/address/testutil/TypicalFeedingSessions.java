package seedu.address.testutil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.animal.Animal;
import seedu.address.model.person.Person;

public class TypicalFeedingSessions {

    // FS1: Max fed by Alice on 2024-01-01 08:00
    public static final FeedingSession FS1 = new FeedingSession(
            TypicalAnimals.MAX.getId(),
            TypicalPersons.ALICE.getId(),
            LocalDateTime.of(2024, 1, 1, 8, 0));

    // FS2: Luna fed by Benson on 2024-02-15 12:00
    public static final FeedingSession FS2 = new FeedingSession(
            TypicalAnimals.LUNA.getId(),
            TypicalPersons.BENSON.getId(),
            LocalDateTime.of(2024, 2, 15, 12, 0));

    // FS3: Whiskers fed by Carl on 2024-03-20 18:30
    public static final FeedingSession FS3 = new FeedingSession(
            TypicalAnimals.WHISKERS.getId(),
            TypicalPersons.CARL.getId(),
            LocalDateTime.of(2024, 3, 20, 18, 30));

    // FS4: Oreo fed by Daniel on 2024-04-10 09:15
    public static final FeedingSession FS4 = new FeedingSession(
            TypicalAnimals.OREO.getId(),
            TypicalPersons.DANIEL.getId(),
            LocalDateTime.of(2024, 4, 10, 9, 15));

    // FS5: Bella fed by Fiona on 2024-05-05 14:45
    public static final FeedingSession FS5 = new FeedingSession(
            TypicalAnimals.BELLA.getId(),
            TypicalPersons.FIONA.getId(),
            LocalDateTime.of(2024, 5, 5, 14, 45));

    private TypicalFeedingSessions() {}

    /**
     * Returns an {@code AddressBook} populated with typical persons, animals, and feeding sessions.
     * This mirrors the style of TypicalPersons and TypicalAnimals helpers.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();

        // Add typical persons
        for (Person p : TypicalPersons.getTypicalPersons()) {
            ab.addPerson(p);
        }

        // Add typical animals
        for (Animal a : TypicalAnimals.getTypicalAnimals()) {
            ab.addAnimal(a);
        }

        // Add typical feeding sessions (note: Person/Animal session ID sets are not pre-linked here)
        for (FeedingSession fs : getTypicalFeedingSessions()) {
            ab.addFeedingSession(fs);
        }

        return ab;
    }

    /**
     * Returns a list of typical feeding sessions.
     */
    public static List<FeedingSession> getTypicalFeedingSessions() {
        return new ArrayList<>(Arrays.asList(FS1, FS2, FS3, FS4, FS5));
    }
}
