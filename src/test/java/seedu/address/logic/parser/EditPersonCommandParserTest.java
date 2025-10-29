package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditPersonCommand;
import seedu.address.logic.commands.EditPersonCommand.EditPersonDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.PersonName;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.NameUtil;

public class EditPersonCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);
    private static final String MESSAGE_INVALID_TYPE_FORMAT = Messages.MESSAGE_INVALID_TYPE;

    private EditCommandParser parser = new EditCommandParser();
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void parse_missingParts_failure() {
        // no type specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "person " + VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no type and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    //     @Test
    //     public void parse_allFieldsSpecified_success() {
    //         Index targetIndex = INDEX_SECOND_PERSON;
    //         PersonName personName = model.getFilteredPersonList().get(targetIndex.getZeroBased()).getName();
    //         String userInput = "person " + personName
    //                 + PHONE_DESC_BOB + TAG_DESC_HUSBAND
    //                 + EMAIL_DESC_AMY + NAME_DESC_AMY + TAG_DESC_FRIEND;
    //
    //         EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
    //                 .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY)
    //                 .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    //         EditPersonCommand expectedCommand = new EditPersonCommand(personName, descriptor);
    //
    //         assertParseSuccess(parser, userInput, expectedCommand);
    //     }

    //     @Test
    //     public void parse_someFieldsSpecified_success() {
    //         Index targetIndex = INDEX_FIRST_PERSON;
    //         PersonName personName = model.getFilteredPersonList().get(targetIndex.getZeroBased()).getName();
    //         String userInput = "person " + personName
    //                 + PHONE_DESC_BOB + EMAIL_DESC_AMY;
    //
    //         EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB)
    //                 .withEmail(VALID_EMAIL_AMY).build();
    //         EditPersonCommand expectedCommand = new EditPersonCommand(personName, descriptor);
    //
    //         assertParseSuccess(parser, userInput, expectedCommand);
    //     }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_FIRST_PERSON;
        PersonName personName = NameUtil.getPersonName(
                model.getFilteredPersonList().get(targetIndex.getZeroBased()));
        String userInput = "person " + personName + " " + PREFIX_NAME + personName.fullName;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(personName.fullName).build();
        EditPersonCommand expectedCommand = new EditPersonCommand(personName, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        //        // phone
        //        userInput = "person " + personName + PHONE_DESC_AMY;
        //        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        //        expectedCommand = new EditPersonCommand(personName, descriptor);
        //        assertParseSuccess(parser, userInput, expectedCommand);
        //
        //        // email
        //        userInput = "person " + personName + EMAIL_DESC_AMY;
        //        descriptor = new EditPersonDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        //        expectedCommand = new EditPersonCommand(personName, descriptor);
        //        assertParseSuccess(parser, userInput, expectedCommand);
        //
        //        // tags
        //        userInput = "person " + personName + TAG_DESC_FRIEND;
        //        descriptor = new EditPersonDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        //        expectedCommand = new EditPersonCommand(personName, descriptor);
        //        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonTagValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_PERSON;
        PersonName personName = NameUtil.getPersonName(
                model.getFilteredPersonList().get(targetIndex.getZeroBased()));
        String userInput = "person " + personName
                + INVALID_PHONE_DESC + PHONE_DESC_BOB;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid
        userInput = "person " + personName
                + PHONE_DESC_BOB + INVALID_PHONE_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple valid fields repeated
        userInput = "person " + personName
                + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + TAG_DESC_FRIEND + PHONE_DESC_AMY + EMAIL_DESC_AMY + TAG_DESC_FRIEND
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + TAG_DESC_HUSBAND;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL));

        // multiple invalid values
        userInput = "person " + personName
                + INVALID_PHONE_DESC + INVALID_EMAIL_DESC
                + INVALID_PHONE_DESC + INVALID_EMAIL_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL));
    }

    //     @Test
    //     public void parse_resetTags_success() {
    //         Index targetIndex = INDEX_THIRD_PERSON;
    //         PersonName personName = model.getFilteredPersonList().get(targetIndex.getZeroBased()).getName();
    //         String userInput = "person " + personName + TAG_EMPTY;
    //
    //         EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags().build();
    //         EditPersonCommand expectedCommand = new EditPersonCommand(personName, descriptor);
    //
    //         assertParseSuccess(parser, userInput, expectedCommand);
    //     }
}
