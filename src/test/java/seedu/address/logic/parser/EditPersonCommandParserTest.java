package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EDIT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.personutil.TypicalPersonDescriptor.ALICE;
import static seedu.address.testutil.personutil.TypicalPersonDescriptor.WHITESPACE;
import static seedu.address.testutil.personutil.TypicalPersonDescriptor.ZACK;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditPersonCommand;

class EditPersonCommandParserTest {

    private EditPersonCommandParser parser = new EditPersonCommandParser();

    @Test
    void parse_success() {
        assertParseSuccess(parser,
                WHITESPACE + PREFIX_EDIT + ALICE.getName().toString() + WHITESPACE
                        + PREFIX_NAME + ZACK.getName().toString() + WHITESPACE
                        + PREFIX_PHONE + ZACK.getPhone().toString() + WHITESPACE
                        + PREFIX_EMAIL + ZACK.getEmail().toString() + WHITESPACE
                        + PREFIX_ADDRESS + ZACK.getAddress().toString() + WHITESPACE
                        + PREFIX_REMARK + ZACK.getRemark().toString() + WHITESPACE
                        + PREFIX_TAG + WHITESPACE,
                new EditPersonCommand(ALICE.getName(), ZACK));
    }

    @Test
    void parse_failure() {
        assertParseFailure(parser,
                WHITESPACE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPersonCommand.MESSAGE_USAGE));

    }
}
