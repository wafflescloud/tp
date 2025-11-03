package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.animal.Animal;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalAnimals;
import seedu.address.testutil.TypicalPersons;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.json");
    private static final Path TYPICAL_ANIMALS_FILE = TEST_DATA_FOLDER.resolve("typicalAnimalsAddressBook.json");
    private static final Path INVALID_ANIMAL_FILE = TEST_DATA_FOLDER.resolve("invalidAnimalAddressBook.json");
    private static final Path DUPLICATE_ANIMAL_FILE = TEST_DATA_FOLDER.resolve("duplicateAnimalAddressBook.json");
    private static final Path TYPICAL_FEEDINGSESSIONS_FILE = TEST_DATA_FOLDER
            .resolve("typicalFeedingSessionsAddressBook.json");
    private static final Path INVALID_FEEDINGSESSION_FILE = TEST_DATA_FOLDER
            .resolve("invalidFeedingSessionAddressBook.json");
    private static final Path DUPLICATE_FEEDINGSESSION_FILE = TEST_DATA_FOLDER
            .resolve("duplicateFeedingSessionAddressBook.json");
    private static final Path ORPHANED_PERSON_SESSION_FILE = TEST_DATA_FOLDER
            .resolve("orphanedPersonFeedingSessionAddressBook.json");
    private static final Path ORPHANED_ANIMAL_SESSION_FILE = TEST_DATA_FOLDER
            .resolve("orphanedAnimalFeedingSessionAddressBook.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalPersonsAddressBook = TypicalPersons.getTypicalAddressBook();

        assertEquals(typicalPersonsAddressBook.getPersonList().size(), addressBookFromFile.getPersonList().size());
        Set<String> expected = new HashSet<>();
        for (Person p : typicalPersonsAddressBook.getPersonList()) {
            expected.add(p.getName() + "|" + p.getPhone() + "|" + p.getEmail() + "|" + p.getTags());
        }
        for (Person p : addressBookFromFile.getPersonList()) {
            String key = p.getName() + "|" + p.getPhone() + "|" + p.getEmail() + "|" + p.getTags();
            assertTrue(expected.contains(key), "Unexpected person loaded: " + p);
        }
    }

    @Test
    public void toModelType_typicalAnimalsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_ANIMALS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalAnimalsAddressBook = TypicalAnimals.getTypicalAddressBook();

        assertEquals(typicalAnimalsAddressBook.getAnimalList().size(),
                addressBookFromFile.getAnimalList().size());
        Set<String> expected = new HashSet<>();
        for (Animal a : typicalAnimalsAddressBook.getAnimalList()) {
            expected.add(a.getName() + "|" + a.getDescription() + "|" + a.getLocation() + "|" + a.getTags());
        }
        for (Animal a : addressBookFromFile.getAnimalList()) {
            String key = a.getName() + "|" + a.getDescription() + "|" + a.getLocation() + "|" + a.getTags();
            assertTrue(expected.contains(key), "Unexpected animal loaded: " + a);
        }
    }

    @Test
    public void toModelType_typicalFeedingSessionsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_FEEDINGSESSIONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();

        Set<String> sessionIds = new HashSet<>();
        addressBookFromFile.getFeedingSessionList().forEach(fs -> sessionIds.add(fs.getId().toString()));
        addressBookFromFile.getPersonList().forEach(p ->
                p.getFeedingSessionIds().forEach(id -> assertTrue(sessionIds.contains(id.toString()))));
        addressBookFromFile.getAnimalList().forEach(a ->
                a.getFeedingSessionIds().forEach(id -> assertTrue(sessionIds.contains(id.toString()))));

        assertEquals(2, addressBookFromFile.getPersonList().size());
        assertEquals(2, addressBookFromFile.getAnimalList().size());
        assertEquals(2, addressBookFromFile.getFeedingSessionList().size());
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_invalidAnimalFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_ANIMAL_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_invalidFeedingSessionFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_FEEDINGSESSION_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateAnimals_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_ANIMAL_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_ANIMAL,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateFeedingSessions_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_FEEDINGSESSION_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_FEEDING_SESSION,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_orphanedPersonFeedingSession_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(ORPHANED_PERSON_SESSION_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_ORPHANED_FEEDING_SESSION,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_orphanedAnimalFeedingSession_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(ORPHANED_ANIMAL_SESSION_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_ORPHANED_FEEDING_SESSION,
                dataFromFile::toModelType);
    }
}
