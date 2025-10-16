package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.logic.commands.EditAnimalCommand;
import seedu.address.logic.commands.EditAnimalCommand.EditAnimalDescriptor;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.animal.AnimalName;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditAnimalCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditAnimalCommand
     * and returns a EditAnimalCommand object for execution.
     * @param args the arguments to be parsed
     * @return the command to be executed
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public EditCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DESCRIPTION, PREFIX_LOCATION);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_DESCRIPTION, PREFIX_LOCATION);

        EditAnimalDescriptor editAnimalDescriptor = new EditAnimalDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editAnimalDescriptor.setName(ParserUtil.parseAnimalName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_DESCRIPTION).isPresent()) {
            editAnimalDescriptor.setDescription(ParserUtil
                    .parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get()));
        }
        if (argMultimap.getValue(PREFIX_LOCATION).isPresent()) {
            editAnimalDescriptor.setLocation(ParserUtil.parseLocation(argMultimap.getValue(PREFIX_LOCATION).get()));
        }

        if (!editAnimalDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditAnimalCommand.MESSAGE_NOT_EDITED);
        }

        AnimalName name = ParserUtil.parseAnimalName(argMultimap.getValue(PREFIX_NAME).get());

        return new EditAnimalCommand(name, editAnimalDescriptor);
    }
}
