package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TYPE_PERSON;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteContactCommand;
import seedu.address.logic.parser.deleteCommandParser.DeleteContactCommandParser;
import seedu.address.model.Name;
import seedu.address.model.person.Person;

public class DeleteCommandParserTest {

    private final DeleteContactCommandParser parser = new DeleteContactCommandParser();

    @Test
    public void parse_validPersonName_returnsDeleteCommand() {
        String input = VALID_TYPE_PERSON + " " + NAME_DESC_AMY;
        DeleteContactCommand<Person> expectedCommand = DeleteContactCommand.forPerson(new Name(VALID_NAME_AMY));
        assertParseSuccess(parser, input, expectedCommand);
    }

    @Test
    public void parse_invalidName_throwsParseException() {
        String input = ""; // empty string is invalid
        assertParseFailure(parser, input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidType_throwsParseException() {
        String input = "invalidType n/SomeName";
        assertParseFailure(parser, input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
}
