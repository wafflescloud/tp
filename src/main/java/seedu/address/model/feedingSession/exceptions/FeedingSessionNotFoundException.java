package seedu.address.model.feedingsession.exceptions;

/**
 * Signals that the operation is unable to find the specified feeding session.
 */
public class FeedingSessionNotFoundException extends RuntimeException {
    public FeedingSessionNotFoundException() {
        super("Feeding session not found");
    }
}
