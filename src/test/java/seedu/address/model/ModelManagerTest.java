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
import seedu.address.model.person.NameContainsKeywordsPredicatePerson;
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
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
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
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicatePerson(Arrays.asList(keywords)));
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
                .withPhone("12345678")
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
                new seedu.address.model.feedingsession.FeedingSession(matt, max, feedingTime);

        java.util.Set<seedu.address.model.feedingsession.FeedingSession> sessions = new java.util.HashSet<>();
        sessions.add(session);

        seedu.address.model.person.Person mattWithSession = new seedu.address.model.person.Person(
                matt.getName(), matt.getPhone(), matt.getEmail(), matt.getTags(), sessions);
        seedu.address.model.animal.Animal maxWithSession = new seedu.address.model.animal.Animal(
                max.getName(), max.getDescription(), max.getLocation(), max.getTags(), sessions);

        modelManager.setPerson(matt, mattWithSession);
        modelManager.setAnimal(max, maxWithSession);

        seedu.address.model.person.Person matthew = new seedu.address.testutil.PersonBuilder()
                .withName("Matthew")
                .withPhone("12345678")
                .withEmail("matt@example.com")
                .withFeedingSessions(sessions)
                .build();

        modelManager.setPerson(mattWithSession, matthew);

        seedu.address.model.animal.Animal updatedAnimal = modelManager.getFilteredAnimalList().stream()
                .filter(a -> a.getName().toString().equals("Max"))
                .findFirst()
                .orElseThrow();

        boolean hasUpdatedSession = updatedAnimal.getFeedingSessions().stream()
                .anyMatch(s -> s.getPersonName().equals("Matthew")
                        && s.getAnimalName().equals("Max")
                        && s.getFeedingTime().equals(feedingTime));

        assertTrue(hasUpdatedSession, "Animal's feeding session should have updated person name");

        boolean hasOldName = updatedAnimal.getFeedingSessions().stream()
                .anyMatch(s -> s.getPersonName().equals("Matt"));

        assertFalse(hasOldName, "Animal's feeding session should not contain old person name");
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
                new seedu.address.model.feedingsession.FeedingSession(john, buddy, feedingTime);

        java.util.Set<seedu.address.model.feedingsession.FeedingSession> sessions = new java.util.HashSet<>();
        sessions.add(session);

        seedu.address.model.person.Person johnWithSession = new seedu.address.model.person.Person(
                john.getName(), john.getPhone(), john.getEmail(), john.getTags(), sessions);
        seedu.address.model.animal.Animal buddyWithSession = new seedu.address.model.animal.Animal(
                buddy.getName(), buddy.getDescription(), buddy.getLocation(), buddy.getTags(), sessions);

        modelManager.setPerson(john, johnWithSession);
        modelManager.setAnimal(buddy, buddyWithSession);

        seedu.address.model.animal.Animal fluffy = new seedu.address.testutil.AnimalBuilder()
                .withName("Fluffy")
                .withDescription("Cat")
                .withLocation("Shelter B")
                .withFeedingSessions(sessions)
                .build();

        modelManager.setAnimal(buddyWithSession, fluffy);

        seedu.address.model.person.Person updatedPerson = modelManager.getFilteredPersonList().stream()
                .filter(p -> p.getName().toString().equals("John"))
                .findFirst()
                .orElseThrow();

        boolean hasUpdatedSession = updatedPerson.getFeedingSessions().stream()
                .anyMatch(s -> s.getPersonName().equals("John")
                        && s.getAnimalName().equals("Fluffy")
                        && s.getFeedingTime().equals(feedingTime));

        assertTrue(hasUpdatedSession, "Person's feeding session should have updated animal name");

        boolean hasOldName = updatedPerson.getFeedingSessions().stream()
                .anyMatch(s -> s.getAnimalName().equals("Buddy"));

        assertFalse(hasOldName, "Person's feeding session should not contain old animal name");
    }
}
