package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Contact;
import seedu.address.model.Model;
import seedu.address.model.animal.Animal;
import seedu.address.model.person.Person;

/**
 * Generic command for finding contacts (Person or Animal) in the address book.
 * Uses method references to handle type-specific behavior without requiring subclasses.
 */
public class FindContactCommand<T extends Contact> extends FindCommand {

    public static final String PERSON_MESSAGE_USAGE = COMMAND_WORD + " person: Finds persons by name substrings "
            + "and/or tags.\n Use n/ for name substring (any-match, case-insensitive). "
            + "Use t/ for tag (exact-match, case-insensitive).\n"
            + "At least one of n/ or t/ must be provided. Multiple flags allowed.\n"
            + "Examples:\n"
            + "- find person n/bob n/al\n"
            + "- find person t/happy\n"
            + "- find person n/bob t/angry t/happy";

    public static final String ANIMAL_MESSAGE_USAGE = COMMAND_WORD + " animal: Finds animals by name substrings "
            + " and/or tags.\n Use n/ for name substring (any-match, case-insensitive). "
            + "Use t/ for tag (exact-match, case-insensitive).\n"
            + "At least one of n/ or t/ must be provided. Multiple flags allowed.\n"
            + "Examples:\n"
            + "- find animal n/lu n/ma\n"
            + "- find animal t/happy\n"
            + "- find animal n/lu t/friendly t/small";

    private final Predicate<T> predicate;
    private final Function<Model, List<T>> listGetter;
    private final BiConsumer<Model, Predicate<T>> listUpdater;
    private final String successMessageFormat;

    /**
     * Creates a FindContactCommand with the specified predicate and model operations.
     */
    public FindContactCommand(Predicate<T> predicate,
                             Function<Model, List<T>> listGetter,
                             BiConsumer<Model, Predicate<T>> listUpdater,
                             String successMessageFormat) {
        this.predicate = predicate;
        this.listGetter = listGetter;
        this.listUpdater = listUpdater;
        this.successMessageFormat = successMessageFormat;
    }

    /**
     * Factory method to create a FindContactCommand for Person entities.
     */
    public static FindContactCommand<Person> forPerson(Predicate<Person> predicate) {
        return new FindContactCommand<>(
            predicate,
            Model::getFilteredPersonList,
            Model::updateFilteredPersonList,
            Messages.MESSAGE_FIND_PERSON_SUCCESS
        );
    }

    /**
     * Factory method to create a FindContactCommand for Animal entities.
     */
    public static FindContactCommand<Animal> forAnimal(Predicate<Animal> predicate) {
        return new FindContactCommand<>(
            predicate,
            Model::getFilteredAnimalList,
            Model::updateFilteredAnimalList,
            Messages.MESSAGE_FIND_ANIMAL_SUCCESS
        );
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        listUpdater.accept(model, predicate);
        int count = listGetter.apply(model).size();
        return new CommandResult(String.format(successMessageFormat, count));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other == null || !other.getClass().equals(this.getClass())) {
            return false;
        }

        @SuppressWarnings("unchecked")
        FindContactCommand<T> otherCommand = (FindContactCommand<T>) other;
        return predicate.equals(otherCommand.predicate)
                && listGetter.equals(otherCommand.listGetter)
                && listUpdater.equals(otherCommand.listUpdater)
                && successMessageFormat.equals(otherCommand.successMessageFormat);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .add("successMessageFormat", successMessageFormat)
                .toString();
    }
}
