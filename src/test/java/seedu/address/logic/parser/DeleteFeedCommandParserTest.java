package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteFeedCommand;
import seedu.address.model.Name;

public class DeleteFeedCommandParserTest {
    private final DeleteFeedCommandParser parser = new DeleteFeedCommandParser();

    // If the delete feed command parser successfully parses valid input (all flags present)
    @Test
    public void parse_allFieldsPresent_success() {
        String input = " f/James Tan  n/Max   dt/2025-12-25 09:00  ";
        DeleteFeedCommand expected = new DeleteFeedCommand(new Name("James Tan"), new Name("Max"),
                LocalDateTime.of(2025, 12, 25, 9, 0));
        assertParseSuccess(parser, input, expected);
    }

    @Test
    public void parse_allFieldsPresentWithExtraWhitespace_success() {
        String input = "\n\t  f/  James Tan \t n/  Max  \n dt/ 2025-12-25 09:00  ";
        DeleteFeedCommand expected = new DeleteFeedCommand(new Name("James Tan"), new Name("Max"),
                LocalDateTime.of(2025, 12, 25, 9, 0));
        assertParseSuccess(parser, input, expected);
    }

    // Negative test for f/ prefix for feeder does not exist
    @Test
    public void parse_missingFeederPrefix_failure() {
        String input = " n/Max dt/2025-12-25 09:00";
        assertParseFailure(parser, input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    // Negative test for n/ prefix for animal name does not exist
    @Test
    public void parse_missingAnimalNamePrefix_failure() {
        String input = " f/James Tan dt/2025-12-25 09:00";
        assertParseFailure(parser, input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    // Negative test for dt/ prefix for date time does not exist
    @Test
    public void parse_missingDateTimePrefix_failure() {
        String input = " f/James Tan n/Max";
        assertParseFailure(parser, input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    // Negative test for non-empty preamble
    @Test
    public void parse_nonEmptyPreamble_failure() {
        String input = "somePreamble f/James Tan n/Max dt/2025-12-25 09:00";
        assertParseFailure(parser, input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    // Negative test for duplicate prefixes
    @Test
    public void parse_duplicateFeederPrefix_failure() {
        String input = " f/James Tan f/Bob n/Max dt/2025-12-25 09:00";
        String expectedMessage = seedu.address.logic.Messages
                .getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_FEEDER);
        assertParseFailure(parser, input, expectedMessage);
    }

    @Test
    public void parse_duplicateAnimalNamePrefix_failure() {
        String input = " f/James Tan n/Max n/Luna dt/2025-12-25 09:00";
        String expectedMessage = seedu.address.logic.Messages
                .getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_NAME);
        assertParseFailure(parser, input, expectedMessage);
    }

    @Test
    public void parse_duplicateDateTimePrefix_failure() {
        String input = " f/James Tan n/Max dt/2025-12-25 09:00 dt/2025-12-26 10:00";
        String expectedMessage = seedu.address.logic.Messages
                .getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_DATETIME);
        assertParseFailure(parser, input, expectedMessage);
    }

    // Negative test for invalid personName parsing
    @Test
    public void parse_invalidPersonName_failure() {
        String input = " f/R@chel n/Max dt/2025-12-25 09:00";
        assertParseFailure(parser, input, seedu.address.model.Name.MESSAGE_CONSTRAINTS);
    }

    // Negative test for invalid animalName parsing
    @Test
    public void parse_invalidAnimalName_failure() {
        String input = " f/James Tan n/R@chel dt/2025-12-25 09:00";
        assertParseFailure(parser, input, seedu.address.model.Name.MESSAGE_CONSTRAINTS);
    }

    // Negative test for invalid feedingTime parsing
    @Test
    public void parse_invalidFeedingTime_failure() {
        String input = " f/James Tan n/Max dt/2025-02-30 10:00"; // invalid date
        assertParseFailure(parser, input, "Date and Time should exist and must be in format: yyyy-MM-dd HH:mm");
    }
}
