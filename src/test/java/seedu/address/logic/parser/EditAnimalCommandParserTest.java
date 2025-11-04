package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_CHOCO;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_KITTY;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_LOCATION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.LOCATION_DESC_CHOCO;
import static seedu.address.logic.commands.CommandTestUtil.LOCATION_DESC_KITTY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_KITTY;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_CHOCO;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_KITTY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LOCATION_KITTY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_CHOCO;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_KITTY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditAnimalCommand;
import seedu.address.logic.commands.EditAnimalCommand.EditAnimalDescriptor;
import seedu.address.logic.commands.EditCommand;
import seedu.address.model.Name;
import seedu.address.model.animal.Description;
import seedu.address.model.animal.Location;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditAnimalDescriptorBuilder;

public class EditAnimalCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditAnimalCommandParser parser = new EditAnimalCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, "n/" + VALID_NAME_CHOCO, MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // invalid name
        assertParseFailure(parser, "0", MESSAGE_INVALID_FORMAT);

        // no name
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, VALID_NAME_CHOCO + " i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, VALID_NAME_CHOCO + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);
        // invalid location
        assertParseFailure(parser, VALID_NAME_CHOCO + INVALID_LOCATION_DESC, Location.MESSAGE_CONSTRAINTS);
        // invalid description
        assertParseFailure(parser, VALID_NAME_CHOCO + INVALID_DESCRIPTION_DESC,
                Description.MESSAGE_CONSTRAINTS);
        // invalid tag
        assertParseFailure(parser, VALID_NAME_CHOCO + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);

        // invalid location followed by valid description
        assertParseFailure(parser, VALID_NAME_CHOCO + INVALID_LOCATION_DESC + DESCRIPTION_DESC_CHOCO,
                Location.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Person} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, VALID_NAME_CHOCO + TAG_DESC_FRIEND + TAG_EMPTY,
                Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, VALID_NAME_CHOCO + INVALID_NAME_DESC + INVALID_LOCATION_DESC
                + VALID_DESCRIPTION_CHOCO, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Name animalName = new Name(VALID_NAME_CHOCO);
        String userInput = VALID_NAME_CHOCO + NAME_DESC_KITTY + LOCATION_DESC_KITTY + TAG_DESC_FRIEND
                + DESCRIPTION_DESC_KITTY;

        EditAnimalDescriptor descriptor = new EditAnimalDescriptorBuilder().withName(VALID_NAME_KITTY)
                .withLocation(VALID_LOCATION_KITTY).withDescription(VALID_DESCRIPTION_KITTY)
                .withTags(VALID_TAG_FRIEND).build();
        EditAnimalCommand expectedCommand = new EditAnimalCommand(animalName, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Name animalName = new Name(VALID_NAME_KITTY);
        String userInput = animalName + LOCATION_DESC_KITTY + DESCRIPTION_DESC_KITTY;

        EditAnimalDescriptor descriptor = new EditAnimalDescriptorBuilder().withLocation(VALID_LOCATION_KITTY)
                .withDescription(VALID_DESCRIPTION_KITTY).build();
        EditAnimalCommand expectedCommand = new EditAnimalCommand(animalName, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Name animalName = new Name(VALID_NAME_KITTY);
        String userInput = animalName + NAME_DESC_KITTY;
        EditAnimalDescriptor descriptor = new EditAnimalDescriptorBuilder().withName(VALID_NAME_KITTY).build();
        EditAnimalCommand expectedCommand = new EditAnimalCommand(animalName, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // location
        userInput = animalName + LOCATION_DESC_KITTY;
        descriptor = new EditAnimalDescriptorBuilder().withLocation(VALID_LOCATION_KITTY).build();
        expectedCommand = new EditAnimalCommand(animalName, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // description
        userInput = animalName + DESCRIPTION_DESC_KITTY;
        descriptor = new EditAnimalDescriptorBuilder().withDescription(VALID_DESCRIPTION_KITTY).build();
        expectedCommand = new EditAnimalCommand(animalName, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = animalName + TAG_DESC_FRIEND;
        descriptor = new EditAnimalDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        expectedCommand = new EditAnimalCommand(animalName, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonTagValue_failure()

        // valid followed by invalid
        Name animalName = new Name(VALID_NAME_KITTY);
        String userInput = animalName + INVALID_LOCATION_DESC + LOCATION_DESC_KITTY;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_LOCATION));

        // invalid followed by valid
        userInput = animalName + LOCATION_DESC_KITTY + INVALID_LOCATION_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_LOCATION));

        // mulltiple valid fields repeated
        userInput = animalName + LOCATION_DESC_KITTY + DESCRIPTION_DESC_KITTY
                + TAG_DESC_FRIEND + LOCATION_DESC_KITTY + DESCRIPTION_DESC_KITTY + TAG_DESC_FRIEND
                + LOCATION_DESC_CHOCO + DESCRIPTION_DESC_CHOCO;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_LOCATION, PREFIX_DESCRIPTION));

        // multiple invalid values
        userInput = animalName + INVALID_LOCATION_DESC + INVALID_DESCRIPTION_DESC
                + INVALID_LOCATION_DESC + INVALID_DESCRIPTION_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_LOCATION, PREFIX_DESCRIPTION));
    }

    @Test
    public void parse_resetTags_success() {
        Name animalName = new Name(VALID_NAME_KITTY);
        String userInput = animalName + TAG_EMPTY;

        EditAnimalDescriptor descriptor = new EditAnimalDescriptorBuilder().withTags().build();
        EditAnimalCommand expectedCommand = new EditAnimalCommand(animalName, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
