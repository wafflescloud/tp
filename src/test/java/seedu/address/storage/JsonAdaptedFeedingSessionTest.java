package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.storage.JsonAdaptedFeedingSession.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.feedingsession.FeedingSession;

public class JsonAdaptedFeedingSessionTest {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Test
    public void toModelType_validFeedingSession_returnsFeedingSession() throws Exception {
        UUID animalId = UUID.randomUUID();
        UUID personId = UUID.randomUUID();
        LocalDateTime time = LocalDateTime.of(2025, 1, 1, 10, 15, 30);
        FeedingSession source = new FeedingSession(animalId, personId, time);

        JsonAdaptedFeedingSession adapted = new JsonAdaptedFeedingSession(source);
        FeedingSession model = adapted.toModelType();

        assertEquals(source, model);
        assertEquals(source.getAnimalId(), model.getAnimalId());
        assertEquals(source.getPersonId(), model.getPersonId());
        assertEquals(source.getDateTime(), model.getDateTime());
    }

    @Test
    public void toModelType_missingAnimalId_throwsIllegalValueException() {
        String id = UUID.randomUUID().toString();
        String personId = UUID.randomUUID().toString();
        String dateTime = LocalDateTime.of(2025, 1, 1, 10, 15, 30).format(FMT);

        JsonAdaptedFeedingSession adapted = new JsonAdaptedFeedingSession(id, null, personId, dateTime);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "animalId");
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void toModelType_missingPersonId_throwsIllegalValueException() {
        String id = UUID.randomUUID().toString();
        String animalId = UUID.randomUUID().toString();
        String dateTime = LocalDateTime.of(2025, 1, 1, 10, 15, 30).format(FMT);

        JsonAdaptedFeedingSession adapted = new JsonAdaptedFeedingSession(id, animalId, null, dateTime);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "personId");
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void toModelType_missingDateTime_throwsIllegalValueException() {
        String id = UUID.randomUUID().toString();
        String animalId = UUID.randomUUID().toString();
        String personId = UUID.randomUUID().toString();

        JsonAdaptedFeedingSession adapted = new JsonAdaptedFeedingSession(id, animalId, personId, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "dateTime");
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void toModelType_nullId_generatesRandomUuid() throws Exception {
        String animalId = UUID.randomUUID().toString();
        String personId = UUID.randomUUID().toString();
        String dateTime = LocalDateTime.of(2025, 1, 1, 10, 15, 30).format(FMT);

        JsonAdaptedFeedingSession adapted = new JsonAdaptedFeedingSession(null, animalId, personId, dateTime);
        FeedingSession first = adapted.toModelType();
        FeedingSession second = adapted.toModelType();

        assertNotNull(first.getId());
        assertNotNull(second.getId());
        assertNotEquals(first.getId(), second.getId());
    }

    @Test
    public void toModelType_invalidId_throwsIllegalValueException() {
        String corrupted = "obviously wrong uuid";
        String animalId = UUID.randomUUID().toString();
        String personId = UUID.randomUUID().toString();
        String dateTime = LocalDateTime.of(2025, 1, 1, 10, 15, 30).format(FMT);

        JsonAdaptedFeedingSession adapted = new JsonAdaptedFeedingSession(corrupted, animalId, personId, dateTime);
        String expectedMessage = "Invalid UUID or DateTime format";
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void toModelType_invalidDateTime_throwsIllegalValueException() {
        String id = UUID.randomUUID().toString();
        String animalId = UUID.randomUUID().toString();
        String personId = UUID.randomUUID().toString();
        String corrupted = "2025-12-25 0230";

        JsonAdaptedFeedingSession adapted = new JsonAdaptedFeedingSession(id, animalId, personId, corrupted);
        String expectedMessage = "Corrupted DateTime field: " + corrupted;
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }
}
