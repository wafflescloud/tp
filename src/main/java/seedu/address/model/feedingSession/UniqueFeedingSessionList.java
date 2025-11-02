package seedu.address.model.feedingsession;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.feedingsession.exceptions.DuplicateFeedingSessionException;
import seedu.address.model.feedingsession.exceptions.FeedingSessionNotFoundException;

/**
 * A list of feeding sessions that enforces uniqueness between its elements and does not allow nulls.
 * A feeding session is considered unique by comparing using
 * {@code FeedingSession#isSameFeedingSession(FeedingSession)}.
 * As such, adding and updating of feeding sessions uses FeedingSession#isSameFeedingSession(FeedingSession)
 * for equality to ensure that the feeding session being added or updated is unique in terms of identity
 * in the UniqueFeedingSessionList. However, the removal of a feeding session uses FeedingSession#equals(Object)
 * to ensure that the feeding session with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see FeedingSession#isSameFeedingSession(FeedingSession)
 */
public class UniqueFeedingSessionList implements Iterable<FeedingSession> {

    private final ObservableList<FeedingSession> internalList = FXCollections.observableArrayList();
    private final ObservableList<FeedingSession> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent feeding session as the given argument.
     *
     * @param toCheck The feeding session to check for.
     * @return True if an equivalent feeding session exists in the list.
     */
    public boolean contains(FeedingSession toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameFeedingSession);
    }

    /**
     * Returns true if the list contains a feeding session with the given ID.
     *
     * @param id The UUID of the feeding session to check for.
     * @return True if a feeding session with the given ID exists in the list.
     */
    public boolean contains(UUID id) {
        requireNonNull(id);
        return internalList.stream().anyMatch(session -> session.getId().equals(id));
    }

    /**
     * Returns true if the list contains a feeding session with the same animal, person, and datetime.
     *
     * @param animalId The UUID of the animal.
     * @param personId The UUID of the person.
     * @param dateTime The date and time of the feeding.
     * @return True if a feeding session with the same details exists in the list.
     */
    public boolean containsByDetails(UUID animalId, UUID personId, LocalDateTime dateTime) {
        requireAllNonNull(animalId, personId, dateTime);
        return internalList.stream().anyMatch(session ->
                session.getAnimalId().equals(animalId)
                        && session.getPersonId().equals(personId)
                        && session.getDateTime().equals(dateTime));
    }

    /**
     * Retrieves a feeding session by its ID.
     *
     * @param id The UUID of the feeding session to retrieve.
     * @return The feeding session with the given ID, or null if not found.
     */
    public FeedingSession getById(UUID id) {
        requireNonNull(id);
        return internalList.stream()
                .filter(session -> session.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a feeding session to the list.
     * The feeding session must not already exist in the list.
     *
     * @param toAdd The feeding session to add.
     * @throws DuplicateFeedingSessionException If an equivalent feeding session already exists in the list.
     */
    public void add(FeedingSession toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateFeedingSessionException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the feeding session {@code target} in the list with {@code editedSession}.
     * {@code target} must exist in the list.
     * The feeding session identity of {@code editedSession} must not be the same as another existing
     * feeding session in the list.
     *
     * @param target The feeding session to be replaced.
     * @param editedSession The feeding session to replace with.
     * @throws FeedingSessionNotFoundException If {@code target} does not exist in the list.
     * @throws DuplicateFeedingSessionException If {@code editedSession} is a duplicate of another feeding session
     *                                          in the list.
     */
    public void setFeedingSession(FeedingSession target, FeedingSession editedSession) {
        requireAllNonNull(target, editedSession);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new FeedingSessionNotFoundException();
        }

        if (!target.isSameFeedingSession(editedSession) && contains(editedSession)) {
            throw new DuplicateFeedingSessionException();
        }

        internalList.set(index, editedSession);
    }

    /**
     * Removes the equivalent feeding session from the list.
     * The feeding session must exist in the list.
     *
     * @param toRemove The feeding session to remove.
     * @throws FeedingSessionNotFoundException If the feeding session does not exist in the list.
     */
    public void remove(FeedingSession toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new FeedingSessionNotFoundException();
        }
    }

    /**
     * Removes a feeding session by its ID.
     *
     * @param id The UUID of the feeding session to remove.
     * @throws FeedingSessionNotFoundException If no feeding session with the given ID exists in the list.
     */
    public void removeById(UUID id) {
        requireNonNull(id);
        FeedingSession session = getById(id);
        if (session == null) {
            throw new FeedingSessionNotFoundException();
        }
        internalList.remove(session);
    }

    /**
     * Replaces the contents of this list with the contents of {@code replacement}.
     *
     * @param replacement The UniqueFeedingSessionList to replace with.
     */
    public void setFeedingSessions(UniqueFeedingSessionList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code feedingSessions}.
     * {@code feedingSessions} must not contain duplicate feeding sessions.
     *
     * @param feedingSessions The list of feeding sessions to set.
     * @throws DuplicateFeedingSessionException If {@code feedingSessions} contains duplicate feeding sessions.
     */
    public void setFeedingSessions(List<FeedingSession> feedingSessions) {
        requireAllNonNull(feedingSessions);
        if (!feedingSessionsAreUnique(feedingSessions)) {
            throw new DuplicateFeedingSessionException();
        }

        internalList.setAll(feedingSessions);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     *
     * @return An unmodifiable view of the internal list.
     */
    public ObservableList<FeedingSession> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    /**
     * Returns an iterator over the feeding sessions in this list.
     *
     * @return An iterator for the feeding sessions.
     */
    @Override
    public Iterator<FeedingSession> iterator() {
        return internalList.iterator();
    }

    /**
     * Returns true if both lists contain the same feeding sessions.
     * This defines a stronger notion of equality between two UniqueFeedingSessionLists.
     *
     * @param other The other object to compare with.
     * @return True if both objects are UniqueFeedingSessionLists with the same feeding sessions.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UniqueFeedingSessionList)) {
            return false;
        }

        UniqueFeedingSessionList otherList = (UniqueFeedingSessionList) other;
        return internalList.equals(otherList.internalList);
    }

    /**
     * Returns the hash code of this list.
     *
     * @return The hash code based on the internal list.
     */
    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns a string representation of this list.
     *
     * @return A string containing all feeding sessions in the list.
     */
    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code feedingSessions} contains only unique feeding sessions.
     *
     * @param feedingSessions The list of feeding sessions to check.
     * @return True if all feeding sessions are unique based on their identity.
     */
    private boolean feedingSessionsAreUnique(List<FeedingSession> feedingSessions) {
        for (int i = 0; i < feedingSessions.size() - 1; i++) {
            for (int j = i + 1; j < feedingSessions.size(); j++) {
                if (feedingSessions.get(i).isSameFeedingSession(feedingSessions.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
