package seedu.address.logic.parser;

import seedu.address.logic.commands.deleteCommand.DeleteFeedCommand;

public class DeleteFeedCommandParser implements Parser<DeleteFeedCommand> {
        @Override
        public DeleteFeedCommand parse(String args) {
            return new DeleteFeedCommand();
        }
}
