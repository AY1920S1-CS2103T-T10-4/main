package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.testutil.modelutil.TypicalModel;

class ClosestLocationCommandTest {
    private ModelManager model;
    private ArrayList<String> locationNameList;
    @BeforeEach
    void init() {
        model = TypicalModel.generateTypicalModel();
        locationNameList = new ArrayList<>(Arrays.asList("LT17", "LT17", "LT17"));
    }
    @Test
    void execute() throws CommandException {
        ClosestLocationCommand closestLocationCommand = new ClosestLocationCommand(locationNameList);
        String expectedResult = "Closest location found: \nFirst closest location: LT17 "
                + "| Average travelling distance/meters 0\n"
                + "Second closest location: LT19 | Average travelling distance/meters 11\n"
                + "Third closest location: AS4 | Average travelling distance/meters 224\n"
                + " location you entered: LT17 LT17 LT17 ";
        assertEquals(expectedResult, closestLocationCommand.execute(model).getFeedbackToUser());
    }

    @Test
    void testEquals() {
        ClosestLocationCommand closestLocationCommand = new ClosestLocationCommand(locationNameList);
        assertEquals(closestLocationCommand, closestLocationCommand);
    }
}
