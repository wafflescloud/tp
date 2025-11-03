package seedu.address.model.feedingsession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.model.feedingsession.exceptions.DuplicateFeedingSessionException;
import seedu.address.model.feedingsession.exceptions.FeedingSessionNotFoundException;

public class UniqueFeedingSessionListTest {

    private static final UUID ANIMAL_ID_1 = UUID.randomUUID();
    private static final UUID ANIMAL_ID_2 = UUID.randomUUID();
    private static final UUID PERSON_ID_1 = UUID.randomUUID();
    private static final UUID PERSON_ID_2 = UUID.randomUUID();
    private static final LocalDateTime DATE_TIME_1 = LocalDateTime.of(2024, 11, 4, 10, 0);
    private static final LocalDateTime DATE_TIME_2 = LocalDateTime.of(2024, 11, 4, 11, 0);
    private static final UUID SESSION_ID = UUID.randomUUID();

    private final UniqueFeedingSessionList uniqueFeedingSessionList = new UniqueFeedingSessionList();

    @Test
    public void contains_nullFeedingSession_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueFeedingSessionList.contains((FeedingSession) null));
    }

    @Test
    public void contains_nullUuid_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueFeedingSessionList.contains((UUID) null));
    }

    @Test
    public void contains_feedingSessionNotInList_returnsFalse() {
        FeedingSession session = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        assertFalse(uniqueFeedingSessionList.contains(session));
    }

    @Test
    public void contains_feedingSessionInList_returnsTrue() {
        FeedingSession session = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        uniqueFeedingSessionList.add(session);
        assertTrue(uniqueFeedingSessionList.contains(session));
    }

    @Test
    public void contains_feedingSessionWithSameId_returnsTrue() {
        FeedingSession session1 = new FeedingSession(SESSION_ID, ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        FeedingSession session2 = new FeedingSession(SESSION_ID, ANIMAL_ID_2, PERSON_ID_2, DATE_TIME_2);
        uniqueFeedingSessionList.add(session1);
        assertTrue(uniqueFeedingSessionList.contains(session2));
    }

    @Test
    public void contains_byUuid_feedingSessionInList_returnsTrue() {
        FeedingSession session = new FeedingSession(SESSION_ID, ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        uniqueFeedingSessionList.add(session);
        assertTrue(uniqueFeedingSessionList.contains(SESSION_ID));
    }

    @Test
    public void contains_byUuid_feedingSessionNotInList_returnsFalse() {
        assertFalse(uniqueFeedingSessionList.contains(UUID.randomUUID()));
    }

    @Test
    public void containsByDetails_nullParameters_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            uniqueFeedingSessionList.containsByDetails(null, PERSON_ID_1, DATE_TIME_1));
        assertThrows(NullPointerException.class, () ->
            uniqueFeedingSessionList.containsByDetails(ANIMAL_ID_1, null, DATE_TIME_1));
        assertThrows(NullPointerException.class, () ->
            uniqueFeedingSessionList.containsByDetails(ANIMAL_ID_1, PERSON_ID_1, null));
    }

    @Test
    public void containsByDetails_matchingSession_returnsTrue() {
        FeedingSession session = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        uniqueFeedingSessionList.add(session);
        assertTrue(uniqueFeedingSessionList.containsByDetails(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1));
    }

    @Test
    public void containsByDetails_noMatchingSession_returnsFalse() {
        FeedingSession session = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        uniqueFeedingSessionList.add(session);
        assertFalse(uniqueFeedingSessionList.containsByDetails(ANIMAL_ID_2, PERSON_ID_1, DATE_TIME_1));
        assertFalse(uniqueFeedingSessionList.containsByDetails(ANIMAL_ID_1, PERSON_ID_2, DATE_TIME_1));
        assertFalse(uniqueFeedingSessionList.containsByDetails(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_2));
    }

    @Test
    public void getById_nullUuid_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueFeedingSessionList.getById(null));
    }

    @Test
    public void getById_sessionExists_returnsSession() {
        FeedingSession session = new FeedingSession(SESSION_ID, ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        uniqueFeedingSessionList.add(session);
        assertEquals(session, uniqueFeedingSessionList.getById(SESSION_ID));
    }

    @Test
    public void getById_sessionDoesNotExist_returnsNull() {
        assertNull(uniqueFeedingSessionList.getById(UUID.randomUUID()));
    }

    @Test
    public void add_nullFeedingSession_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueFeedingSessionList.add(null));
    }

    @Test
    public void add_duplicateFeedingSession_throwsDuplicateFeedingSessionException() {
        FeedingSession session = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        uniqueFeedingSessionList.add(session);
        assertThrows(DuplicateFeedingSessionException.class, () -> uniqueFeedingSessionList.add(session));
    }

    @Test
    public void add_feedingSessionWithSameId_throwsDuplicateFeedingSessionException() {
        FeedingSession session1 = new FeedingSession(SESSION_ID, ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        FeedingSession session2 = new FeedingSession(SESSION_ID, ANIMAL_ID_2, PERSON_ID_2, DATE_TIME_2);
        uniqueFeedingSessionList.add(session1);
        assertThrows(DuplicateFeedingSessionException.class, () -> uniqueFeedingSessionList.add(session2));
    }

    @Test
    public void setFeedingSession_nullTargetFeedingSession_throwsNullPointerException() {
        FeedingSession session = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        assertThrows(NullPointerException.class, () -> uniqueFeedingSessionList.setFeedingSession(null, session));
    }

    @Test
    public void setFeedingSession_nullEditedFeedingSession_throwsNullPointerException() {
        FeedingSession session = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        assertThrows(NullPointerException.class, () -> uniqueFeedingSessionList.setFeedingSession(session, null));
    }

    @Test
    public void setFeedingSession_targetFeedingSessionNotInList_throwsFeedingSessionNotFoundException() {
        FeedingSession session1 = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        FeedingSession session2 = new FeedingSession(ANIMAL_ID_2, PERSON_ID_2, DATE_TIME_2);
        assertThrows(FeedingSessionNotFoundException.class, () ->
            uniqueFeedingSessionList.setFeedingSession(session1, session2));
    }

    @Test
    public void setFeedingSession_editedFeedingSessionIsSameFeedingSession_success() {
        FeedingSession session = new FeedingSession(SESSION_ID, ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        uniqueFeedingSessionList.add(session);
        uniqueFeedingSessionList.setFeedingSession(session, session);
        UniqueFeedingSessionList expectedList = new UniqueFeedingSessionList();
        expectedList.add(session);
        assertEquals(expectedList, uniqueFeedingSessionList);
    }

    @Test
    public void setFeedingSession_editedFeedingSessionHasSameIdentity_success() {
        FeedingSession session1 = new FeedingSession(SESSION_ID, ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        FeedingSession session2 = new FeedingSession(SESSION_ID, ANIMAL_ID_2, PERSON_ID_2, DATE_TIME_2);
        uniqueFeedingSessionList.add(session1);
        uniqueFeedingSessionList.setFeedingSession(session1, session2);
        UniqueFeedingSessionList expectedList = new UniqueFeedingSessionList();
        expectedList.add(session2);
        assertEquals(expectedList, uniqueFeedingSessionList);
    }

    @Test
    public void setFeedingSession_editedFeedingSessionHasDifferentIdentity_success() {
        FeedingSession session1 = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        FeedingSession session2 = new FeedingSession(ANIMAL_ID_2, PERSON_ID_2, DATE_TIME_2);
        uniqueFeedingSessionList.add(session1);
        uniqueFeedingSessionList.setFeedingSession(session1, session2);
        UniqueFeedingSessionList expectedList = new UniqueFeedingSessionList();
        expectedList.add(session2);
        assertEquals(expectedList, uniqueFeedingSessionList);
    }

    @Test
    public void setFeedingSession_editedFeedingSessionHasNonUniqueIdentity_throwsDuplicateFeedingSessionException() {
        FeedingSession session1 = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        FeedingSession session2 = new FeedingSession(ANIMAL_ID_2, PERSON_ID_2, DATE_TIME_2);
        uniqueFeedingSessionList.add(session1);
        uniqueFeedingSessionList.add(session2);
        assertThrows(DuplicateFeedingSessionException.class, () ->
            uniqueFeedingSessionList.setFeedingSession(session1, session2));
    }

    @Test
    public void remove_nullFeedingSession_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueFeedingSessionList.remove(null));
    }

    @Test
    public void remove_feedingSessionDoesNotExist_throwsFeedingSessionNotFoundException() {
        FeedingSession session = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        assertThrows(FeedingSessionNotFoundException.class, () -> uniqueFeedingSessionList.remove(session));
    }

    @Test
    public void remove_existingFeedingSession_removesFeedingSession() {
        FeedingSession session = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        uniqueFeedingSessionList.add(session);
        uniqueFeedingSessionList.remove(session);
        UniqueFeedingSessionList expectedList = new UniqueFeedingSessionList();
        assertEquals(expectedList, uniqueFeedingSessionList);
    }

    @Test
    public void removeById_nullUuid_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueFeedingSessionList.removeById(null));
    }

    @Test
    public void removeById_feedingSessionDoesNotExist_throwsFeedingSessionNotFoundException() {
        assertThrows(FeedingSessionNotFoundException.class, () ->
            uniqueFeedingSessionList.removeById(UUID.randomUUID()));
    }

    @Test
    public void removeById_existingFeedingSession_removesFeedingSession() {
        FeedingSession session = new FeedingSession(SESSION_ID, ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        uniqueFeedingSessionList.add(session);
        uniqueFeedingSessionList.removeById(SESSION_ID);
        UniqueFeedingSessionList expectedList = new UniqueFeedingSessionList();
        assertEquals(expectedList, uniqueFeedingSessionList);
    }

    @Test
    public void setFeedingSessions_nullUniqueFeedingSessionList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            uniqueFeedingSessionList.setFeedingSessions((UniqueFeedingSessionList) null));
    }

    @Test
    public void setFeedingSessions_uniqueFeedingSessionList_replacesOwnListWithProvidedList() {
        FeedingSession session = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        uniqueFeedingSessionList.add(session);
        UniqueFeedingSessionList expectedList = new UniqueFeedingSessionList();
        FeedingSession newSession = new FeedingSession(ANIMAL_ID_2, PERSON_ID_2, DATE_TIME_2);
        expectedList.add(newSession);
        uniqueFeedingSessionList.setFeedingSessions(expectedList);
        assertEquals(expectedList, uniqueFeedingSessionList);
    }

    @Test
    public void setFeedingSessions_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            uniqueFeedingSessionList.setFeedingSessions((List<FeedingSession>) null));
    }

    @Test
    public void setFeedingSessions_list_replacesOwnListWithProvidedList() {
        FeedingSession session1 = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        uniqueFeedingSessionList.add(session1);
        FeedingSession session2 = new FeedingSession(ANIMAL_ID_2, PERSON_ID_2, DATE_TIME_2);
        List<FeedingSession> sessionList = Collections.singletonList(session2);
        uniqueFeedingSessionList.setFeedingSessions(sessionList);
        UniqueFeedingSessionList expectedList = new UniqueFeedingSessionList();
        expectedList.add(session2);
        assertEquals(expectedList, uniqueFeedingSessionList);
    }

    @Test
    public void setFeedingSessions_listWithDuplicateFeedingSessions_throwsDuplicateFeedingSessionException() {
        FeedingSession session = new FeedingSession(SESSION_ID, ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        FeedingSession duplicateSession = new FeedingSession(SESSION_ID, ANIMAL_ID_2, PERSON_ID_2, DATE_TIME_2);
        List<FeedingSession> listWithDuplicates = Arrays.asList(session, duplicateSession);
        assertThrows(DuplicateFeedingSessionException.class, () ->
            uniqueFeedingSessionList.setFeedingSessions(listWithDuplicates));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () ->
            uniqueFeedingSessionList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        assertTrue(uniqueFeedingSessionList.equals(uniqueFeedingSessionList));
    }

    @Test
    public void equals_null_returnsFalse() {
        assertFalse(uniqueFeedingSessionList.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        assertFalse(uniqueFeedingSessionList.equals(5));
    }

    @Test
    public void equals_sameContent_returnsTrue() {
        FeedingSession session = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        uniqueFeedingSessionList.add(session);
        UniqueFeedingSessionList otherList = new UniqueFeedingSessionList();
        otherList.add(session);
        assertTrue(uniqueFeedingSessionList.equals(otherList));
    }

    @Test
    public void equals_differentContent_returnsFalse() {
        FeedingSession session1 = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        FeedingSession session2 = new FeedingSession(ANIMAL_ID_2, PERSON_ID_2, DATE_TIME_2);
        uniqueFeedingSessionList.add(session1);
        UniqueFeedingSessionList otherList = new UniqueFeedingSessionList();
        otherList.add(session2);
        assertFalse(uniqueFeedingSessionList.equals(otherList));
    }

    @Test
    public void hashCode_sameContent_returnsSameHashCode() {
        FeedingSession session = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        uniqueFeedingSessionList.add(session);
        UniqueFeedingSessionList otherList = new UniqueFeedingSessionList();
        otherList.add(session);
        assertEquals(uniqueFeedingSessionList.hashCode(), otherList.hashCode());
    }

    @Test
    public void toStringMethod() {
        FeedingSession session = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        uniqueFeedingSessionList.add(session);
        assertEquals(uniqueFeedingSessionList.asUnmodifiableObservableList().toString(),
                uniqueFeedingSessionList.toString());
    }

    @Test
    public void iterator_canIterateThroughList() {
        FeedingSession session1 = new FeedingSession(ANIMAL_ID_1, PERSON_ID_1, DATE_TIME_1);
        FeedingSession session2 = new FeedingSession(ANIMAL_ID_2, PERSON_ID_2, DATE_TIME_2);
        uniqueFeedingSessionList.add(session1);
        uniqueFeedingSessionList.add(session2);

        int count = 0;
        for (FeedingSession session : uniqueFeedingSessionList) {
            count++;
        }
        assertEquals(2, count);
    }
}
