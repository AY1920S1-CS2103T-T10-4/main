package seedu.address.logic.parser;

import seedu.address.logic.commands.AddPersonCommand;
import seedu.address.logic.commands.AddToGroupCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.group.GroupID;
import seedu.address.model.group.GroupName;
import seedu.address.model.person.Name;
import seedu.address.model.person.PersonID;

import java.util.stream.Stream;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUPNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

public class AddToGroupCommandParser implements Parser<AddToGroupCommand>{
    @Override
    public AddToGroupCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_GROUPNAME);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_GROUPNAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddToGroupCommand.MESSAGE_USAGE));
        }

        Name name = new Name(argMultimap.getValue(PREFIX_NAME).get());
        GroupName groupName = new GroupName(argMultimap.getValue(PREFIX_GROUPNAME).get());

        return new AddToGroupCommand(name, groupName);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
