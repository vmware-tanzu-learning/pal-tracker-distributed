package test.pivotal.pal.tracker.backlog;

import io.pivotal.pal.tracker.backlog.*;
import io.pivotal.pal.tracker.backlog.data.StoryDataGateway;
import io.pivotal.pal.tracker.backlog.data.StoryRecord;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static io.pivotal.pal.tracker.backlog.StoryForm.storyFormBuilder;
import static io.pivotal.pal.tracker.backlog.StoryInfo.storyInfoBuilder;
import static io.pivotal.pal.tracker.backlog.data.StoryFields.storyFieldsBuilder;
import static io.pivotal.pal.tracker.backlog.data.StoryRecord.storyRecordBuilder;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static test.pivotal.pal.tracker.backlog.TestBuilders.*;

public class StoryControllerTest {

    private StoryDataGateway storyDataGateway = mock(StoryDataGateway.class);
    private ProjectClient client = mock(ProjectClient.class);
    private StoryController storyController = new StoryController(storyDataGateway, client);

    @Test
    public void testCreate() {
        StoryRecord record = storyRecordBuilder()
            .id(4L)
            .projectId(3L)
            .name("Something Fun")
            .build();

        doReturn(record).when(storyDataGateway).create(
            storyFieldsBuilder().projectId(3L).name("Something Fun").build()
        );

        doReturn(new ProjectInfo(true)).when(client).getProject(anyLong());

        StoryForm form = storyFormBuilder()
            .projectId(3L)
            .name("Something Fun")
            .build();


        ResponseEntity<StoryInfo> response = storyController.create(form);


        verify(client).getProject(3L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(storyInfoBuilder()
            .id(4L)
            .projectId(3L)
            .name("Something Fun")
            .info("story info")
            .build()
        );
    }

    @Test
    public void testFailedCreate() {
        doReturn(new ProjectInfo(false)).when(client).getProject(anyLong());

        StoryForm form = testStoryFormBuilder()
            .projectId(3L)
            .build();


        ResponseEntity<StoryInfo> response = storyController.create(form);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    public void testList() {
        List<StoryRecord> records = asList(
            testStoryRecordBuilder().id(12L).build(),
            testStoryRecordBuilder().id(13L).build()
        );

        doReturn(records).when(storyDataGateway).findAllByProjectId(anyLong());


        List<StoryInfo> result = storyController.list(13);


        verify(storyDataGateway).findAllByProjectId(13L);

        assertThat(result).containsExactlyInAnyOrder(
            testStoryInfoBuilder().id(12L).build(),
            testStoryInfoBuilder().id(13L).build()
        );
    }
}
