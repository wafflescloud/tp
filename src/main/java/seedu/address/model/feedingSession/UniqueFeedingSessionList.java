package seedu.address.model.feedingsession;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

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
 * Supports a minimal set of list operations.
 */
public class UniqueFeedingSessionList implements Iterable<FeedingSession> {

    private final ObservableList<FeedingSession> internalList = FXCollections.observableArrayList();
    private final ObservableList<FeedingSession> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent feeding session as the given argument.
     */
    public boolean contains(FeedingSession toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameFeedingSession);
    }

    /**
     * Returns true if the list contains a feeding session with the given ID.
     */
    public boolean contains(UUID id) {
        requireNonNull(id);
        return internalList.stream().anyMatch(session -> session.getId().equals(id));
    }

    /**
     * Retrieves a feeding session by its ID.
     * Returns null if not found.
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
     * The feeding session identity of {@code editedSession} must not be the same as another existing session.
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
     */
    public void remove(FeedingSession toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new FeedingSessionNotFoundException();
        }
    }

    /**
     * Removes a feeding session by its ID.
     */
    public void removeById(UUID id) {
        requireNonNull(id);
        FeedingSession session = getById(id);
        if (session == null) {
            throw new FeedingSessionNotFoundException();
        }
        internalList.remove(session);
    }

    public void setFeedingSessions(UniqueFeedingSessionList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code feedingSessions}.
     * {@code feedingSessions} must not contain duplicate feeding sessions.
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
     */
    public ObservableList<FeedingSession> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<FeedingSession> iterator() {
        return internalList.iterator();
    }

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

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code feedingSessions} contains only unique feeding sessions.
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

