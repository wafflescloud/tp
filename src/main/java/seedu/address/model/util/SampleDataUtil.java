package seedu.address.model.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.AnimalName;
import seedu.address.model.animal.Description;
import seedu.address.model.animal.Location;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.person.Email;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonName;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new PersonName("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                getTagSet("friends"),
                getFeedingSessionSet()),
            new Person(new PersonName("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                getTagSet("colleagues", "friends"),
                getFeedingSessionSet()),
            new Person(new PersonName("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                getTagSet("neighbours"),
                getFeedingSessionSet()),
            new Person(new PersonName("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                getTagSet("family"),
                getFeedingSessionSet()),
            new Person(new PersonName("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                getTagSet("classmates"),
                getFeedingSessionSet()),
            new Person(new PersonName("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                getTagSet("colleagues"),
                getFeedingSessionSet())
        };
    }

    public static Animal[] getSampleAnimals() {
        return new Animal[] {
            new Animal(new AnimalName("Max"),
                new Description("Golden Retriever, friendly and well-trained"),
                new Location("Block A, Kennel 1"), getTagSet("Shy")),
            new Animal(new AnimalName("Luna"),
                new Description("Persian cat, white fur, blue eyes"),
                new Location("Block B, Cat Room 3"), getTagSet("Rowdy")),
            new Animal(new AnimalName("Charlie"),
                new Description("German Shepherd, good guard dog"),
                new Location("Block A, Kennel 5"), getTagSet("Playful")),
            new Animal(new AnimalName("Bella"),
                new Description("Siamese cat, very playful"),
                new Location("Block B, Cat Room 2"), getTagSet("Fierce"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        for (Animal sampleAnimal : getSampleAnimals()) {
            sampleAb.addAnimal(sampleAnimal);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns an empty feeding session set.
     */
    public static Set<FeedingSession> getFeedingSessionSet() {
        return new HashSet<>();
    }

}
