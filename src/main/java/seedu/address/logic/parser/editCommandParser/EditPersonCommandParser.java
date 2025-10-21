package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATETIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FEEDING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditPersonCommand;
import seedu.address.logic.commands.EditPersonCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.feedingsession.IntermediateFeedingSession;
import seedu.address.model.person.PersonName;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditPersonCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditPersonCommand
     * and returns a EditPersonCommand object for execution.
     * @param args the arguments to be parsed
     * @return the command to be executed
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public EditCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_TAG, PREFIX_FEEDING, PREFIX_DATETIME);

        String personName = argMultimap.getPreamble().trim();

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parsePersonName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);
        parseIntermediateFeedingSessionsForEdit(argMultimap)
                .ifPresent(editPersonDescriptor::setIntermediateFeedingSessions);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        return new EditPersonCommand(new PersonName(personName), editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

    /**
     * Parses feeding sessions from the argument multimap.
     * Returns an Optional of Set containing the parsed feeding sessions.
     *
     * @param argMultimap ArgumentMultimap containing the feeding session details
     * @return Optional containing Set of IntermediateFeedingSession if present, empty Optional otherwise
     * @throws ParseException if feeding sessions are not properly formatted
     */
    private Optional<Set<IntermediateFeedingSession>> parseIntermediateFeedingSessionsForEdit(
            ArgumentMultimap argMultimap) throws ParseException {
        List<String> animalNames = argMultimap.getAllValues(PREFIX_FEEDING);
        List<String> dateTimes = argMultimap.getAllValues(PREFIX_DATETIME);

        if (animalNames.isEmpty() && dateTimes.isEmpty()) {
            return Optional.empty();
        }

        if (animalNames.size() != dateTimes.size()) {
            throw new ParseException("Each feeding session must have both an animal name and a datetime");
        }

        Set<IntermediateFeedingSession> sessions = new HashSet<>();
        for (int i = 0; i < animalNames.size(); i++) {
            String animalName = animalNames.get(i).trim();
            LocalDateTime dateTime = ParserUtil.parseDateTime(dateTimes.get(i));
            sessions.add(new IntermediateFeedingSession(animalName, dateTime));
        }

        return Optional.of(sessions);
    }
}
