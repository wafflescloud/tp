package seedu.address.logic.parser;

import seedu.address.logic.commands.FeedCommand;

public class FeedCommandParser implements Parser<FeedCommand> {

    @Override
    public FeedCommand parse(String args) {
        // Parsing logic for FeedCommand can be added here
        return new FeedCommand();
    }

}
