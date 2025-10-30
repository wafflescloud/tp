package seedu.address.logic.commands;

import java.util.function.Predicate;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords
 * and/or whose tags match all of the provided tag keywords (case-insensitive exact match).
 */
public class FindPersonCommand extends FindContactCommand<Person> {

    public static final String MESSAGE_USAGE = COMMAND_WORD + " person: Finds persons by name substrings "
            + "and/or tags.\n Use n/ for name substring (any-match, case-insensitive). "
            + "Use t/ for tag (exact-match, case-insensitive).\n"
            + "At least one of n/ or t/ must be provided. Multiple flags allowed.\n"
            + "Examples:\n"
            + "- find person n/bob n/al\n"
            + "- find person t/happy\n"
            + "- find person n/bob t/angry t/happy";

    public FindPersonCommand(Predicate<Person> predicate) {
        super(predicate);
    }

    @Override
    protected void updateFilteredList(Model model) {
        model.updateFilteredPersonList(predicate);
    }

    @Override
    protected int getFilteredListSize(Model model) {
        return model.getFilteredPersonList().size();
    }

    @Override
    protected String getSuccessMessage(int count) {
        return String.format(Messages.MESSAGE_FIND_PERSON_SUCCESS, count);
    }
}
