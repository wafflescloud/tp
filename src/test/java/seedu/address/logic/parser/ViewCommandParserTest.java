package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.TYPE_ANIMAL;
import static seedu.address.logic.parser.CliSyntax.TYPE_PERSON;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ViewCommand;

public class ViewCommandParserTest {

    private ViewCommandParser parser = new ViewCommandParser();
    private final String invalidCommandFormatMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            ViewCommand.MESSAGE_USAGE);

    // Valid Input Tests

    @Test
    public void parse_validPersonArgs_returnsViewCommand() {
        ViewCommand expectedCommand = new ViewCommand(TYPE_PERSON, "Alice Pauline");
        assertParseSuccess(parser, "person " + PREFIX_NAME + "Alice Pauline", expectedCommand);
    }

    @Test
    public void parse_validAnimalArgs_returnsViewCommand() {
        ViewCommand expectedCommand = new ViewCommand(TYPE_ANIMAL, "Fluffy");
        assertParseSuccess(parser, "animal " + PREFIX_NAME + "Fluffy", expectedCommand);
    }

    @Test
    public void parse_validArgsWithExtraSpaces_returnsViewCommand() {
        ViewCommand expectedCommand = new ViewCommand(TYPE_PERSON, "Alice Pauline");
        assertParseSuccess(parser, "  person   " + PREFIX_NAME + "  Alice Pauline  ", expectedCommand);
    }

    @Test
    public void parse_nameWithSpecialCharacters_success() {
        ViewCommand expectedCommand = new ViewCommand(TYPE_PERSON, "Mary-Jane O'Connor");
        assertParseSuccess(parser, "person " + PREFIX_NAME + "Mary-Jane O'Connor", expectedCommand);
    }

    @Test
    public void parse_nameWithNumbers_success() {
        ViewCommand expectedCommand = new ViewCommand(TYPE_ANIMAL, "Fluffy123");
        assertParseSuccess(parser, "animal " + PREFIX_NAME + "Fluffy123", expectedCommand);
    }

    @Test
    public void parse_singleWordName_success() {
        ViewCommand expectedCommand = new ViewCommand(TYPE_PERSON, "Alice");
        assertParseSuccess(parser, "person " + PREFIX_NAME + "Alice", expectedCommand);
    }

    @Test
    public void parse_longName_success() {
        String longName = "Alice Pauline Wonderland Smith Johnson Williams Brown Davis Miller Wilson Moore Taylor";
        ViewCommand expectedCommand = new ViewCommand(TYPE_PERSON, longName);
        assertParseSuccess(parser, "person " + PREFIX_NAME + longName, expectedCommand);
    }

    @Test
    public void parse_multipleNamePrefixes_returnsLastValue() {
        ViewCommand expectedCommand = new ViewCommand(TYPE_PERSON, "Bob");
        assertParseSuccess(parser, "person " + PREFIX_NAME + "Alice " + PREFIX_NAME + "Bob", expectedCommand);
    }

    // Case Insensitive Type Tests

    @Test
    public void parse_personTypeUpperCase_returnsViewCommand() {
        ViewCommand expectedCommand = new ViewCommand(TYPE_PERSON, "Alice Pauline");
        assertParseSuccess(parser, "PERSON " + PREFIX_NAME + "Alice Pauline", expectedCommand);
    }

    @Test
    public void parse_animalTypeUpperCase_returnsViewCommand() {
        ViewCommand expectedCommand = new ViewCommand(TYPE_ANIMAL, "Fluffy");
        assertParseSuccess(parser, "ANIMAL " + PREFIX_NAME + "Fluffy", expectedCommand);
    }

    @Test
    public void parse_personTypeMixedCase_returnsViewCommand() {
        ViewCommand expectedCommand = new ViewCommand(TYPE_PERSON, "Alice Pauline");
        assertParseSuccess(parser, "Person " + PREFIX_NAME + "Alice Pauline", expectedCommand);
        assertParseSuccess(parser, "pErSoN " + PREFIX_NAME + "Alice Pauline", expectedCommand);
    }

    @Test
    public void parse_animalTypeMixedCase_returnsViewCommand() {
        ViewCommand expectedCommand = new ViewCommand(TYPE_ANIMAL, "Fluffy");
        assertParseSuccess(parser, "Animal " + PREFIX_NAME + "Fluffy", expectedCommand);
        assertParseSuccess(parser, "aNiMaL " + PREFIX_NAME + "Fluffy", expectedCommand);
    }

    // Missing Component Tests

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "", invalidCommandFormatMessage);
    }

    @Test
    public void parse_whitespaceOnly_throwsParseException() {
        assertParseFailure(parser, "   ", invalidCommandFormatMessage);
    }

    @Test
    public void parse_missingType_throwsParseException() {
        assertParseFailure(parser, PREFIX_NAME + "Alice Pauline", invalidCommandFormatMessage);
    }

    @Test
    public void parse_onlyType_throwsParseException() {
        assertParseFailure(parser, "person", invalidCommandFormatMessage);
    }

    @Test
    public void parse_typeWithWhitespace_throwsParseException() {
        assertParseFailure(parser, "person   ", invalidCommandFormatMessage);
    }

    @Test
    public void parse_missingNamePrefix_throwsParseException() {
        assertParseFailure(parser, "person Alice Pauline", invalidCommandFormatMessage);
    }

    @Test
    public void parse_wrongPrefix_throwsParseException() {
        assertParseFailure(parser, "person p/Alice Pauline", invalidCommandFormatMessage);
    }

    @Test
    public void parse_missingName_throwsParseException() {
        assertParseFailure(parser, "person " + PREFIX_NAME, "Name cannot be empty");
    }

    @Test
    public void parse_emptyName_throwsParseException() {
        assertParseFailure(parser, "person " + PREFIX_NAME + "   ", "Name cannot be empty");
    }

    // Invalid Type Tests

    @Test
    public void parse_invalidType_throwsParseException() {
        assertParseFailure(parser, "vehicle " + PREFIX_NAME + "Car", invalidCommandFormatMessage);
    }

    @Test
    public void parse_numericType_throwsParseException() {
        assertParseFailure(parser, "123 " + PREFIX_NAME + "Alice", invalidCommandFormatMessage);
    }

    @Test
    public void parse_specialCharacterType_throwsParseException() {
        assertParseFailure(parser, "@#$ " + PREFIX_NAME + "Alice", invalidCommandFormatMessage);
    }

    @Test
    public void parse_emptyType_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_NAME + "Alice", invalidCommandFormatMessage);
    }

    // Preamble Tests

    @Test
    public void parse_extraPreambleBeforePrefix_throwsParseException() {
        assertParseFailure(parser, "person extratext " + PREFIX_NAME + "Alice", invalidCommandFormatMessage);
    }

    @Test
    public void parse_extraPreambleAfterType_throwsParseException() {
        assertParseFailure(parser, "person extra text " + PREFIX_NAME + "Alice", invalidCommandFormatMessage);
    }

    @Test
    public void parse_numberInPreamble_throwsParseException() {
        assertParseFailure(parser, "person 123 " + PREFIX_NAME + "Alice", invalidCommandFormatMessage);
    }

    // Edge Case Tests

    @Test
    public void parse_namePrefixAtStart_throwsParseException() {
        assertParseFailure(parser, PREFIX_NAME + "Alice person", invalidCommandFormatMessage);
    }

    @Test
    public void parse_typeAtEnd_throwsParseException() {
        assertParseFailure(parser, PREFIX_NAME + "Alice person", invalidCommandFormatMessage);
    }

    @Test
    public void parse_multiplePrefixesWithEmptyValues_throwsParseException() {
        assertParseFailure(parser, "person " + PREFIX_NAME + " " + PREFIX_NAME + " ", "Name cannot be empty");
    }

    @Test
    public void parse_onlySpacesInName_throwsParseException() {
        assertParseFailure(parser, "person " + PREFIX_NAME + "     ", "Name cannot be empty");
    }

    @Test
    public void parse_tabsAndSpacesInName_throwsParseException() {
        assertParseFailure(parser, "person " + PREFIX_NAME + "\t  \t  ", "Name cannot be empty");
    }

    // Boundary Value Tests

    @Test
    public void parse_singleCharacterName_success() {
        ViewCommand expectedCommand = new ViewCommand(TYPE_PERSON, "A");
        assertParseSuccess(parser, "person " + PREFIX_NAME + "A", expectedCommand);
    }

    @Test
    public void parse_veryLongName_success() {
        StringBuilder longNameBuilder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longNameBuilder.append("A");
        }
        String longName = longNameBuilder.toString();

        ViewCommand expectedCommand = new ViewCommand(TYPE_PERSON, longName);
        assertParseSuccess(parser, "person " + PREFIX_NAME + longName, expectedCommand);
    }

    @Test
    public void parse_nameWithUnicodeCharacters_success() {
        ViewCommand expectedCommand = new ViewCommand(TYPE_PERSON, "José María");
        assertParseSuccess(parser, "person " + PREFIX_NAME + "José María", expectedCommand);
    }

    @Test
    public void parse_nameWithEmojis_success() {
        ViewCommand expectedCommand = new ViewCommand(TYPE_ANIMAL, "Fluffy 🐱");
        assertParseSuccess(parser, "animal " + PREFIX_NAME + "Fluffy 🐱", expectedCommand);
    }
}
