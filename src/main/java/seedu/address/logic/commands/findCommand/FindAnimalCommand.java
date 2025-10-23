package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.animal.Animal;

/**
 * Finds and lists all animals in address book whose name contains any of the argument keywords
 * and/or whose tags match all of the provided tag keywords (case-insensitive exact match).
 */
public class FindAnimalCommand extends FindCommand {

    public static final String MESSAGE_USAGE = COMMAND_WORD + " animal: Finds animals by name substrings "
            + " and/or tags.\n Use n/ for name substring (any-match, case-insensitive). "
            + "Use t/ for tag (exact-match, case-insensitive).\n"
            + "At least one of n/ or t/ must be provided. Multiple flags allowed.\n"
            + "Examples:\n"
            + "- find animal n/lu n/ma\n"
            + "- find animal t/happy\n"
            + "- find animal n/lu t/friendly t/small";

    private final Predicate<Animal> predicate;

    public FindAnimalCommand(Predicate<Animal> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredAnimalList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_FIND_ANIMAL_SUCCESS, model.getFilteredAnimalList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindAnimalCommand)) {
            return false;
        }

        FindAnimalCommand otherFindCommand = (FindAnimalCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
