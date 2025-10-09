package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TYPE_PERSON;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeletePersonCommand;
import seedu.address.model.person.PersonName;

public class DeleteCommandParserTest {

    private final DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validPersonName_returnsDeleteCommand() {
        String input = VALID_TYPE_PERSON + " " + NAME_DESC_AMY;
        DeletePersonCommand expectedCommand = new DeletePersonCommand(new PersonName(VALID_NAME_AMY));
        assertParseSuccess(parser, input, expectedCommand);
    }

    @Test
    public void parse_invalidName_throwsParseException() {
        String input = ""; // empty string is invalid
        String type = "person";
        assertParseFailure(parser, input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidType_throwsParseException() {
        String input = "invalidType n/SomeName";
        assertParseFailure(parser, input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE));
    }

}
