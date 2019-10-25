package seedu.address.model.person;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import seedu.address.model.display.detailwindow.WeekSchedule;
import seedu.address.model.mapping.Role;
import seedu.address.model.person.schedule.Event;
import seedu.address.model.person.schedule.Schedule;
import seedu.address.model.person.schedule.Timeslot;
import seedu.address.model.person.schedule.Venue;

/**
 * A class for testing Schedule View.
 */
public class ScheduleStub {

    public ScheduleStub() {
    }

    public WeekSchedule getSchedule() {
        Person p = new Person(new PersonDescriptor());
        p.setName(new Name("AlexwithaverylongnameSolongthateventhespellingofthisentiresentenceisonlyafraction"
                + "ofhistruename"));
        Schedule schedule = new Schedule(new PersonId(12345));
        Venue venue = new Venue("Central Library");

        LocalDateTime startTime1 = LocalDateTime.of(2019, 10, 21, 12, 0);
        LocalDateTime endTime1 = LocalDateTime.of(2019, 10, 21, 14, 0);

        LocalDateTime startTime2 = LocalDateTime.of(2019, 10, 22, 9, 0);
        LocalDateTime endTime2 = LocalDateTime.of(2019, 10, 22, 11, 0);

        LocalDateTime startTime3 = LocalDateTime.of(2019, 10, 23, 13, 0);
        LocalDateTime endTime3 = LocalDateTime.of(2019, 10, 23, 14, 0);

        LocalDateTime startTime4 = LocalDateTime.of(2019, 10, 24, 9, 0);
        LocalDateTime endTime4 = LocalDateTime.of(2019, 10, 24, 10, 0);

        LocalDateTime startTime5 = LocalDateTime.of(2019, 10, 24, 13, 0);
        LocalDateTime endTime5 = LocalDateTime.of(2019, 10, 24, 16, 0);

        Timeslot timeslot1 = new Timeslot(startTime1, endTime1, venue);
        Timeslot timeslot2 = new Timeslot(startTime2, endTime2, venue);
        Timeslot timeslot3 = new Timeslot(startTime3, endTime3, venue);
        Timeslot timeslot4 = new Timeslot(startTime4, endTime4, venue);
        Timeslot timeslot5 = new Timeslot(startTime5, endTime5, venue);
        Event monday1pmTo3pm = new Event("Test", new ArrayList<>(List.of(timeslot1, timeslot2, timeslot3,
                timeslot4, timeslot5)));
        schedule.addEvent(monday1pmTo3pm);
        p.setSchedule(schedule);
        return new WeekSchedule("TestSchedule", LocalDateTime.now(), p, Role.emptyRole());
    }
}
