package seedu.address.storage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.feedingsession.FeedingSession;

/**
 * Jackson-friendly version of {@link FeedingSession}.
 */
class JsonAdaptedFeedingSession {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "FeedingSession's %s field is missing!";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final String id;
    private final String animalId;
    private final String personId;
    private final String dateTime;
    private final String notes;

    @JsonCreator
    public JsonAdaptedFeedingSession(@JsonProperty("id") String id,
                                     @JsonProperty("animalId") String animalId,
                                     @JsonProperty("personId") String personId,
                                     @JsonProperty("dateTime") String dateTime,
                                     @JsonProperty("notes") String notes) {
        this.id = id;
        this.animalId = animalId;
        this.personId = personId;
        this.dateTime = dateTime;
        this.notes = notes;
    }

    public JsonAdaptedFeedingSession(FeedingSession source) {
        id = source.getId().toString();
        animalId = source.getAnimalId().toString();
        personId = source.getPersonId().toString();
        dateTime = source.getDateTime().format(DATE_TIME_FORMATTER);
        notes = source.getNotes();
    }

    public FeedingSession toModelType() throws IllegalValueException {
        if (animalId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "animalId"));
        }

        if (personId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "personId"));
        }

        if (dateTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "dateTime"));
        }

        final UUID modelId;
        final UUID modelAnimalId;
        final UUID modelPersonId;
        final LocalDateTime modelDateTime;

        try {
            modelId = (id == null) ? UUID.randomUUID() : UUID.fromString(id);
            modelAnimalId = UUID.fromString(animalId);
            modelPersonId = UUID.fromString(personId);
            modelDateTime = LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException("Invalid UUID or DateTime format");
        }

        final String modelNotes = notes != null ? notes : "";

        return new FeedingSession(modelId, modelAnimalId, modelPersonId, modelDateTime, modelNotes);
    }
}
