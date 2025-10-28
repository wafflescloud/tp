package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATETIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FEEDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.animal.Animal;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonName;

/**
 * Records a feeding session for an animal by a person.
 */
public class FeedCommand extends Command {

    public static final String COMMAND_WORD = "feed";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Records a feeding session for an animal. "
            + "Parameters: "
            + PREFIX_FEEDER + "PERSON_NAME "
            + PREFIX_NAME + "ANIMAL_NAME "
            + PREFIX_DATETIME + "DATETIME\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Max "
            + PREFIX_FEEDER + "James Tan "
            + PREFIX_DATETIME + "2025-12-25 09:00";

    public static final String MESSAGE_SUCCESS = """
            Feeding session recorded successfully!
            Person: %1$s
            Animal: %2$s
            Time: %3$s""";
    public static final String MESSAGE_PERSON_NOT_FOUND = "The person '%1$s' is not found in the address book";
    public static final String MESSAGE_ANIMAL_NOT_FOUND = "The animal '%1$s' is not found in the address book";

    private final PersonName personName;
    private final String animalName;
    private final LocalDateTime feedingTime;

    /**
     * Creates a FeedCommand to record a feeding session.
     *
     * @param personName Name of the person feeding the animal
     * @param animalName Name of the animal being fed
     * @param feedingTime Time when the feeding occurs
     */
    public FeedCommand(PersonName personName, String animalName, LocalDateTime feedingTime) {
        requireNonNull(personName);
        requireNonNull(animalName);
        requireNonNull(feedingTime);

        this.personName = personName;
        this.animalName = animalName;
        this.feedingTime = feedingTime;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> personList = model.getFilteredPersonList();
        Person person = personList.stream()
                .filter(p -> p.getName().equals(personName))
                .findFirst()
                .orElseThrow(() -> new CommandException(
                        String.format(MESSAGE_PERSON_NOT_FOUND, personName)));

        List<Animal> animalList = model.getFilteredAnimalList();
        Animal animal = animalList.stream()
                .filter(a -> a.getName().fullName.equals(animalName))
                .findFirst()
                .orElseThrow(() -> new CommandException(
                        String.format(MESSAGE_ANIMAL_NOT_FOUND, animalName)));

        FeedingSession newFeedingSession = new FeedingSession(animal.getId(), person.getId(), feedingTime, "");

        model.addFeedingSession(newFeedingSession);

        Person updatedPerson = person.addFeedingSessionId(newFeedingSession.getId());

        Animal updatedAnimal = animal.addFeedingSessionId(newFeedingSession.getId());

        model.setPerson(person, updatedPerson);
        model.setAnimal(animal, updatedAnimal);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy 'at' HH:mm");
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                personName,
                animalName,
                feedingTime.format(formatter)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FeedCommand otherCommand)) {
            return false;
        }

        return personName.equals(otherCommand.personName)
                && animalName.equals(otherCommand.animalName)
                && feedingTime.equals(otherCommand.feedingTime);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personName", personName)
                .add("animalName", animalName)
                .add("feedingTime", feedingTime)
                .toString();
    }
}
