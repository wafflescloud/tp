package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalFeedingSessions.getTypicalAddressBook;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.Name;
import seedu.address.model.UserPrefs;
import seedu.address.model.animal.Animal;
import seedu.address.model.feedingsession.FeedingSession;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalAnimals;
import seedu.address.testutil.TypicalPersons;

public class FeedCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    // Test methods for FeedCommand

    // Add a feeding session successfully (using a valid Person, Animal and date time)
    @Test
    public void execute_validPersonAnimalDateTime_success() throws Exception {
        Person person = TypicalPersons.ALICE;
        Animal animal = TypicalAnimals.MAX;
        Name personName = person.getName();
        Name animalName = animal.getName();

        LocalDateTime feedingTime = LocalDateTime.of(2025, 12, 25, 9, 0);

        FeedCommand command = new FeedCommand(personName, animalName, feedingTime);

        CommandResult result = command.execute(model);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy 'at' HH:mm");
        String expectedMessage = String.format(FeedCommand.MESSAGE_SUCCESS,
                personName, animalName, feedingTime.format(formatter));
        assertEquals(expectedMessage, result.getFeedbackToUser());

        assertTrue(model.hasFeedingSessionByDetails(animal.getId(), person.getId(), feedingTime));

        FeedingSession addedSession = model.getFeedingSessionList().stream()
                .filter(s -> s.getAnimalId().equals(animal.getId())
                        && s.getPersonId().equals(person.getId())
                        && s.getDateTime().equals(feedingTime))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected feeding session was not found"));

        // Person and Animal should now include this feeding session ID
        Person updatedPerson = model.getPersonById(person.getId());
        Animal updatedAnimal = model.getAnimalById(animal.getId());
        assertTrue(updatedPerson.getFeedingSessionIds().contains(addedSession.getId()));
        assertTrue(updatedAnimal.getFeedingSessionIds().contains(addedSession.getId()));
    }

    // Fail to add a feeding session (non-existent Person, valid Animal, valid date time)
    @Test
    public void execute_nonExistentPerson_throwsCommandException() {
        Name missingPerson = new Name("Non Existent Person");
        Name existingAnimal = TypicalAnimals.MAX.getName();
        LocalDateTime feedingTime = LocalDateTime.of(2025, 12, 25, 9, 0);

        FeedCommand command = new FeedCommand(missingPerson, existingAnimal, feedingTime);

        CommandException ex = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(FeedCommand.MESSAGE_PERSON_NOT_FOUND, missingPerson), ex.getMessage());
    }

    // Fail to add a feeding session (valid Person, non-existent Animal, valid date time)
    @Test
    public void execute_nonExistentAnimal_throwsCommandException() {
        Name existingPerson = TypicalPersons.ALICE.getName();
        Name missingAnimal = new Name("Not A Real Animal");
        LocalDateTime feedingTime = LocalDateTime.of(2025, 12, 25, 9, 0);

        FeedCommand command = new FeedCommand(existingPerson, missingAnimal, feedingTime);

        CommandException ex = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(FeedCommand.MESSAGE_ANIMAL_NOT_FOUND, missingAnimal), ex.getMessage());
    }

    // Test for null handling
    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        Name personName = TypicalPersons.ALICE.getName();
        Name animalName = TypicalAnimals.MAX.getName();
        LocalDateTime validTime = LocalDateTime.of(2025, 12, 25, 9, 0);

        assertThrows(NullPointerException.class, () -> new FeedCommand(null, animalName, validTime));
        assertThrows(NullPointerException.class, () -> new FeedCommand(personName, null, validTime));
        assertThrows(NullPointerException.class, () -> new FeedCommand(personName, animalName, null));
    }

    // Fail to add a feeding session (duplicate feeding session)
    @Test
    public void execute_duplicateFeedingSession_throwsCommandException() {
        Name personName = TypicalPersons.ALICE.getName();
        Name animalName = TypicalAnimals.MAX.getName();
        LocalDateTime feedingTime = LocalDateTime.of(2024, 1, 1, 8, 0);

        FeedCommand command = new FeedCommand(personName, animalName, feedingTime);

        CommandException ex = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(FeedCommand.MESSAGE_DUPLICATE_FEEDING_SESSION, ex.getMessage());
    }

    // Test equals method
    @Test
    public void equals() {
        Name personA = new Name("Alice Pauline");
        Name animalMax = new Name("Max");
        LocalDateTime time = LocalDateTime.of(2025, 12, 25, 9, 0);

        FeedCommand cmd1 = new FeedCommand(personA, animalMax, time);
        FeedCommand cmd2 = new FeedCommand(new Name("Alice Pauline"), new Name("Max"),
                LocalDateTime.of(2025, 12, 25, 9, 0));

        assertTrue(cmd1.equals(cmd2));

        // different person
        FeedCommand diffPerson = new FeedCommand(new Name("Benson Meier"), animalMax, time);
        // different animal
        FeedCommand diffAnimal = new FeedCommand(personA, new Name("Luna"), time);
        // different time
        FeedCommand diffTime = new FeedCommand(personA, animalMax, LocalDateTime.of(2025, 12, 25, 10, 0));

        assertFalse(cmd1.equals(diffPerson));
        assertFalse(cmd1.equals(diffAnimal));
        assertFalse(cmd1.equals(diffTime));
    }

    // Test equals method (other == this) branch
    @Test
    public void equals_sameInstance_returnsTrue() {
        Name person = new Name("Alice Pauline");
        Name animal = new Name("Max");
        LocalDateTime time = LocalDateTime.of(2025, 12, 25, 9, 0);

        FeedCommand cmd = new FeedCommand(person, animal, time);
        Object[] box = new Object[] { cmd };
        Object sameRef = box[0];
        assertTrue(cmd.equals(sameRef));
    }

    // Test equals method (not other instance of FeedCommand) branch
    @Test
    public void equals_differentType_returnsFalse() {
        Name person = new Name("Alice Pauline");
        Name animal = new Name("Max");
        LocalDateTime time = LocalDateTime.of(2025, 12, 25, 9, 0);

        FeedCommand cmd = new FeedCommand(person, animal, time);
        assertFalse(cmd.equals(new Object()));
    }

    // Test toString method
    @Test
    public void toString_returnsExpectedFormat() {
        Name personName = new Name("Alice Pauline");
        Name animalName = new Name("Max");
        LocalDateTime feedingTime = LocalDateTime.of(2025, 12, 25, 9, 0);

        FeedCommand cmd = new FeedCommand(personName, animalName, feedingTime);

        String expected = "seedu.address.logic.commands.FeedCommand{"
                + "personName=" + personName
                + ", animalName=" + animalName
                + ", feedingTime=" + feedingTime
                + "}";

        assertEquals(expected, cmd.toString());
    }
}
