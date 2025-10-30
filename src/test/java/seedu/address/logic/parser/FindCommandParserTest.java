package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.ContactContainsKeywordsPredicate;
import seedu.address.model.animal.Animal;
import seedu.address.model.person.Person;

/**
 * Contains tests for FindCommandParser that handles both Person and Animal find commands.
 */
public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    // Person Tests

    @Test
    public void parse_emptyArgPerson_throwsParseException() {
        assertParseFailure(parser, "person     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyStringAfterTrimPerson_throwsParseException() {
        assertParseFailure(parser, "person",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyWhitespacesPerson_throwsParseException() {
        assertParseFailure(parser, "person   \t   \n   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validPersonNameArgs_returnsFindPersonCommand() {
        // no leading and trailing whitespaces
        FindCommand<Person> expectedFindPersonCommand =
                FindCommand.forPerson(new ContactContainsKeywordsPredicate<Person>(
                        Arrays.asList("Alice", "Bob"), Collections.emptyList()));
        assertParseSuccess(parser, "person n/Alice n/Bob", expectedFindPersonCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, "person \n n/Alice \n \t n/Bob  \t", expectedFindPersonCommand);
    }

    @Test
    public void parse_singlePersonKeyword_returnsFindPersonCommand() {
        FindCommand<Person> expectedFindPersonCommand =
                FindCommand.forPerson(new ContactContainsKeywordsPredicate<Person>(
                        Arrays.asList("Alice"), Collections.emptyList()));
        assertParseSuccess(parser, "person n/Alice", expectedFindPersonCommand);
    }

    @Test
    public void parse_multiplePersonKeywords_returnsFindPersonCommand() {
        FindCommand<Person> expectedFindPersonCommand =
                FindCommand.forPerson(new ContactContainsKeywordsPredicate<Person>(
                        Arrays.asList("Alice", "Bob", "Charlie", "Dave"), Collections.emptyList()));
        assertParseSuccess(parser, "person n/Alice n/Bob n/Charlie n/Dave", expectedFindPersonCommand);
    }

    @Test
    public void parse_personTagsOnly_returnsFindPersonCommand() {
        FindCommand<Person> expectedFindPersonCommand =
                FindCommand.forPerson(new ContactContainsKeywordsPredicate<Person>(
                        Collections.emptyList(), Arrays.asList("friend", "colleague")));
        assertParseSuccess(parser, "person t/friend t/colleague", expectedFindPersonCommand);
    }

    @Test
    public void parse_personNameAndTags_returnsFindPersonCommand() {
        FindCommand<Person> expectedFindPersonCommand =
                FindCommand.forPerson(new ContactContainsKeywordsPredicate<Person>(
                        Arrays.asList("Alice", "Bob"), Arrays.asList("friend", "colleague")));
        assertParseSuccess(parser, "person n/Alice n/Bob t/friend t/colleague", expectedFindPersonCommand);
    }

    @Test
    public void parse_mixedCasePersonKeywords_returnsFindPersonCommand() {
        FindCommand<Person> expectedFindPersonCommand =
                FindCommand.forPerson(new ContactContainsKeywordsPredicate<Person>(
                        Arrays.asList("aLICe", "bOB", "cHARlie"), Collections.emptyList()));
        assertParseSuccess(parser, "person n/aLICe n/bOB n/cHARlie", expectedFindPersonCommand);
    }

    @Test
    public void parse_specialCharactersInPersonKeywords_returnsFindPersonCommand() {
        FindCommand<Person> expectedFindPersonCommand =
                FindCommand.forPerson(new ContactContainsKeywordsPredicate<Person>(
                        Arrays.asList("O'Connor", "Smith-Jones"), Collections.emptyList()));
        assertParseSuccess(parser, "person n/O'Connor n/Smith-Jones", expectedFindPersonCommand);
    }

    // Animal Tests

    @Test
    public void parse_emptyArgAnimal_throwsParseException() {
        assertParseFailure(parser, "animal     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyStringAfterTrimAnimal_throwsParseException() {
        assertParseFailure(parser, "animal",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyWhitespacesAnimal_throwsParseException() {
        assertParseFailure(parser, "animal   \t   \n   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validAnimalNameArgs_returnsFindAnimalCommand() {
        // no leading and trailing whitespaces
        FindCommand<Animal> expectedFindAnimalCommand =
                FindCommand.forAnimal(new ContactContainsKeywordsPredicate<Animal>(
                        Arrays.asList("Whiskers", "Luna"), Collections.emptyList()));
        assertParseSuccess(parser, "animal n/Whiskers n/Luna", expectedFindAnimalCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, "animal \n n/Whiskers \n \t n/Luna  \t", expectedFindAnimalCommand);
    }

    @Test
    public void parse_singleAnimalKeyword_returnsFindAnimalCommand() {
        FindCommand<Animal> expectedFindAnimalCommand =
                FindCommand.forAnimal(new ContactContainsKeywordsPredicate<Animal>(
                        Arrays.asList("Simba"), Collections.emptyList()));
        assertParseSuccess(parser, "animal n/Simba", expectedFindAnimalCommand);
    }

    @Test
    public void parse_multipleAnimalKeywords_returnsFindAnimalCommand() {
        FindCommand<Animal> expectedFindAnimalCommand =
                FindCommand.forAnimal(new ContactContainsKeywordsPredicate<Animal>(
                        Arrays.asList("Whiskers", "Luna", "Simba", "Bella"), Collections.emptyList()));
        assertParseSuccess(parser, "animal n/Whiskers n/Luna n/Simba n/Bella", expectedFindAnimalCommand);
    }

    @Test
    public void parse_animalTagsOnly_returnsFindAnimalCommand() {
        FindCommand<Animal> expectedFindAnimalCommand =
                FindCommand.forAnimal(new ContactContainsKeywordsPredicate<Animal>(
                        Collections.emptyList(), Arrays.asList("friendly", "small")));
        assertParseSuccess(parser, "animal t/friendly t/small", expectedFindAnimalCommand);
    }

    @Test
    public void parse_animalNameAndTags_returnsFindAnimalCommand() {
        FindCommand<Animal> expectedFindAnimalCommand =
                FindCommand.forAnimal(new ContactContainsKeywordsPredicate<Animal>(
                        Arrays.asList("Whiskers", "Luna"), Arrays.asList("friendly", "small")));
        assertParseSuccess(parser, "animal n/Whiskers n/Luna t/friendly t/small", expectedFindAnimalCommand);
    }

    @Test
    public void parse_mixedCaseAnimalKeywords_returnsFindAnimalCommand() {
        FindCommand<Animal> expectedFindAnimalCommand =
                FindCommand.forAnimal(new ContactContainsKeywordsPredicate<Animal>(
                        Arrays.asList("wHISkers", "lUNA", "sIMba"), Collections.emptyList()));
        assertParseSuccess(parser, "animal n/wHISkers n/lUNA n/sIMba", expectedFindAnimalCommand);
    }

    @Test
    public void parse_specialCharactersInAnimalKeywords_returnsFindAnimalCommand() {
        FindCommand<Animal> expectedFindAnimalCommand =
                FindCommand.forAnimal(new ContactContainsKeywordsPredicate<Animal>(
                        Arrays.asList("Mr.Whiskers", "Luna-Belle"), Collections.emptyList()));
        assertParseSuccess(parser, "animal n/Mr.Whiskers n/Luna-Belle", expectedFindAnimalCommand);
    }

    // Invalid Type Tests

    @Test
    public void parse_invalidType_throwsParseException() {
        assertParseFailure(parser, "invalidtype n/Alice",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noType_throwsParseException() {
        assertParseFailure(parser, "n/Alice",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyInput_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyWhitespaces_throwsParseException() {
        assertParseFailure(parser, "   \t   \n   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    // Edge Case Tests

    @Test
    public void parse_personWithExtraWhitespace_returnsFindPersonCommand() {
        FindCommand<Person> expectedFindPersonCommand =
                FindCommand.forPerson(new ContactContainsKeywordsPredicate<Person>(
                        Arrays.asList("Alice", "Bob"), Collections.emptyList()));
        assertParseSuccess(parser, "\t  person   \n  n/Alice \t n/Bob ", expectedFindPersonCommand);
    }

    @Test
    public void parse_animalWithExtraWhitespace_returnsFindAnimalCommand() {
        FindCommand<Animal> expectedFindAnimalCommand =
                FindCommand.forAnimal(new ContactContainsKeywordsPredicate<Animal>(
                        Arrays.asList("Whiskers", "Luna"), Collections.emptyList()));
        assertParseSuccess(parser, "\t  animal   \n  n/Whiskers \t n/Luna ", expectedFindAnimalCommand);
    }

    @Test
    public void parse_singlePersonTag_returnsFindPersonCommand() {
        FindCommand<Person> expectedFindPersonCommand =
                FindCommand.forPerson(new ContactContainsKeywordsPredicate<Person>(
                        Collections.emptyList(), Arrays.asList("friend")));
        assertParseSuccess(parser, "person t/friend", expectedFindPersonCommand);
    }

    @Test
    public void parse_singleAnimalTag_returnsFindAnimalCommand() {
        FindCommand<Animal> expectedFindAnimalCommand =
                FindCommand.forAnimal(new ContactContainsKeywordsPredicate<Animal>(
                        Collections.emptyList(), Arrays.asList("friendly")));
        assertParseSuccess(parser, "animal t/friendly", expectedFindAnimalCommand);
    }

    @Test
    public void parse_emptyNameValuePerson_returnsFindPersonCommand() {
        // Test when there's an empty name value but tags are present
        FindCommand<Person> expectedFindPersonCommand =
                FindCommand.forPerson(new ContactContainsKeywordsPredicate<Person>(
                        Arrays.asList(""), Arrays.asList("friend")));
        assertParseSuccess(parser, "person n/ t/friend", expectedFindPersonCommand);
    }

    @Test
    public void parse_emptyNameValueAnimal_returnsFindAnimalCommand() {
        // Test when there's an empty name value but tags are present
        FindCommand<Animal> expectedFindAnimalCommand =
                FindCommand.forAnimal(new ContactContainsKeywordsPredicate<Animal>(
                        Arrays.asList(""), Arrays.asList("friendly")));
        assertParseSuccess(parser, "animal n/ t/friendly", expectedFindAnimalCommand);
    }
}
