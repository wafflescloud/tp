package seedu.address.testutil;

import seedu.address.model.Name;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.AnimalName;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonName;

/**
 * Utility class for safely extracting specific name types from contacts.
 * This is primarily used in test utilities to handle the type conversion
 * from the generic Name to specific name types.
 */
public class NameUtil {

    /**
     * Safely extracts PersonName from a Person object.
     *
     * @param person The person object to extract name from
     * @return The PersonName of the person
     * @throws IllegalStateException if the person doesn't have a PersonName
     */
    public static PersonName getPersonName(Person person) {
        Name name = person.getName();
        if (name instanceof PersonName) {
            return (PersonName) name;
        }
        throw new IllegalStateException("Person should have PersonName, but got: " + name.getClass());
    }

    /**
     * Safely extracts AnimalName from an Animal object.
     *
     * @param animal The animal object to extract name from
     * @return The AnimalName of the animal
     * @throws IllegalStateException if the animal doesn't have an AnimalName
     */
    public static AnimalName getAnimalName(Animal animal) {
        Name name = animal.getName();
        if (name instanceof AnimalName) {
            return (AnimalName) name;
        }
        throw new IllegalStateException("Animal should have AnimalName, but got: " + name.getClass());
    }
}
