package seedu.address.logic.commands.suggestions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import seedu.address.logic.parser.ArgumentList;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.logic.parser.CommandArgument;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;

/**
 * Provides suggestions for the {@link Prefix}es of the {@link seedu.address.logic.commands.EditGroupCommand}.
 */
public class EditGroupCommandSuggester extends Suggester {
    public static final List<Prefix> SUPPORTED_PREFIXES = Collections.unmodifiableList(List.of(
            CliSyntax.PREFIX_EDIT,
            CliSyntax.PREFIX_REMARK
    ));

    protected static Optional<Group> getSelectedGroup(final Model model, final ArgumentList arguments) {
        final Optional<String> groupNameInput = arguments.getFirstValueOfPrefix(CliSyntax.PREFIX_EDIT);
        if (groupNameInput.isEmpty()) {
            return Optional.empty();
        }

        final GroupName groupName = new GroupName(groupNameInput.get());
        final Group group = model.findGroup(groupName);

        return Optional.ofNullable(group);
    }

    @Override
    protected List<String> provideSuggestions(
            final Model model, final ArgumentList arguments, final CommandArgument commandArgument) {
        final Prefix prefix = commandArgument.getPrefix();
        final String value = commandArgument.getValue();

        if (prefix.equals(CliSyntax.PREFIX_EDIT)) {
            return model.groupSuggester(value);
        } else if (prefix.equals(CliSyntax.PREFIX_REMARK)) {
            final Optional<Group> optionalSelectedGroup = getSelectedGroup(model, arguments);

            if (optionalSelectedGroup.isPresent()) {
                final Group selectedGroup = optionalSelectedGroup.get();
                return List.of(selectedGroup.getGroupRemark().toString());
            }
        }

        return null;
    }
}
