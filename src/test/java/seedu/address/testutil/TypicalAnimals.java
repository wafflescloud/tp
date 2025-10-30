package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.animal.Animal;

/**
 * A utility class containing a list of {@code Animal} objects (cats) to be used in tests.
 */
public class TypicalAnimals {

    public static final Animal WHISKERS = new AnimalBuilder()
            .withName("Whiskers")
            .withDescription("A playful tabby cat.")
            .withLocation("Shelter Room 2")
            .build();

    public static final Animal LUNA = new AnimalBuilder()
            .withName("Luna")
            .withDescription("Shy but affectionate Siamese cat.")
            .withLocation("Shelter Room 1")
            .build();

    public static final Animal SIMBA = new AnimalBuilder()
            .withName("Simba")
            .withDescription("Curious ginger kitten who loves to climb.")
            .withLocation("Shelter Room 3")
            .build();

    public static final Animal BELLA = new AnimalBuilder()
            .withName("Bella")
            .withDescription("Gentle Ragdoll cat with blue eyes.")
            .withLocation("Shelter Room 4")
            .build();

    public static final Animal OREO = new AnimalBuilder()
            .withName("Oreo")
            .withDescription("Black and white Tuxedo cat, very active.")
            .withLocation("Quarantine Zone")
            .build();

    public static final Animal MAX = new AnimalBuilder()
            .withName("Max")
            .withDescription("Black Cat")
            .withLocation("Campus Library")
            .build();

    public static final Animal FLUFFY = new AnimalBuilder()
            .withName("Fluffy")
            .withDescription("White Persian Cat")
            .withLocation("Student Dormitory")
            .build();

    private TypicalAnimals() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical animals.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Animal animal : getTypicalAnimals()) {
            ab.addAnimal(animal);
        }
        return ab;
    }

    public static List<Animal> getTypicalAnimals() {
        return new ArrayList<>(Arrays.asList(WHISKERS, LUNA, SIMBA, BELLA, OREO, MAX, FLUFFY));
    }
}
