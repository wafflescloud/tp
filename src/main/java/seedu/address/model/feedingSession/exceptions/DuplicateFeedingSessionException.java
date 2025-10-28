package seedu.address.model.feedingsession.exceptions;

/**
 * Signals that the operation will result in duplicate FeedingSessions.
 * FeedingSessions are considered duplicates if they have the same identity.
 */
public class DuplicateFeedingSessionException extends RuntimeException {
    public DuplicateFeedingSessionException() {
        super("Operation would result in duplicate feeding sessions");
    }
}

