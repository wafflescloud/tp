package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Name;
import seedu.address.model.person.Email;
import seedu.address.model.person.Phone;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_ID = BENSON.getId().toString();
    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final List<String> VALID_FEEDING_SESSION_IDS = BENSON.getFeedingSessionIds().stream()
            .map(UUID::toString)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    // test the getId() method
    @Test
    public void getId_fromAdaptedPerson_matchesSourceId() {
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON.getId().toString(), adapted.getId());

        // Also verify when constructed directly with explicit values
        JsonAdaptedPerson adaptedExplicit = new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_TAGS, VALID_FEEDING_SESSION_IDS);
        assertEquals(VALID_ID, adaptedExplicit.getId());
    }

    // test the getFeedingSessionIds() method
    @Test
    public void getFeedingSessionIds_fromAdaptedPerson_containsAllSessionIds() {
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(BENSON);
        List<String> actual = adapted.getFeedingSessionIds();
        // Compare as sets to avoid order sensitivity
        Set<String> expectedSet = new HashSet<>(VALID_FEEDING_SESSION_IDS);
        Set<String> actualSet = new HashSet<>(actual);
        assertEquals(expectedSet, actualSet);
    }

    // test throwing IllegalValueException due to providing invalid id
    @Test
    public void toModelType_invalidId_throwsIllegalValueException() {
        String invalidId = "not-a-valid-uuid";
        JsonAdaptedPerson person = new JsonAdaptedPerson(invalidId, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_TAGS, VALID_FEEDING_SESSION_IDS);
        String expectedMessage = "Invalid UUID format for id";
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ID, INVALID_NAME, VALID_PHONE, VALID_EMAIL,
                        VALID_TAGS, VALID_FEEDING_SESSION_IDS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, null, VALID_PHONE, VALID_EMAIL,
                VALID_TAGS, VALID_FEEDING_SESSION_IDS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ID, VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_TAGS,
                        VALID_FEEDING_SESSION_IDS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, VALID_NAME, null, VALID_EMAIL, VALID_TAGS,
                VALID_FEEDING_SESSION_IDS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_TAGS,
                        VALID_FEEDING_SESSION_IDS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, null, VALID_TAGS,
                VALID_FEEDING_SESSION_IDS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, VALID_EMAIL, invalidTags,
                        VALID_FEEDING_SESSION_IDS);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

}
