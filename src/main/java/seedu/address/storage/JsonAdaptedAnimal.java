package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Name;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.Description;
import seedu.address.model.animal.Location;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Animal}.
 */
class JsonAdaptedAnimal {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Animal's %s field is missing!";

    private final String id;
    private final String name;
    private final String description;
    private final String location;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<String> feedingSessionIds = new ArrayList<>();

    @JsonCreator
    public JsonAdaptedAnimal(@JsonProperty("id") String id,
                             @JsonProperty("name") String name,
                             @JsonProperty("description") String description,
                             @JsonProperty("location") String location,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags,
                             @JsonProperty("feedingSessionIds") List<String> feedingSessionIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (feedingSessionIds != null) {
            this.feedingSessionIds.addAll(feedingSessionIds);
        }
    }

    public JsonAdaptedAnimal(Animal source) {
        id = source.getId().toString();
        name = source.getName().fullName;
        description = source.getDescription().value;
        location = source.getLocation().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        feedingSessionIds.addAll(source.getFeedingSessionIds().stream()
                .map(UUID::toString)
                .collect(Collectors.toList()));
    }

    public String getId() {
        return id;
    }

    public List<String> getFeedingSessionIds() {
        return feedingSessionIds;
    }

    public Animal toModelType() throws IllegalValueException {
        final List<Tag> animalTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            animalTags.add(tag.toModelType());
        }

        final UUID modelId;
        if (id == null) {
            modelId = UUID.randomUUID();
        } else {
            try {
                modelId = UUID.fromString(id);
            } catch (IllegalArgumentException e) {
                throw new IllegalValueException("Invalid UUID format for id");
            }
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

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

        final Set<UUID> modelFeedingSessionIds = feedingSessionIds.stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        return new Animal(modelId, modelName, modelDescription, modelLocation, modelTags, modelFeedingSessionIds);
    }
}
