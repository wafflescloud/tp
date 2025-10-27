package seedu.address.storage;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.feedingsession.FeedingSession;

/**
 * Jackson-friendly version of {@link FeedingSession}.
 * Stores only names and datetime to prevent infinite loops during serialization.
 */
class JsonAdaptedFeedingSession {
    private final String personName;
    private final String animalName;
    private final String feedingTime;

    /**
     * Constructs a {@code JsonAdaptedFeedingSession} with the given details.
     */
    @JsonCreator
    public JsonAdaptedFeedingSession(@JsonProperty("personName") String personName,
            @JsonProperty("animalName") String animalName,
            @JsonProperty("feedingTime") String feedingTime) {
        this.personName = personName;
        this.animalName = animalName;
        this.feedingTime = feedingTime;
    }

    /**
     * Converts a given {@code FeedingSession} into this class for Jackson use.
     */
    public JsonAdaptedFeedingSession(FeedingSession source) {
        this.personName = source.getPersonName();
        this.animalName = source.getAnimalName();
        this.feedingTime = source.getFeedingTime().toString();
    }

    /**
     * Converts this Jackson-friendly adapted feeding session object into the model's {@code FeedingSession} object.
     */
    public FeedingSession toModelType() throws IllegalValueException {
        if (personName == null) {
            throw new IllegalValueException("Person name is missing!");
        }
        if (animalName == null) {
            throw new IllegalValueException("Animal name is missing!");
        }
        if (feedingTime == null) {
            throw new IllegalValueException("Feeding time is missing!");
        }
        LocalDateTime modelFeedingTime = LocalDateTime.parse(feedingTime);
        return new FeedingSession(personName, animalName, modelFeedingTime);
    }
}
