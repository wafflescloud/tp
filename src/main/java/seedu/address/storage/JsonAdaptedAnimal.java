package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.AnimalName;
import seedu.address.model.animal.Description;
import seedu.address.model.animal.Location;

/**
 * Jackson-friendly version of {@link Animal}.
 */
public class JsonAdaptedAnimal {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Animal's %s field is missing!";

    private final String name;
    private final String description;
    private final String location;

    /**
     * Constructs a {@code JsonAdaptedAnimal} with the given animal details.
     */
    @JsonCreator
    public JsonAdaptedAnimal(@JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("location") String location) {
        this.name = name;
        this.description = description;
        this.location = location;
    }

    /**
     * Converts a given {@code Animal} into this class for Jackson use.
     */
    public JsonAdaptedAnimal(Animal source) {
        name = source.getName().fullName;
        description = source.getDescription().value;
        location = source.getLocation().value;
    }

    /**
     * Converts this Jackson-friendly adapted animal object into the model's {@code Animal} object.
     */
    public Animal toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    AnimalName.class.getSimpleName()));
        }
        if (!AnimalName.isValidName(name)) {
            throw new IllegalValueException(AnimalName.MESSAGE_CONSTRAINTS);
        }
        final AnimalName modelName = new AnimalName(name);

        if (description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(description)) {
            throw new IllegalValueException(Description.MESSAGE_CONSTRAINTS);
        }
        final Description modelDescription = new Description(description);

        if (location == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Location.class.getSimpleName()));
        }
        if (!Location.isValidLocation(location)) {
            throw new IllegalValueException(Location.MESSAGE_CONSTRAINTS);
        }
        final Location modelLocation = new Location(location);

        return new Animal(modelName, modelDescription, modelLocation);
    }
}
