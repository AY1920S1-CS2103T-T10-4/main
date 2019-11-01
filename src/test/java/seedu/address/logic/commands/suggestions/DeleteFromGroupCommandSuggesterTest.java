package seedu.address.logic.commands.suggestions;

import seedu.address.logic.parser.CliSyntax;

class DeleteFromGroupCommandSuggesterTest extends SuggesterImplTester {
    protected DeleteFromGroupCommandSuggesterTest() throws ReflectiveOperationException {
        super(DeleteFromGroupCommandSuggester.class);
        disableAutoTestFor(CliSyntax.PREFIX_NAME);
    }
}
