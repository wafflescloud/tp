package seedu.address.storage;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.animal.Animal;
import seedu.address.model.feedingsession.FeedingSession;

/**
 * Jackson-friendly version of {@link FeedingSession}.
 */
class JsonAdaptedFeedingSession {
    private final JsonAdaptedAnimal animal;
    private final String feedingTime;

    /**
     * Constructs a {@code JsonAdaptedFeedingSession} with the given details.
     */
    @JsonCreator
    public JsonAdaptedFeedingSession(@JsonProperty("animal") JsonAdaptedAnimal animal,
            @JsonProperty("feedingTime") String feedingTime) {
        this.animal = animal;
        this.feedingTime = feedingTime;
    }

    /**
     * Converts a given {@code FeedingSession} into this class for Jackson use.
     */
    public JsonAdaptedFeedingSession(FeedingSession source) {
        this.animal = new JsonAdaptedAnimal(source.getAnimal());
        this.feedingTime = source.getFeedingTime().toString();
    }

    /**
     * Converts this Jackson-friendly adapted feeding session object into the model's {@code FeedingSession} object.
     */
    public FeedingSession toModelType() throws IllegalValueException {
        Animal modelAnimal = animal.toModelType();
        LocalDateTime modelFeedingTime = LocalDateTime.parse(feedingTime);
        return new FeedingSession(modelAnimal, modelFeedingTime);
    }
}
