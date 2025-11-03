package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.animal.Animal;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_returnsNonEmptyArray() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertNotNull(persons);
        assertTrue(persons.length > 0);
    }

    @Test
    public void getSamplePersons_returnsExpectedNumberOfPersons() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertEquals(6, persons.length);
    }

    @Test
    public void getSamplePersons_allPersonsHaveValidFields() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        for (Person person : persons) {
            assertNotNull(person.getName());
            assertNotNull(person.getPhone());
            assertNotNull(person.getEmail());
            assertNotNull(person.getTags());
            assertNotNull(person.getFeedingSessionIds());
        }
    }

    @Test
    public void getSamplePersons_personHasCorrectName() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertEquals("Alex Yeoh", persons[0].getName().fullName);
        assertEquals("Bernice Yu", persons[1].getName().fullName);
        assertEquals("Charlotte Oliveiro", persons[2].getName().fullName);
        assertEquals("David Li", persons[3].getName().fullName);
        assertEquals("Irfan Ibrahim", persons[4].getName().fullName);
        assertEquals("Roy Balakrishnan", persons[5].getName().fullName);
    }

    @Test
    public void getSamplePersons_personHasCorrectPhone() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertEquals("87438807", persons[0].getPhone().value);
        assertEquals("99272758", persons[1].getPhone().value);
    }

    @Test
    public void getSamplePersons_personHasCorrectEmail() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertEquals("alexyeoh@example.com", persons[0].getEmail().value);
        assertEquals("berniceyu@example.com", persons[1].getEmail().value);
    }

    @Test
    public void getSamplePersons_personHasCorrectTags() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        // Alex has "friends" tag
        assertTrue(persons[0].getTags().contains(new Tag("friends")));
        // Bernice has "colleagues" and "friends" tags
        assertTrue(persons[1].getTags().contains(new Tag("colleagues")));
        assertTrue(persons[1].getTags().contains(new Tag("friends")));
        assertEquals(2, persons[1].getTags().size());
    }

    @Test
    public void getSamplePersons_allPersonsHaveEmptyFeedingSessions() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        for (Person person : persons) {
            assertTrue(person.getFeedingSessionIds().isEmpty());
        }
    }

    @Test
    public void getSampleAnimals_returnsNonEmptyArray() {
        Animal[] animals = SampleDataUtil.getSampleAnimals();
        assertNotNull(animals);
        assertTrue(animals.length > 0);
    }

    @Test
    public void getSampleAnimals_returnsExpectedNumberOfAnimals() {
        Animal[] animals = SampleDataUtil.getSampleAnimals();
        assertEquals(4, animals.length);
    }

    @Test
    public void getSampleAnimals_allAnimalsHaveValidFields() {
        Animal[] animals = SampleDataUtil.getSampleAnimals();
        for (Animal animal : animals) {
            assertNotNull(animal.getName());
            assertNotNull(animal.getDescription());
            assertNotNull(animal.getLocation());
            assertNotNull(animal.getTags());
            assertNotNull(animal.getFeedingSessionIds());
        }
    }

    @Test
    public void getSampleAnimals_animalHasCorrectName() {
        Animal[] animals = SampleDataUtil.getSampleAnimals();
        assertEquals("Max", animals[0].getName().fullName);
        assertEquals("Luna", animals[1].getName().fullName);
        assertEquals("Charlie", animals[2].getName().fullName);
        assertEquals("Bella", animals[3].getName().fullName);
    }

    @Test
    public void getSampleAnimals_animalHasCorrectDescription() {
        Animal[] animals = SampleDataUtil.getSampleAnimals();
        assertEquals("Golden Retriever, friendly and well-trained", animals[0].getDescription().value);
        assertEquals("Persian cat, white fur, blue eyes", animals[1].getDescription().value);
    }

    @Test
    public void getSampleAnimals_animalHasCorrectLocation() {
        Animal[] animals = SampleDataUtil.getSampleAnimals();
        assertEquals("Block A, Kennel 1", animals[0].getLocation().value);
        assertEquals("Block B, Cat Room 3", animals[1].getLocation().value);
        assertEquals("Block A, Kennel 5", animals[2].getLocation().value);
        assertEquals("Block B, Cat Room 2", animals[3].getLocation().value);
    }

    @Test
    public void getSampleAnimals_animalHasCorrectTags() {
        Animal[] animals = SampleDataUtil.getSampleAnimals();
        assertTrue(animals[0].getTags().contains(new Tag("Shy")));
        assertTrue(animals[1].getTags().contains(new Tag("Rowdy")));
        assertTrue(animals[2].getTags().contains(new Tag("Playful")));
        assertTrue(animals[3].getTags().contains(new Tag("Fierce")));
    }

    @Test
    public void getSampleAnimals_allAnimalsHaveEmptyFeedingSessions() {
        Animal[] animals = SampleDataUtil.getSampleAnimals();
        for (Animal animal : animals) {
            assertTrue(animal.getFeedingSessionIds().isEmpty());
        }
    }

    @Test
    public void getSampleAddressBook_returnsNonNullAddressBook() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();
        assertNotNull(addressBook);
    }

    @Test
    public void getSampleAddressBook_containsAllSamplePersons() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();
        Person[] samplePersons = SampleDataUtil.getSamplePersons();
        assertEquals(samplePersons.length, addressBook.getPersonList().size());
    }

    @Test
    public void getSampleAddressBook_containsAllSampleAnimals() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();
        Animal[] sampleAnimals = SampleDataUtil.getSampleAnimals();
        assertEquals(sampleAnimals.length, addressBook.getAnimalList().size());
    }

    @Test
    public void getSampleAddressBook_personsAreAddedCorrectly() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();
        Person[] samplePersons = SampleDataUtil.getSamplePersons();

        for (Person person : samplePersons) {
            boolean found = addressBook.getPersonList().stream()
                    .anyMatch(p -> p.getName().equals(person.getName()));
            assertTrue(found, "Person " + person.getName() + " should be in address book");
        }
    }

    @Test
    public void getSampleAddressBook_animalsAreAddedCorrectly() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();
        Animal[] sampleAnimals = SampleDataUtil.getSampleAnimals();

        for (Animal animal : sampleAnimals) {
            boolean found = addressBook.getAnimalList().stream()
                    .anyMatch(a -> a.getName().equals(animal.getName()));
            assertTrue(found, "Animal " + animal.getName() + " should be in address book");
        }
    }

    @Test
    public void getTagSet_emptyArray_returnsEmptySet() {
        Set<Tag> tags = SampleDataUtil.getTagSet();
        assertNotNull(tags);
        assertTrue(tags.isEmpty());
    }

    @Test
    public void getTagSet_singleTag_returnsSetWithOneTag() {
        Set<Tag> tags = SampleDataUtil.getTagSet("friend");
        assertEquals(1, tags.size());
        assertTrue(tags.contains(new Tag("friend")));
    }

    @Test
    public void getTagSet_multipleTags_returnsSetWithAllTags() {
        Set<Tag> tags = SampleDataUtil.getTagSet("friend", "colleague", "family");
        assertEquals(3, tags.size());
        assertTrue(tags.contains(new Tag("friend")));
        assertTrue(tags.contains(new Tag("colleague")));
        assertTrue(tags.contains(new Tag("family")));
    }

    @Test
    public void getTagSet_duplicateTags_returnsSetWithUniqueTagsOnly() {
        Set<Tag> tags = SampleDataUtil.getTagSet("friend", "friend", "colleague");
        assertEquals(2, tags.size());
        assertTrue(tags.contains(new Tag("friend")));
        assertTrue(tags.contains(new Tag("colleague")));
    }

    @Test
    public void getFeedingSessionSet_returnsEmptySet() {
        Set<UUID> sessions = SampleDataUtil.getFeedingSessionSet();
        assertNotNull(sessions);
        assertTrue(sessions.isEmpty());
    }

    @Test
    public void getFeedingSessionSet_returnsNewInstanceEachTime() {
        Set<UUID> sessions1 = SampleDataUtil.getFeedingSessionSet();
        Set<UUID> sessions2 = SampleDataUtil.getFeedingSessionSet();
        assertFalse(sessions1 == sessions2); // Different instances
        assertEquals(sessions1, sessions2); // But equal content
    }

    @Test
    public void getTagSet_returnsNewInstanceEachTime() {
        Set<Tag> tags1 = SampleDataUtil.getTagSet("friend");
        Set<Tag> tags2 = SampleDataUtil.getTagSet("friend");
        assertFalse(tags1 == tags2); // Different instances
        assertEquals(tags1, tags2); // But equal content
    }
}
