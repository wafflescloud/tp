package seedu.address.logic.commands.deleteCommand;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.model.Model;

public class DeleteFeedCommand extends DeleteCommand {
    @Override
    public CommandResult execute(Model model) {
        return new CommandResult("Feeding session deleted");
    }
}
