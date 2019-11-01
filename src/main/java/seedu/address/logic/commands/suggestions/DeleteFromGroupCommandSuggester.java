package seedu.address.logic.commands.suggestions;

import java.util.List;
import java.util.Optional;

import seedu.address.logic.parser.ArgumentList;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.logic.parser.CommandArgument;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;

/**
 * Provides suggestions for the {@link Prefix}es of the {@link seedu.address.logic.commands.DeleteFromGroupCommand}.
 */
public class DeleteFromGroupCommandSuggester extends Suggester {
    public static final List<Prefix> SUPPORTED_PREFIXES = List.of(
            CliSyntax.PREFIX_GROUPNAME,
            CliSyntax.PREFIX_NAME
    );

    @Override
    protected List<String> provideSuggestions(
            final Model model, final ArgumentList arguments, final CommandArgument commandArgument) {
        final Prefix prefix = commandArgument.getPrefix();

        if (!SUPPORTED_PREFIXES.contains(prefix)) {
            return null;
        } else if (prefix.equals(CliSyntax.PREFIX_GROUPNAME)) {
            return getGroupNameSuggestions(model, commandArgument);
        } else if (prefix.equals(CliSyntax.PREFIX_NAME)) {
            final Optional<String> groupNameInput = arguments.getFirstValueOfPrefix(CliSyntax.PREFIX_GROUPNAME);
            if (groupNameInput.isEmpty() || (groupNameInput.isPresent() && groupNameInput.get().isBlank())) {
                return null;
            }

            final String personNameInput = commandArgument.getValue();

            return model.personSuggester(personNameInput, groupNameInput.get());
        }

        return null;
    }
}
