package seedu.address.logic.commands;

import java.util.function.Predicate;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.animal.Animal;

/**
 * Finds and lists all animals in address book whose name contains any of the argument keywords
 * and/or whose tags match all of the provided tag keywords (case-insensitive exact match).
 */
public class FindAnimalCommand extends FindContactCommand<Animal> {

    public static final String MESSAGE_USAGE = COMMAND_WORD + " animal: Finds animals by name substrings "
            + " and/or tags.\n Use n/ for name substring (any-match, case-insensitive). "
            + "Use t/ for tag (exact-match, case-insensitive).\n"
            + "At least one of n/ or t/ must be provided. Multiple flags allowed.\n"
            + "Examples:\n"
            + "- find animal n/lu n/ma\n"
            + "- find animal t/happy\n"
            + "- find animal n/lu t/friendly t/small";

    public FindAnimalCommand(Predicate<Animal> predicate) {
        super(predicate);
    }

    @Override
    protected void updateFilteredList(Model model) {
        model.updateFilteredAnimalList(predicate);
    }

    @Override
    protected int getFilteredListSize(Model model) {
        return model.getFilteredAnimalList().size();
    }

    @Override
    protected String getSuccessMessage(int count) {
        return String.format(Messages.MESSAGE_FIND_ANIMAL_SUCCESS, count);
    }
}
