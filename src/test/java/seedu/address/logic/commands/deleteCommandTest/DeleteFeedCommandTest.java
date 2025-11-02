package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalFeedingSessions.getTypicalAddressBook;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.Name;
import seedu.address.model.UserPrefs;
import seedu.address.model.animal.Animal;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalAnimals;
import seedu.address.testutil.TypicalPersons;

public class DeleteFeedCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    // Delete a feeding session successfully (Person, animal and date time exists as a feeding session currently)
    @Test
    public void execute_validPersonAnimalDateTime_success() throws Exception {
        Person person = TypicalPersons.ALICE;
        Animal animal = TypicalAnimals.MAX;
        Name personName = person.getName();
        Name animalName = animal.getName();
        LocalDateTime feedingTime = LocalDateTime.of(2024, 1, 1, 8, 0); // FS1

        assertTrue(model.hasFeedingSessionByDetails(animal.getId(), person.getId(), feedingTime));

        DeleteFeedCommand command = new DeleteFeedCommand(personName, animalName, feedingTime);
        CommandResult result = command.execute(model);

        String expectedMessage = String.format(Messages.MESSAGE_DELETED_FEEDING_SESSION_SUCCESS,
                personName, animalName, feedingTime);
        assertEquals(expectedMessage, result.getFeedbackToUser());

        assertFalse(model.hasFeedingSessionByDetails(animal.getId(), person.getId(), feedingTime));
        assertTrue(model.getFeedingSessionList().stream().noneMatch(s ->
                s.getAnimalId().equals(animal.getId())
                        && s.getPersonId().equals(person.getId())
                        && s.getDateTime().equals(feedingTime)));
    }

    // Fail to delete a feeding session (Person does not exist in the list)
    @Test
    public void execute_nonExistentPerson_throwsCommandException() {
        Name missingPerson = new Name("Non Existent Person");
        Name existingAnimal = TypicalAnimals.MAX.getName();
        LocalDateTime feedingTime = LocalDateTime.of(2024, 1, 1, 8, 0);

        DeleteFeedCommand command = new DeleteFeedCommand(missingPerson, existingAnimal, feedingTime);
        CommandException ex = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(Messages.MESSAGE_PERSON_NOT_FOUND_FOR_FEED, missingPerson), ex.getMessage());
    }

    // Fail to delete a feeding session (Animal does not exist in the list)
    @Test
    public void execute_nonExistentAnimal_throwsCommandException() {
        Name existingPerson = TypicalPersons.ALICE.getName();
        Name missingAnimal = new Name("Not A Real Animal");
        LocalDateTime feedingTime = LocalDateTime.of(2024, 1, 1, 8, 0);

        DeleteFeedCommand command = new DeleteFeedCommand(existingPerson, missingAnimal, feedingTime);
        CommandException ex = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(Messages.MESSAGE_ANIMAL_NOT_FOUND_FOR_FEED, missingAnimal), ex.getMessage());
    }

    // Fail to delete a feeding session (Date time does not exist)
    @Test
    public void execute_nonExistentDateTime_throwsCommandException() {
        Name personName = TypicalPersons.ALICE.getName();
        Name animalName = TypicalAnimals.MAX.getName();
        LocalDateTime missingTime = LocalDateTime.of(2025, 1, 1, 8, 0); // not in typical sessions

        DeleteFeedCommand command = new DeleteFeedCommand(personName, animalName, missingTime);
        CommandException ex = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(Messages.MESSAGE_FEEDING_SESSION_NOT_FOUND, ex.getMessage());
    }

    // Fail to delete a feeding session when person and time match an existing session, but animal does not
    @Test
    public void execute_samePersonAndTimeDifferentAnimal_throwsCommandException() {
        // FS1: Max fed by Alice on 2024-01-01 08:00
        Name personName = TypicalPersons.ALICE.getName();
        Name differentAnimalName = TypicalAnimals.LUNA.getName();
        LocalDateTime sameTimeAsFs1 = LocalDateTime.of(2024, 1, 1, 8, 0);

        // Try to find the feeding session with Luna instead of Max
        DeleteFeedCommand command = new DeleteFeedCommand(personName, differentAnimalName, sameTimeAsFs1);
        CommandException ex = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(Messages.MESSAGE_FEEDING_SESSION_NOT_FOUND, ex.getMessage());
    }

    // Test equals() with successful return
    @Test
    public void equals_sameValues_returnsTrue() {
        Name person = new Name("Alice Pauline");
        Name animal = new Name("Max");
        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 8, 0);

        DeleteFeedCommand cmd1 = new DeleteFeedCommand(person, animal, time);
        DeleteFeedCommand cmd2 = new DeleteFeedCommand(new Name("Alice Pauline"), new Name("Max"),
                LocalDateTime.of(2024, 1, 1, 8, 0));

        assertTrue(cmd1.equals(cmd2));

        // different person
        DeleteFeedCommand diffPerson = new DeleteFeedCommand(new Name("Benson Meier"), animal, time);
        // different animal
        DeleteFeedCommand diffAnimal = new DeleteFeedCommand(person, new Name("Luna"), time);
        // different time
        DeleteFeedCommand diffTime = new DeleteFeedCommand(person, animal, LocalDateTime.of(2024, 1, 1, 9, 0));

        assertFalse(cmd1.equals(diffPerson));
        assertFalse(cmd1.equals(diffAnimal));
        assertFalse(cmd1.equals(diffTime));
    }

    // Test equals(), (other == this) branch taken
    @Test
    public void equals_sameInstance_returnsTrue() {
        Name person = new Name("Alice Pauline");
        Name animal = new Name("Max");
        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 8, 0);

        DeleteFeedCommand cmd = new DeleteFeedCommand(person, animal, time);
        Object[] box = new Object[] { cmd };
        Object sameRef = box[0];
        assertTrue(cmd.equals(sameRef));
    }

    // Test equals(), (other not instance of DeleteFeedCommand) branch taken
    @Test
    public void equals_differentType_returnsFalse() {
        Name person = new Name("Alice Pauline");
        Name animal = new Name("Max");
        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 8, 0);

        DeleteFeedCommand cmd = new DeleteFeedCommand(person, animal, time);
        assertFalse(cmd.equals(new Object()));
    }

    // Test toString() method
    @Test
    public void toString_returnsExpectedFormat() {
        Name personName = new Name("Alice Pauline");
        Name animalName = new Name("Max");
        LocalDateTime feedingTime = LocalDateTime.of(2024, 1, 1, 8, 0);

        DeleteFeedCommand cmd = new DeleteFeedCommand(personName, animalName, feedingTime);

        String expected = "seedu.address.logic.commands.DeleteFeedCommand{"
                + "personName=" + personName
                + ", animalName=" + animalName
                + ", feedingTime=" + feedingTime
                + "}";

        assertEquals(expected, cmd.toString());
    }
}
