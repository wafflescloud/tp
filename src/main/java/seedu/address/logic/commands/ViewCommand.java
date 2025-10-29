package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.TYPE_ANIMAL;
import static seedu.address.logic.parser.CliSyntax.TYPE_PERSON;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Type;
import seedu.address.model.Model;
import seedu.address.model.animal.Animal;
import seedu.address.model.person.Person;
import seedu.address.ui.AnimalProfileWindow;
import seedu.address.ui.PersonProfileWindow;

/**
 * Views a person or animal profile window.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Views a person or animal profile. "
            + "Parameters: TYPE " + PREFIX_NAME + "NAME\n"
            + "Example: " + COMMAND_WORD + " " + TYPE_PERSON + " " + PREFIX_NAME + "Alex Yo\n"
            + "Example: " + COMMAND_WORD + " " + TYPE_ANIMAL + " " + PREFIX_NAME + "Fluffy";

    public static final String MESSAGE_VIEW_PERSON_SUCCESS = "Opened profile for person: %1$s";
    public static final String MESSAGE_VIEW_ANIMAL_SUCCESS = "Opened profile for animal: %1$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Person with name '%1$s' not found.";
    public static final String MESSAGE_ANIMAL_NOT_FOUND = "Animal with name '%1$s' not found.";

    private final Type type;
    private final String name;

    /**
     * Creates a ViewCommand to view a person or animal profile.
     *
     * @param type The type of entity to view (TYPE_PERSON or TYPE_ANIMAL)
     * @param name The name of the person or animal
     */
    public ViewCommand(Type type, String name) {
        requireNonNull(type);
        requireNonNull(name);
        this.type = type;
        this.name = name;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (TYPE_PERSON.equals(type)) {
            return viewPerson(model);
        } else if (TYPE_ANIMAL.equals(type)) {
            return viewAnimal(model);
        } else {
            throw new CommandException("Invalid type. Use 'person' or 'animal'.");
        }
    }

    /**
     * Views a person profile.
     */
    private CommandResult viewPerson(Model model) throws CommandException {
        List<Person> personList = model.getFilteredPersonList();
        Person person = personList.stream()
                .filter(p -> p.getName().fullName.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, name)));

        List<seedu.address.model.feedingsession.FeedingSession> feedingSessions =
                new ArrayList<>(model.getFeedingSessionList());
        List<Animal> animals = new ArrayList<>(model.getFilteredAnimalList());

        PersonProfileWindow.openProfile(person, feedingSessions, animals);
        return new CommandResult(String.format(MESSAGE_VIEW_PERSON_SUCCESS, person.getName().fullName));
    }

    /**
     * Views an animal profile.
     */
    private CommandResult viewAnimal(Model model) throws CommandException {
        List<Animal> animalList = model.getFilteredAnimalList();
        Animal animal = animalList.stream()
                .filter(a -> a.getName().fullName.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_ANIMAL_NOT_FOUND, name)));

        AnimalProfileWindow.openProfile(animal);
        return new CommandResult(String.format(MESSAGE_VIEW_ANIMAL_SUCCESS, animal.getName().fullName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ViewCommand otherCommand)) {
            return false;
        }

        return type.equals(otherCommand.type) && name.equals(otherCommand.name);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("type", type)
                .add("name", name)
                .toString();
    }
}
