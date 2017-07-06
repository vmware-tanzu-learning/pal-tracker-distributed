package io.pivotal.pal.tracker.backlog;

import io.pivotal.pal.tracker.backlog.data.StoryDataGateway;
import io.pivotal.pal.tracker.backlog.data.StoryFields;
import io.pivotal.pal.tracker.backlog.data.StoryRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.pivotal.pal.tracker.backlog.StoryInfo.storyInfoBuilder;
import static io.pivotal.pal.tracker.backlog.data.StoryFields.storyFieldsBuilder;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/stories")
public class StoryController {
    private final StoryDataGateway gateway;
    private final ProjectClient client;

    public StoryController(StoryDataGateway gateway, ProjectClient client) {
        this.gateway = gateway;
        this.client = client;
    }


    @PostMapping
    public ResponseEntity<StoryInfo> create(@RequestBody StoryForm form) {
        if (projectIsActive(form.projectId)) {
            StoryRecord record = gateway.create(mapToFields(form));
            return new ResponseEntity<>(present(record), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping
    public List<StoryInfo> list(@RequestParam long projectId) {
        return gateway.findAllByProjectId(projectId).stream()
            .map(this::present)
            .collect(toList());
    }


    private boolean projectIsActive(long projectId) {
        ProjectInfo project = client.getProject(projectId);
        return project != null && project.active;
    }

    private StoryFields mapToFields(StoryForm form) {
        return storyFieldsBuilder()
            .projectId(form.projectId)
            .name(form.name)
            .build();
    }

    private StoryInfo present(StoryRecord record) {
        return storyInfoBuilder()
            .id(record.id)
            .projectId(record.projectId)
            .name(record.name)
            .info("story info")
            .build();
    }
}
