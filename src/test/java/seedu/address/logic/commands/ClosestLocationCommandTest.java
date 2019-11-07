package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.group.exceptions.DuplicateGroupException;
import seedu.address.model.mapping.exceptions.DuplicateMappingException;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.modelutil.TypicalModel;

class ClosestLocationCommandTest {
    private ModelManager model;
    private ArrayList<String> locationNameList;
    @BeforeEach
    void init() throws DuplicateMappingException, DuplicatePersonException, DuplicateGroupException {
        model = TypicalModel.generateTypicalModel();
        locationNameList = new ArrayList<>(Arrays.asList("LT17", "LT17", "LT17"));
    }
    @Test
    void execute() throws CommandException {
        ClosestLocationCommand closestLocationCommand = new ClosestLocationCommand(locationNameList);
        String expectedResult = "Closest location found: \n"
               + "First closest location: LT17 | Average travelling distance/meters Avg distance: 0(meters)\n"
               + "Second closest location: LT19 | Average travelling distance/meters Avg distance: 11(meters)\n"
               + "Third closest location: SH | Average travelling distance/meters Avg distance: 403(meters)\n"
               + " location you entered: LT17 LT17 LT17 ";
        assertEquals(expectedResult, closestLocationCommand.execute(model).getFeedbackToUser());
    }

    @Test
    void executeSadFlow() throws CommandException {
        ArrayList<String> locationNameList = new ArrayList<>();
        ClosestLocationCommand closestLocationCommand = new ClosestLocationCommand(locationNameList);
        String expectedResult = "Closest location found: Cannot get result because You must enter at least one "
                + "location.\n"
                + " location you entered: ";
        assertEquals(closestLocationCommand.execute(model).getFeedbackToUser(), expectedResult);
    }

    @Test
    void testEqualsSameInstance() {
        ClosestLocationCommand closestLocationCommand = new ClosestLocationCommand(locationNameList);
        assertEquals(closestLocationCommand, closestLocationCommand);
    }
}
