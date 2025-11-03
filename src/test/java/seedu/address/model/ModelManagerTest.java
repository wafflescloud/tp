package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.testutil.AddressBookBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getPersonList().remove(0));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void getAnimalList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getAnimalList().remove(0));
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().toString().split("\\s+");
        modelManager.updateFilteredPersonList(new ContactContainsKeywordsPredicate<>(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }

    @Test
    public void setPerson_nameChanged_feedingSessionsUpdatedInAnimals() {
        modelManager = new ModelManager();

        seedu.address.model.person.Person matt = new seedu.address.testutil.PersonBuilder()
                .withName("Matt")
                .withPhone("82345678")
                .withEmail("matt@example.com")
                .build();

        seedu.address.model.animal.Animal max = new seedu.address.testutil.AnimalBuilder()
                .withName("Max")
                .withDescription("Dog")
                .withLocation("Shelter A")
                .build();

        modelManager.addPerson(matt);
        modelManager.addAnimal(max);

        java.time.LocalDateTime feedingTime = java.time.LocalDateTime.of(2024, 10, 15, 10, 0);
        seedu.address.model.feedingsession.FeedingSession session =
                new seedu.address.model.feedingsession.FeedingSession(max.getId(), matt.getId(), feedingTime);

        java.util.Set<java.util.UUID> sessionIds = new java.util.HashSet<>();
        sessionIds.add(session.getId());

        seedu.address.model.person.Person mattWithSession = new seedu.address.model.person.Person(
                matt.getId(), matt.getName(), matt.getPhone(), matt.getEmail(), matt.getTags(), sessionIds);
        seedu.address.model.animal.Animal maxWithSession = new seedu.address.model.animal.Animal(
                max.getId(), max.getName(), max.getDescription(), max.getLocation(), max.getTags(), sessionIds);

        modelManager.setPerson(matt, mattWithSession);
        modelManager.setAnimal(max, maxWithSession);

        seedu.address.model.person.Person matthew = new seedu.address.testutil.PersonBuilder()
                .withName("Matthew")
                .withPhone("82345678")
                .withEmail("matt@example.com")
                .withFeedingSessionIds(sessionIds)
                .build();

        modelManager.setPerson(mattWithSession, matthew);

        seedu.address.model.animal.Animal updatedAnimal = modelManager.getFilteredAnimalList().stream()
                .filter(a -> a.getName().toString().equals("Max"))
                .findFirst()
                .orElseThrow();

        boolean hasSession = updatedAnimal.getFeedingSessionIds().contains(session.getId());

        assertTrue(hasSession, "Animal should still have the feeding session ID");
    }

    @Test
    public void setAnimal_nameChanged_feedingSessionsUpdatedInPersons() {
        modelManager = new ModelManager();

        seedu.address.model.person.Person john = new seedu.address.testutil.PersonBuilder()
                .withName("John")
                .withPhone("87654321")
                .withEmail("john@example.com")
                .build();

        seedu.address.model.animal.Animal buddy = new seedu.address.testutil.AnimalBuilder()
                .withName("Buddy")
                .withDescription("Cat")
                .withLocation("Shelter B")
                .build();

        modelManager.addPerson(john);
        modelManager.addAnimal(buddy);

        java.time.LocalDateTime feedingTime = java.time.LocalDateTime.of(2024, 10, 16, 14, 30);
        seedu.address.model.feedingsession.FeedingSession session =
                new seedu.address.model.feedingsession.FeedingSession(buddy.getId(), john.getId(), feedingTime);

        java.util.Set<java.util.UUID> sessionIds = new java.util.HashSet<>();
        sessionIds.add(session.getId());

        seedu.address.model.person.Person johnWithSession = new seedu.address.model.person.Person(
                john.getId(), john.getName(), john.getPhone(), john.getEmail(), john.getTags(), sessionIds);
        seedu.address.model.animal.Animal buddyWithSession = new seedu.address.model.animal.Animal(
                buddy.getId(), buddy.getName(), buddy.getDescription(),
                buddy.getLocation(), buddy.getTags(), sessionIds);

        modelManager.setPerson(john, johnWithSession);
        modelManager.setAnimal(buddy, buddyWithSession);

        seedu.address.model.animal.Animal fluffy = new seedu.address.testutil.AnimalBuilder()
                .withName("Fluffy")
                .withDescription("Cat")
                .withLocation("Shelter B")
                .build();
        fluffy = new seedu.address.model.animal.Animal(
                buddyWithSession.getId(), fluffy.getName(), fluffy.getDescription(),
                fluffy.getLocation(), fluffy.getTags(), sessionIds);

        modelManager.setAnimal(buddyWithSession, fluffy);

        seedu.address.model.person.Person updatedPerson = modelManager.getFilteredPersonList().stream()
                .filter(p -> p.getName().toString().equals("John"))
                .findFirst()
                .orElseThrow();

        boolean hasSession = updatedPerson.getFeedingSessionIds().contains(session.getId());

        assertTrue(hasSession, "Person should still have the feeding session ID");
    }

    @Test
    public void deletePerson_withFeedingSessions_feedingSessionsRemovedFromAnimals() {
        modelManager = new ModelManager();

        seedu.address.model.person.Person max = new seedu.address.testutil.PersonBuilder()
                .withName("Max")
                .withPhone("81111111")
                .withEmail("max@example.com")
                .build();

        seedu.address.model.animal.Animal matt = new seedu.address.testutil.AnimalBuilder()
                .withName("Matt")
                .withDescription("Dog")
                .withLocation("Shelter A")
                .build();

        seedu.address.model.animal.Animal luna = new seedu.address.testutil.AnimalBuilder()
                .withName("Luna")
                .withDescription("Cat")
                .withLocation("Shelter B")
                .build();

        modelManager.addPerson(max);
        modelManager.addAnimal(matt);
        modelManager.addAnimal(luna);

        java.time.LocalDateTime feedingTime1 = java.time.LocalDateTime.of(2024, 10, 20, 9, 0);
        java.time.LocalDateTime feedingTime2 = java.time.LocalDateTime.of(2024, 10, 20, 15, 0);

        seedu.address.model.feedingsession.FeedingSession session1 =
                new seedu.address.model.feedingsession.FeedingSession(matt.getId(), max.getId(), feedingTime1);
        seedu.address.model.feedingsession.FeedingSession session2 =
                new seedu.address.model.feedingsession.FeedingSession(luna.getId(), max.getId(), feedingTime2);

        java.util.Set<java.util.UUID> maxSessionIds = new java.util.HashSet<>();
        maxSessionIds.add(session1.getId());
        maxSessionIds.add(session2.getId());

        java.util.Set<java.util.UUID> mattSessionIds = new java.util.HashSet<>();
        mattSessionIds.add(session1.getId());

        java.util.Set<java.util.UUID> lunaSessionIds = new java.util.HashSet<>();
        lunaSessionIds.add(session2.getId());

        seedu.address.model.person.Person maxWithSessions = new seedu.address.model.person.Person(
                max.getId(), max.getName(), max.getPhone(), max.getEmail(), max.getTags(), maxSessionIds);
        seedu.address.model.animal.Animal mattWithSessions = new seedu.address.model.animal.Animal(
                matt.getId(), matt.getName(), matt.getDescription(),
                matt.getLocation(), matt.getTags(), mattSessionIds);
        seedu.address.model.animal.Animal lunaWithSessions = new seedu.address.model.animal.Animal(
                luna.getId(), luna.getName(), luna.getDescription(),
                luna.getLocation(), luna.getTags(), lunaSessionIds);

        modelManager.setPerson(max, maxWithSessions);
        modelManager.setAnimal(matt, mattWithSessions);
        modelManager.setAnimal(luna, lunaWithSessions);

        modelManager.deletePerson(maxWithSessions);

        assertFalse(modelManager.hasPerson(maxWithSessions), "Person Max should be deleted");
        assertTrue(modelManager.hasAnimal(mattWithSessions), "Animal Matt should still exist");
        assertTrue(modelManager.hasAnimal(lunaWithSessions), "Animal Luna should still exist");

        seedu.address.model.animal.Animal updatedMatt = modelManager.getFilteredAnimalList().stream()
                .filter(a -> a.getName().toString().equals("Matt"))
                .findFirst()
                .orElseThrow();

        assertTrue(updatedMatt.getFeedingSessionIds().isEmpty(),
                "Matt should have no feeding sessions after Max is deleted");

        seedu.address.model.animal.Animal updatedLuna = modelManager.getFilteredAnimalList().stream()
                .filter(a -> a.getName().toString().equals("Luna"))
                .findFirst()
                .orElseThrow();

        assertTrue(updatedLuna.getFeedingSessionIds().isEmpty(),
                "Luna should have no feeding sessions after Max is deleted");
    }

    @Test
    public void deleteAnimal_withFeedingSessions_feedingSessionsRemovedFromPersons() {
        modelManager = new ModelManager();

        seedu.address.model.animal.Animal buddy = new seedu.address.testutil.AnimalBuilder()
                .withName("Buddy")
                .withDescription("Hamster")
                .withLocation("Shelter C")
                .build();

        seedu.address.model.person.Person alice = new seedu.address.testutil.PersonBuilder()
                .withName("Alice")
                .withPhone("82222222")
                .withEmail("alice@example.com")
                .build();

        seedu.address.model.person.Person bob = new seedu.address.testutil.PersonBuilder()
                .withName("Bob")
                .withPhone("83333333")
                .withEmail("bob@example.com")
                .build();

        modelManager.addAnimal(buddy);
        modelManager.addPerson(alice);
        modelManager.addPerson(bob);

        java.time.LocalDateTime feedingTime1 = java.time.LocalDateTime.of(2024, 10, 21, 10, 0);
        java.time.LocalDateTime feedingTime2 = java.time.LocalDateTime.of(2024, 10, 21, 16, 0);

        seedu.address.model.feedingsession.FeedingSession session1 =
                new seedu.address.model.feedingsession.FeedingSession(buddy.getId(), alice.getId(), feedingTime1);
        seedu.address.model.feedingsession.FeedingSession session2 =
                new seedu.address.model.feedingsession.FeedingSession(buddy.getId(), bob.getId(), feedingTime2);

        java.util.Set<java.util.UUID> buddySessionIds = new java.util.HashSet<>();
        buddySessionIds.add(session1.getId());
        buddySessionIds.add(session2.getId());

        java.util.Set<java.util.UUID> aliceSessionIds = new java.util.HashSet<>();
        aliceSessionIds.add(session1.getId());

        java.util.Set<java.util.UUID> bobSessionIds = new java.util.HashSet<>();
        bobSessionIds.add(session2.getId());

        seedu.address.model.animal.Animal buddyWithSessions = new seedu.address.model.animal.Animal(
                buddy.getId(), buddy.getName(), buddy.getDescription(),
                buddy.getLocation(), buddy.getTags(), buddySessionIds);
        seedu.address.model.person.Person aliceWithSessions = new seedu.address.model.person.Person(
                alice.getId(), alice.getName(), alice.getPhone(),
                alice.getEmail(), alice.getTags(), aliceSessionIds);
        seedu.address.model.person.Person bobWithSessions = new seedu.address.model.person.Person(
                bob.getId(), bob.getName(), bob.getPhone(),
                bob.getEmail(), bob.getTags(), bobSessionIds);

        modelManager.setAnimal(buddy, buddyWithSessions);
        modelManager.setPerson(alice, aliceWithSessions);
        modelManager.setPerson(bob, bobWithSessions);

        modelManager.deleteAnimal(buddyWithSessions);

        assertFalse(modelManager.hasAnimal(buddyWithSessions), "Animal Buddy should be deleted");
        assertTrue(modelManager.hasPerson(aliceWithSessions), "Person Alice should still exist");
        assertTrue(modelManager.hasPerson(bobWithSessions), "Person Bob should still exist");

        seedu.address.model.person.Person updatedAlice = modelManager.getFilteredPersonList().stream()
                .filter(p -> p.getName().toString().equals("Alice"))
                .findFirst()
                .orElseThrow();

        assertTrue(updatedAlice.getFeedingSessionIds().isEmpty(),
                "Alice should have no feeding sessions after Buddy is deleted");

        seedu.address.model.person.Person updatedBob = modelManager.getFilteredPersonList().stream()
                .filter(p -> p.getName().toString().equals("Bob"))
                .findFirst()
                .orElseThrow();

        assertTrue(updatedBob.getFeedingSessionIds().isEmpty(),
                "Bob should have no feeding sessions after Buddy is deleted");
    }
}
