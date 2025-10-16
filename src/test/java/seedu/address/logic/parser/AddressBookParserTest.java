package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
//import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
// import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

//import seedu.address.logic.commands.AddCommand;
//import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteAnimalCommand;
// import seedu.address.logic.commands.DeletePersonCommand;
// import seedu.address.logic.commands.editCommand.EditPersonCommand;
// import seedu.address.logic.commands.editCommand.EditPersonCommand.EditPersonDescriptor;
//import seedu.address.logic.commands.ExitCommand;
//import seedu.address.logic.commands.FindCommand;
//import seedu.address.logic.commands.HelpCommand;
//import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.animal.AnimalName;
//import seedu.address.model.person.NameContainsKeywordsPredicate;
// import seedu.address.model.person.Person;
// import seedu.address.model.person.PersonName;
// import seedu.address.testutil.EditPersonDescriptorBuilder;
// import seedu.address.testutil.PersonBuilder;
// import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    /*
    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }
    */
    // @Test
    // public void parseCommand_delete_person() throws Exception {
    //     String input = "delete person n/Alice";
    //     DeletePersonCommand command = (DeletePersonCommand) parser.parseCommand(input);
    //     assertEquals(new DeletePersonCommand(new PersonName("Alice")), command);
    // }

    @Test
    public void parseCommand_delete_animal() throws Exception {
        String input = "delete animal n/Whiskers";
        DeleteAnimalCommand command = (DeleteAnimalCommand) parser.parseCommand(input);
        assertEquals(new DeleteAnimalCommand(new AnimalName("Whiskers")), command);
    }

    @Test
    public void parseCommand_invalidType_throwsParseException() {
        String input = "delete vehicle Car1";
        assertThrows(ParseException.class, () -> parser.parseCommand(input));
    }

    @Test
    public void parseCommand_missingName_throwsParseException() {
        String input = "delete person";
        assertThrows(ParseException.class, () -> parser.parseCommand(input));
    }

    // @Test
    // public void parseCommand_edit() throws Exception {
    //     Person person = new PersonBuilder().build();

    //     PersonName personName = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getName();

    //     EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
    //     EditPersonCommand command = (EditPersonCommand) parser.parseCommand(EditPersonCommand.COMMAND_WORD
    //             + " "
    //             + Person.PERSON_TYPE + " " + personName.toString() + " "
    //             + PersonUtil.getEditPersonDescriptorDetails(descriptor));
    //     assertEquals(new EditPersonCommand(personName, descriptor), command);
    // }


    //    @Test
    //    public void parseCommand_exit() throws Exception {
    //        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
    //        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    //    }
    //
    //    @Test
    //    public void parseCommand_find() throws Exception {
    //        List<String> keywords = Arrays.asList("foo", "bar", "baz");
    //        FindCommand command = (FindCommand) parser.parseCommand(
    //                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
    //        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    //    }
    //
    //    @Test
    //    public void parseCommand_help() throws Exception {
    //        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
    //        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    //    }
    //
    //    @Test
    //    public void parseCommand_list() throws Exception {
    //        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
    //        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    //    }
    //
    //    @Test
    //    public void parseCommand_unrecognisedInput_throwsParseException() {
    //        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
    //        HelpCommand.MESSAGE_USAGE), ()
    //            -> parser.parseCommand(""));
    //    }
    //
    //    @Test
    //    public void parseCommand_unknownCommand_throwsParseException() {
    //        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    //    }
}
