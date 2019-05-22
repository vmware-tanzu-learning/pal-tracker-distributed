package test.pivotal.pal.tracker.timesheets;

import io.pivotal.pal.tracker.timesheets.TimeEntryForm;
import io.pivotal.pal.tracker.timesheets.TimeEntryInfo;
import io.pivotal.pal.tracker.timesheets.data.TimeEntryFields;
import io.pivotal.pal.tracker.timesheets.data.TimeEntryRecord;

import java.time.LocalDate;

import static io.pivotal.pal.tracker.timesheets.TimeEntryForm.timeEntryFormBuilder;
import static io.pivotal.pal.tracker.timesheets.TimeEntryInfo.timeEntryInfoBuilder;
import static io.pivotal.pal.tracker.timesheets.data.TimeEntryFields.timeEntryFieldsBuilder;
import static io.pivotal.pal.tracker.timesheets.data.TimeEntryRecord.timeEntryRecordBuilder;

public class TestBuilders {

    public static TimeEntryRecord.Builder testTimeEntryRecordBuilder() {
        return timeEntryRecordBuilder()
            .id(11)
            .projectId(12)
            .userId(13)
            .date(LocalDate.parse("2017-09-19"))
            .hours(20);
    }

    public static TimeEntryFields.Builder testTimeEntryFieldsBuilder() {
        return timeEntryFieldsBuilder()
            .projectId(12)
            .userId(13)
            .date(LocalDate.parse("2017-09-19"))
            .hours(20);
    }

    public static TimeEntryForm.Builder testTimeEntryFormBuilder() {
        return timeEntryFormBuilder()
            .projectId(12)
            .userId(13)
            .date("2017-09-19")
            .hours(20);
    }

    public static TimeEntryInfo.Builder testTimeEntryInfoBuilder() {
        return timeEntryInfoBuilder()
            .id(11)
            .projectId(12)
            .userId(13)
            .date("2017-09-19")
            .hours(20)
            .info("time entry info");
    }
}
