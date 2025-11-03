package seedu.address.model.feedingsession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class FeedingSessionTest {

    private static final UUID VALID_ANIMAL_ID = UUID.randomUUID();
    private static final UUID VALID_PERSON_ID = UUID.randomUUID();
    private static final LocalDateTime VALID_DATE_TIME = LocalDateTime.of(2024, 11, 4, 10, 30);
    private static final UUID VALID_SESSION_ID = UUID.randomUUID();

    @Test
    public void constructor_withoutId_success() {
        FeedingSession session = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME);
        assertNotNull(session.getId());
        assertEquals(VALID_ANIMAL_ID, session.getAnimalId());
        assertEquals(VALID_PERSON_ID, session.getPersonId());
        assertEquals(VALID_DATE_TIME, session.getDateTime());
    }

    @Test
    public void constructor_withId_success() {
        FeedingSession session = new FeedingSession(VALID_SESSION_ID, VALID_ANIMAL_ID,
                VALID_PERSON_ID, VALID_DATE_TIME);
        assertEquals(VALID_SESSION_ID, session.getId());
        assertEquals(VALID_ANIMAL_ID, session.getAnimalId());
        assertEquals(VALID_PERSON_ID, session.getPersonId());
        assertEquals(VALID_DATE_TIME, session.getDateTime());
    }

    @Test
    public void constructor_nullAnimalId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            new FeedingSession(null, VALID_PERSON_ID, VALID_DATE_TIME));
    }

    @Test
    public void constructor_nullPersonId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            new FeedingSession(VALID_ANIMAL_ID, null, VALID_DATE_TIME));
    }

    @Test
    public void constructor_nullDateTime_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, null));
    }

    @Test
    public void constructorWithId_nullId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            new FeedingSession(null, VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME));
    }

    @Test
    public void getFeedingTime_returnsDateTime() {
        FeedingSession session = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME);
        assertEquals(VALID_DATE_TIME, session.getFeedingTime());
        assertEquals(session.getDateTime(), session.getFeedingTime());
    }

    @Test
    public void isSameFeedingSession_sameObject_returnsTrue() {
        FeedingSession session = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME);
        assertTrue(session.isSameFeedingSession(session));
    }

    @Test
    public void isSameFeedingSession_null_returnsFalse() {
        FeedingSession session = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME);
        assertFalse(session.isSameFeedingSession(null));
    }

    @Test
    public void isSameFeedingSession_sameId_returnsTrue() {
        FeedingSession session1 = new FeedingSession(VALID_SESSION_ID, VALID_ANIMAL_ID,
                VALID_PERSON_ID, VALID_DATE_TIME);
        FeedingSession session2 = new FeedingSession(VALID_SESSION_ID, UUID.randomUUID(),
                UUID.randomUUID(), LocalDateTime.now());
        assertTrue(session1.isSameFeedingSession(session2));
    }

    @Test
    public void isSameFeedingSession_differentId_returnsFalse() {
        FeedingSession session1 = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME);
        FeedingSession session2 = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME);
        assertFalse(session1.isSameFeedingSession(session2)); // Different auto-generated IDs
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        FeedingSession session = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME);
        assertTrue(session.equals(session));
    }

    @Test
    public void equals_null_returnsFalse() {
        FeedingSession session = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME);
        assertFalse(session.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        FeedingSession session = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME);
        assertFalse(session.equals(5));
    }

    @Test
    public void equals_sameId_returnsTrue() {
        FeedingSession session1 = new FeedingSession(VALID_SESSION_ID, VALID_ANIMAL_ID,
                VALID_PERSON_ID, VALID_DATE_TIME);
        FeedingSession session2 = new FeedingSession(VALID_SESSION_ID, UUID.randomUUID(),
                UUID.randomUUID(), LocalDateTime.now());
        assertTrue(session1.equals(session2));
    }

    @Test
    public void equals_differentId_returnsFalse() {
        FeedingSession session1 = new FeedingSession(UUID.randomUUID(), VALID_ANIMAL_ID,
                VALID_PERSON_ID, VALID_DATE_TIME);
        FeedingSession session2 = new FeedingSession(UUID.randomUUID(), VALID_ANIMAL_ID,
                VALID_PERSON_ID, VALID_DATE_TIME);
        assertFalse(session1.equals(session2));
    }

    @Test
    public void hashCode_sameId_returnsSameHashCode() {
        FeedingSession session1 = new FeedingSession(VALID_SESSION_ID, VALID_ANIMAL_ID,
                VALID_PERSON_ID, VALID_DATE_TIME);
        FeedingSession session2 = new FeedingSession(VALID_SESSION_ID, UUID.randomUUID(),
                UUID.randomUUID(), LocalDateTime.now());
        assertEquals(session1.hashCode(), session2.hashCode());
    }

    @Test
    public void involvesPerson_samePerson_returnsTrue() {
        FeedingSession session = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME);
        assertTrue(session.involvesPerson(VALID_PERSON_ID));
    }

    @Test
    public void involvesPerson_differentPerson_returnsFalse() {
        FeedingSession session = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME);
        assertFalse(session.involvesPerson(UUID.randomUUID()));
    }

    @Test
    public void involvesAnimal_sameAnimal_returnsTrue() {
        FeedingSession session = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME);
        assertTrue(session.involvesAnimal(VALID_ANIMAL_ID));
    }

    @Test
    public void involvesAnimal_differentAnimal_returnsFalse() {
        FeedingSession session = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME);
        assertFalse(session.involvesAnimal(UUID.randomUUID()));
    }

    @Test
    public void isEarlierThan_earlierDateTime_returnsTrue() {
        LocalDateTime earlier = LocalDateTime.of(2024, 11, 4, 9, 0);
        LocalDateTime later = LocalDateTime.of(2024, 11, 4, 10, 0);
        FeedingSession earlierSession = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, earlier);
        FeedingSession laterSession = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, later);
        assertTrue(earlierSession.isEarlierThan(laterSession));
    }

    @Test
    public void isEarlierThan_laterDateTime_returnsFalse() {
        LocalDateTime earlier = LocalDateTime.of(2024, 11, 4, 9, 0);
        LocalDateTime later = LocalDateTime.of(2024, 11, 4, 10, 0);
        FeedingSession earlierSession = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, earlier);
        FeedingSession laterSession = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, later);
        assertFalse(laterSession.isEarlierThan(earlierSession));
    }

    @Test
    public void isEarlierThan_sameDateTime_returnsFalse() {
        FeedingSession session1 = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME);
        FeedingSession session2 = new FeedingSession(VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME);
        assertFalse(session1.isEarlierThan(session2));
    }

    @Test
    public void toStringMethod() {
        FeedingSession session = new FeedingSession(VALID_SESSION_ID, VALID_ANIMAL_ID,
                VALID_PERSON_ID, VALID_DATE_TIME);
        String expected = String.format("FeedingSession[id=%s, animalId=%s, personId=%s, dateTime=%s]",
                VALID_SESSION_ID, VALID_ANIMAL_ID, VALID_PERSON_ID, VALID_DATE_TIME);
        assertEquals(expected, session.toString());
    }
}
