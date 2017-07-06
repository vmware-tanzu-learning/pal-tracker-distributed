package test.pivotal.pal.tracker.allocations;

import io.pivotal.pal.tracker.allocations.*;
import io.pivotal.pal.tracker.allocations.data.AllocationDataGateway;
import io.pivotal.pal.tracker.allocations.data.AllocationRecord;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static test.pivotal.pal.tracker.allocations.TestBuilders.*;


public class AllocationControllerTest {

    private AllocationDataGateway allocationDataGateway = mock(AllocationDataGateway.class);
    private ProjectClient client = mock(ProjectClient.class);
    private AllocationController allocationsController = new AllocationController(allocationDataGateway, client);


    @Test
    public void testCreate() {
        AllocationRecord record = testAllocationRecordBuilder()
            .id(20L)
            .projectId(31L)
            .firstDay(LocalDate.parse("2016-02-20"))
            .build();
        doReturn(record).when(allocationDataGateway).create(any());
        doReturn(new ProjectInfo(true)).when(client).getProject(anyLong());


        AllocationForm form = testAllocationFormBuilder()
            .projectId(31L)
            .firstDay("2016-02-20")
            .build();
        ResponseEntity<AllocationInfo> response = allocationsController.create(form);


        verify(allocationDataGateway).create(testAllocationFieldsBuilder()
            .projectId(31L)
            .firstDay(LocalDate.parse("2016-02-20"))
            .build()
        );
        verify(client).getProject(31L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(testAllocationInfoBuilder()
            .id(20L)
            .projectId(31L)
            .firstDay("2016-02-20")
            .build()
        );
    }

    @Test
    public void testCreate_WhenProjectIsNotActive() {
        doReturn(new ProjectInfo(false)).when(client).getProject(anyLong());

        AllocationForm form = testAllocationFormBuilder().build();


        ResponseEntity<AllocationInfo> response = allocationsController.create(form);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    public void testList() {
        List<AllocationRecord> records = asList(
            testAllocationRecordBuilder().id(12L).build(),
            testAllocationRecordBuilder().id(13L).build()
        );
        doReturn(records).when(allocationDataGateway).findAllByProjectId(anyLong());


        List<AllocationInfo> result = allocationsController.list(13);


        verify(allocationDataGateway).findAllByProjectId(13L);
        assertThat(result).containsExactlyInAnyOrder(
            testAllocationInfoBuilder().id(12L).build(),
            testAllocationInfoBuilder().id(13L).build()
        );
    }
}
