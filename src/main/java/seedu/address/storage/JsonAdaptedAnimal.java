package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.AnimalName;
import seedu.address.model.animal.Description;
import seedu.address.model.animal.Location;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Animal}.
 */
class JsonAdaptedAnimal {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Animal's %s field is missing!";

    private final String name;
    private final String description;
    private final String location;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedAnimal} with the given animal details.
     */
    @JsonCreator
    public JsonAdaptedAnimal(@JsonProperty("name") String name,
                             @JsonProperty("description") String description,
                             @JsonProperty("location") String location,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.name = name;
        this.description = description;
        this.location = location;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Animal} into this class for Jackson use.
     */
    public JsonAdaptedAnimal(Animal source) {
        name = source.getName().fullName;
        description = source.getDescription().value;
        location = source.getLocation().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted animal object into the model's {@code Animal} object.
     */
    public Animal toModelType() throws IllegalValueException {
        final List<Tag> animalTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            animalTags.add(tag.toModelType());
        }

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

        final Set<Tag> modelTags = new HashSet<>(animalTags);
        return new Animal(modelName, modelDescription, modelLocation, modelTags);
    }
}
