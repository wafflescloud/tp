package seedu.address.logic.commands;

import seedu.address.model.Model;

public class FeedCommand extends Command {

    public static final String COMMAND_WORD = "feed";

    public static final String MESSAGE_SUCCESS = "Feeding recorded successfully!";
    public static final String MESSAGE_FAILURE = "Failed to record feeding.";

    public CommandResult execute(Model model) {
        // Implementation for recording feeding goes here
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
