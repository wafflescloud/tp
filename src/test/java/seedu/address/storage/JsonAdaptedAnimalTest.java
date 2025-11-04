package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.storage.JsonAdaptedAnimal.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalAnimals.WHISKERS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Name;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.Description;
import seedu.address.model.animal.Location;

public class JsonAdaptedAnimalTest {

    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_DESCRIPTION = "   "; // blank
    private static final String INVALID_LOCATION = "   "; // blank
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_ID = WHISKERS.getId().toString();
    private static final String VALID_NAME = WHISKERS.getName().toString();
    private static final String VALID_DESCRIPTION = WHISKERS.getDescription().toString();
    private static final String VALID_LOCATION = WHISKERS.getLocation().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = WHISKERS.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final List<String> VALID_FEEDING_SESSION_IDS = WHISKERS.getFeedingSessionIds().stream()
            .map(UUID::toString)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validAnimalDetails_returnsAnimal() throws Exception {
        JsonAdaptedAnimal adapted = new JsonAdaptedAnimal(WHISKERS);
        Animal model = adapted.toModelType();
        assertEquals(WHISKERS, model);
    }

    @Test
    public void constructor_nullTags_toModelTypeYieldsEmptyTags() throws Exception {
        UUID s1 = UUID.randomUUID();
        List<String> sessions = Arrays.asList(s1.toString());
        JsonAdaptedAnimal adapted = new JsonAdaptedAnimal(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                VALID_LOCATION, null, sessions);
        Animal model = adapted.toModelType();
        assertTrue(model.getTags().isEmpty(), "Expected no tags when Json tags list is null");
        assertEquals(new HashSet<>(Arrays.asList(s1)), model.getFeedingSessionIds());
        assertEquals(new Name(VALID_NAME), model.getName());
        assertEquals(new Description(VALID_DESCRIPTION), model.getDescription());
        assertEquals(new Location(VALID_LOCATION), model.getLocation());
    }

    @Test
    public void constructor_nullFeedingSessionIds_toModelTypeYieldsEmptySessions() throws Exception {
        List<JsonAdaptedTag> tags = Arrays.asList(new JsonAdaptedTag("friendly"));
        JsonAdaptedAnimal adapted = new JsonAdaptedAnimal(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                VALID_LOCATION, tags, null);
        Animal model = adapted.toModelType();
        assertTrue(model.getFeedingSessionIds().isEmpty(), "Expected no feeding sessions when list is null");
        Set<String> tagNames = model.getTags().stream().map(t -> t.tagName).collect(Collectors.toSet());
        assertEquals(new HashSet<>(Arrays.asList("friendly")), tagNames);
        assertEquals(new Name(VALID_NAME), model.getName());
        assertEquals(new Description(VALID_DESCRIPTION), model.getDescription());
        assertEquals(new Location(VALID_LOCATION), model.getLocation());
    }

    @Test
    public void getId_fromAdaptedAnimal_matchesSourceId() {
        JsonAdaptedAnimal adapted = new JsonAdaptedAnimal(WHISKERS);
        assertEquals(WHISKERS.getId().toString(), adapted.getId());

        JsonAdaptedAnimal adaptedExplicit = new JsonAdaptedAnimal(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                VALID_LOCATION, VALID_TAGS, VALID_FEEDING_SESSION_IDS);
        assertEquals(VALID_ID, adaptedExplicit.getId());
    }

    @Test
    public void getFeedingSessionIds_fromAdaptedAnimal_containsAllSessionIds() {
        JsonAdaptedAnimal adapted = new JsonAdaptedAnimal(WHISKERS);
        List<String> actual = adapted.getFeedingSessionIds();
        Set<String> expectedSet = new HashSet<>(VALID_FEEDING_SESSION_IDS);
        Set<String> actualSet = new HashSet<>(actual);
        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void toModelType_invalidId_throwsIllegalValueException() {
        JsonAdaptedAnimal adapted = new JsonAdaptedAnimal("not-a-valid-uuid", VALID_NAME, VALID_DESCRIPTION,
                VALID_LOCATION, VALID_TAGS, VALID_FEEDING_SESSION_IDS);
        String expectedMessage = "Invalid UUID format for id";
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void toModelType_nullId_allowsRandomUuid() throws Exception {
        JsonAdaptedAnimal adapted = new JsonAdaptedAnimal(null, VALID_NAME, VALID_DESCRIPTION,
                VALID_LOCATION, VALID_TAGS, VALID_FEEDING_SESSION_IDS);
        Animal model = adapted.toModelType();
        assertNotNull(model.getId());
        assertEquals(new Name(VALID_NAME), model.getName());
        assertEquals(new Description(VALID_DESCRIPTION), model.getDescription());
        assertEquals(new Location(VALID_LOCATION), model.getLocation());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedAnimal adapted = new JsonAdaptedAnimal(VALID_ID, INVALID_NAME, VALID_DESCRIPTION,
                VALID_LOCATION, VALID_TAGS, VALID_FEEDING_SESSION_IDS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedAnimal adapted = new JsonAdaptedAnimal(VALID_ID, null, VALID_DESCRIPTION,
                VALID_LOCATION, VALID_TAGS, VALID_FEEDING_SESSION_IDS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void toModelType_invalidDescription_throwsIllegalValueException() {
        JsonAdaptedAnimal adapted = new JsonAdaptedAnimal(VALID_ID, VALID_NAME, INVALID_DESCRIPTION,
                VALID_LOCATION, VALID_TAGS, VALID_FEEDING_SESSION_IDS);
        String expectedMessage = Description.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        JsonAdaptedAnimal adapted = new JsonAdaptedAnimal(VALID_ID, VALID_NAME, null,
                VALID_LOCATION, VALID_TAGS, VALID_FEEDING_SESSION_IDS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void toModelType_invalidLocation_throwsIllegalValueException() {
        JsonAdaptedAnimal adapted = new JsonAdaptedAnimal(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                INVALID_LOCATION, VALID_TAGS, VALID_FEEDING_SESSION_IDS);
        String expectedMessage = Location.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void toModelType_nullLocation_throwsIllegalValueException() {
        JsonAdaptedAnimal adapted = new JsonAdaptedAnimal(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                null, VALID_TAGS, VALID_FEEDING_SESSION_IDS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Location.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedAnimal adapted = new JsonAdaptedAnimal(VALID_ID, VALID_NAME, VALID_DESCRIPTION,
                VALID_LOCATION, invalidTags, VALID_FEEDING_SESSION_IDS);
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }
}
