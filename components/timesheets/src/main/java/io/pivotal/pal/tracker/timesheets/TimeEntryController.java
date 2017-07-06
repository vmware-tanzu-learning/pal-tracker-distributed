package io.pivotal.pal.tracker.timesheets;

import io.pivotal.pal.tracker.timesheets.data.TimeEntryDataGateway;
import io.pivotal.pal.tracker.timesheets.data.TimeEntryFields;
import io.pivotal.pal.tracker.timesheets.data.TimeEntryRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static io.pivotal.pal.tracker.timesheets.TimeEntryInfo.timeEntryInfoBuilder;
import static io.pivotal.pal.tracker.timesheets.data.TimeEntryFields.timeEntryFieldsBuilder;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private final TimeEntryDataGateway gateway;
    private final ProjectClient client;

    public TimeEntryController(TimeEntryDataGateway gateway, ProjectClient client) {
        this.gateway = gateway;
        this.client = client;
    }


    @PostMapping
    public ResponseEntity<TimeEntryInfo> create(@RequestBody TimeEntryForm form) {
        if (projectIsActive(form.projectId)) {
            TimeEntryRecord record = gateway.create(mapToFields(form));
            return new ResponseEntity<>(present(record), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping
    public List<TimeEntryInfo> list(@RequestParam long userId) {
        return gateway.findAllByUserId(userId).stream()
            .map(this::present)
            .collect(toList());
    }


    private TimeEntryInfo present(TimeEntryRecord record) {
        return timeEntryInfoBuilder()
            .id(record.id)
            .projectId(record.projectId)
            .userId(record.userId)
            .date(record.date.toString())
            .hours(record.hours)
            .info("time entry info")
            .build();
    }

    private TimeEntryFields mapToFields(TimeEntryForm form) {
        return timeEntryFieldsBuilder()
            .projectId(form.projectId)
            .userId(form.userId)
            .date(LocalDate.parse(form.date))
            .hours(form.hours)
            .build();
    }

    private boolean projectIsActive(long projectId) {
        ProjectInfo project = client.getProject(projectId);
        return project != null && project.active;
    }
}
