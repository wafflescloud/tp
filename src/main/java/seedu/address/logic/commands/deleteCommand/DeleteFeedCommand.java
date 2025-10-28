package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.animal.Animal;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonName;

/**
 * Deletes a feeding session identified by person name, animal name, and datetime.
 */
public class DeleteFeedCommand extends DeleteCommand {

    private final PersonName personName;
    private final String animalName;
    private final LocalDateTime feedingTime;

    /**
     * Creates a DeleteFeedCommand to delete the feeding session with the specified details.
     *
     * @param personName Name of the person involved in the feeding session
     * @param animalName Name of the animal involved in the feeding session
     * @param feedingTime Time when the feeding occurred
     */
    public DeleteFeedCommand(PersonName personName, String animalName, LocalDateTime feedingTime) {
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
                        String.format(Messages.MESSAGE_PERSON_NOT_FOUND_FOR_FEED, personName)));

        List<Animal> animalList = model.getFilteredAnimalList();
        Animal animal = animalList.stream()
                .filter(a -> a.getName().fullName.equals(animalName))
                .findFirst()
                .orElseThrow(() -> new CommandException(
                        String.format(Messages.MESSAGE_ANIMAL_NOT_FOUND_FOR_FEED, animalName)));

        FeedingSession sessionToDelete = model.getFeedingSessionList().stream()
                .filter(session -> session.getPersonId().equals(person.getId())
                        && session.getAnimalId().equals(animal.getId())
                        && session.getDateTime().equals(feedingTime))
                .findFirst()
                .orElseThrow(() -> new CommandException(Messages.MESSAGE_FEEDING_SESSION_NOT_FOUND));

        Person updatedPerson = person.removeFeedingSessionId(sessionToDelete.getId());
        Animal updatedAnimal = animal.removeFeedingSessionId(sessionToDelete.getId());

        model.deleteFeedingSessionWithUpdates(sessionToDelete, person, updatedPerson, animal, updatedAnimal);

        return new CommandResult(String.format(Messages.MESSAGE_DELETED_FEEDING_SESSION_SUCCESS,
                personName, animalName, feedingTime));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteFeedCommand)) {
            return false;
        }

        DeleteFeedCommand otherCommand = (DeleteFeedCommand) other;
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



