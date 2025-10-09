package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.AddressBookParser.BASIC_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.regex.Matcher;

import seedu.address.logic.commands.DeleteAnimalCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeletePersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.animal.Animal;
import seedu.address.model.animal.AnimalName;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonName;

/**
 * Parses input arguments and creates a new DeletePersonCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeletePersonCommand
     * and returns a DeletePersonCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        try {
            final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(args.trim());

            if (!matcher.matches()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }

            String type = matcher.group("commandWord");
            ArgumentMultimap argMultimap =
                    ArgumentTokenizer.tokenize(matcher.group("arguments"), PREFIX_NAME);

            if (!argMultimap.getValue(PREFIX_NAME).isPresent()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        DeletePersonCommand.MESSAGE_USAGE));
            }

            if (type.equals(Person.PERSON_TYPE)) {
                PersonName personName = ParserUtil.parsePersonName(argMultimap.getValue(PREFIX_NAME).get());
                return new DeletePersonCommand(personName);
            }

            if (type.equals(Animal.ANIMAL_TYPE)) {
                AnimalName name = ParserUtil.parseAnimalName(argMultimap.getValue(PREFIX_NAME).get());
                return new DeleteAnimalCommand(name);
            }

            throw new ParseException("Invalid type: " + type);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE), pe);
        }
    }
}
