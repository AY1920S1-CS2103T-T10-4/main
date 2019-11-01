package seedu.address.logic.commands.suggestions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.ArgumentList;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.logic.parser.CommandArgument;
import seedu.address.testutil.grouputil.TypicalGroups;

class EditGroupCommandSuggesterTest extends EditCommandSuggesterTest {

    protected EditGroupCommandSuggesterTest() throws ReflectiveOperationException {
        super(EditGroupCommandSuggester.class, PrefixEditType.GROUP);
    }

    @Test
    void getSuggestion_missingGroupPrefix_currentRemark() {
        final ArgumentList argumentList = singularArgumentListOfCommandArgument(CliSyntax.PREFIX_REMARK, 0, "");

        assertNoSuggestions(argumentList, argumentList.get(0));
    }

    @Test
    void getSuggestion_prefixRemarkExactGroup_currentRemark() {
        final ArgumentList argumentList = argumentListOf(
                new CommandArgument(CliSyntax.PREFIX_EDIT, 0, TypicalGroups.GROUPNAME1.toString()),
                new CommandArgument(CliSyntax.PREFIX_REMARK, 1, "")
        );

        final List<String> expectedSuggestions = List.of(TypicalGroups.GROUPREMARK1.toString());
        final List<String> actualSuggestions = getSuggestions(argumentList, argumentList.get(1));
        assertEquals(expectedSuggestions, actualSuggestions);
    }

    @Test
    void getSuggestion_prefixRemarkNonExistentGroup_noSuggestion() {
        final ArgumentList argumentList = argumentListOf(
                new CommandArgument(CliSyntax.PREFIX_EDIT, 0, TypicalGroups.GROUPNAME0.toString()),
                new CommandArgument(CliSyntax.PREFIX_REMARK, 1, "")
        );

        assertNoSuggestions(argumentList, argumentList.get(1));
    }

    @Test
    void getSuggestion_prefixDescriptionExactGroup_currentDescription() {
        final ArgumentList argumentList = argumentListOf(
                new CommandArgument(CliSyntax.PREFIX_EDIT, 0, TypicalGroups.GROUPNAME1.toString()),
                new CommandArgument(CliSyntax.PREFIX_DESCRIPTION, 1, "")
        );

        final List<String> expectedSuggestions = List.of(TypicalGroups.GROUPDESCRIPTION1.toString());
        final List<String> actualSuggestions = getSuggestions(argumentList, argumentList.get(1));
        assertEquals(expectedSuggestions, actualSuggestions);
    }

    @Test
    void getSuggestion_prefixDescriptionNonExistentGroup_noSuggestion() {
        final ArgumentList argumentList = argumentListOf(
                new CommandArgument(CliSyntax.PREFIX_EDIT, 0, TypicalGroups.GROUPNAME0.toString()),
                new CommandArgument(CliSyntax.PREFIX_REMARK, 1, "")
        );

        assertNoSuggestions(argumentList, argumentList.get(1));
    }

}
