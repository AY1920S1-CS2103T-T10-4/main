package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ACAD_YEAR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON_NOS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;
import static seedu.address.model.util.ModuleEventMappingUtil.mapModuleToEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.ModuleToEventMappingException;
import seedu.address.model.Model;
import seedu.address.model.display.detailwindow.DetailWindowDisplayType;
import seedu.address.model.display.sidepanel.SidePanelDisplayType;
import seedu.address.model.module.AcadYear;
import seedu.address.model.module.LessonNo;
import seedu.address.model.module.Module;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.module.ModuleId;
import seedu.address.model.module.SemesterNo;
import seedu.address.model.module.exceptions.ModuleNotFoundException;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.schedule.Event;

/**
 * Gets details about a module from NusMods
 */
public class AddNusModCommand extends Command {
    public static final String COMMAND_WORD = "addmod";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + PREFIX_NAME + "PERSON_NAME "
            + PREFIX_MODULE_CODE + "MODULE_CODE "
            + "[" + PREFIX_LESSON_NOS + "CLASS_NUMBERS (comma-separated)] "
            + "[" + PREFIX_ACAD_YEAR + "ACADEMIC_YEAR] "
            + "[" + PREFIX_SEMESTER + "SEMESTER]\n";

    public static final String MESSAGE_SUCCESS = "Added module to person's schedule: \n\n";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Unable to find person";
    public static final String MESSAGE_MODULE_NOT_FOUND = "Unable to find module";
    public static final String MESSAGE_EVENTS_CLASH = "Unable to add module - there is a timing clash "
            + "between the module you're adding and the events in the person's schedule!";

    private final Name name;
    private final ModuleCode moduleCode;
    private final List<LessonNo> lessonNoList;
    private final AddNusModCommandOptions options;

    public AddNusModCommand(Name name, ModuleCode moduleCode,
                            List<LessonNo> lessonNos, AddNusModCommandOptions options) {
        this.name = name;
        this.moduleCode = moduleCode;
        this.lessonNoList = lessonNos;
        this.options = options;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person person = model.findPerson(name);
        if (person == null) {
            return new CommandResult(MESSAGE_PERSON_NOT_FOUND);
        }

        AcadYear acadYear = options.getAcadYear().orElse(model.getDefaultAcadYear());
        SemesterNo semesterNo = options.getSemesterNo().orElse(model.getDefaultSemesterNo());
        String startAcadSemDateString = model.getAcadSemStartDateString(acadYear, semesterNo);
        List<String> holidayDateStrings = model.getHolidayDateStrings();
        Event event;
        Module module;
        ModuleId moduleId = new ModuleId(acadYear, moduleCode);

        try {
            module = model.findModule(moduleId);
            event = mapModuleToEvent(module, startAcadSemDateString, semesterNo,
                    this.lessonNoList, holidayDateStrings);
        } catch (ModuleNotFoundException e) {
            return new CommandResult(MESSAGE_MODULE_NOT_FOUND);
        } catch (ModuleToEventMappingException e) {
            return new CommandResult(e.getMessage());
        }

        if (model.isEventClash(name, event)) {
            return new CommandResult(MESSAGE_EVENTS_CLASH);
        }
        model.addEvent(name, event);

        // updates UI
        model.updateDetailWindowDisplay(name, LocalDateTime.now(), DetailWindowDisplayType.PERSON);
        model.updateSidePanelDisplay(SidePanelDisplayType.PERSONS);

        return new CommandResult(MESSAGE_SUCCESS + person.getSchedule());
    }

    @Override
    public boolean equals(Command command) {
        if (command == null) {
            return false;
        } else if (!(command instanceof AddNusModCommand)) {
            return false;
        } else if (((AddNusModCommand) command).moduleCode.equals(this.moduleCode)
                && ((AddNusModCommand) command).options.equals(this.options)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Contains optional arguments for executing the ShowNusModCommand.
     */
    public static class AddNusModCommandOptions {
        private AcadYear acadYear;
        private SemesterNo semesterNo;

        public AddNusModCommandOptions() {}

        public Optional<AcadYear> getAcadYear() {
            return Optional.ofNullable(acadYear);
        }

        public void setAcadYear(AcadYear acadYear) {
            this.acadYear = acadYear;
        }

        public Optional<SemesterNo> getSemesterNo() {
            return Optional.ofNullable(semesterNo);
        }

        public void setSemesterNo(SemesterNo semesterNo) {
            this.semesterNo = semesterNo;
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof AddNusModCommandOptions)) {
                return false;
            }

            // state check
            AddNusModCommandOptions o = (AddNusModCommandOptions) other;

            return getAcadYear().equals(o.getAcadYear())
                    && getSemesterNo().equals(o.getSemesterNo());
        }
    }
}
