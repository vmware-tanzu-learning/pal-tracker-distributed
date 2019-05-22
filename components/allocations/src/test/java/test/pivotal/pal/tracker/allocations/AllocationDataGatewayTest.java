package test.pivotal.pal.tracker.allocations;

import io.pivotal.pal.tracker.allocations.data.AllocationDataGateway;
import io.pivotal.pal.tracker.allocations.data.AllocationFields;
import io.pivotal.pal.tracker.allocations.data.AllocationRecord;
import io.pivotal.pal.tracker.testsupport.TestScenarioSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static io.pivotal.pal.tracker.allocations.data.AllocationFields.allocationFieldsBuilder;
import static io.pivotal.pal.tracker.allocations.data.AllocationRecord.allocationRecordBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class AllocationDataGatewayTest {

    private TestScenarioSupport testScenarioSupport = new TestScenarioSupport("tracker_allocations_test");
    private JdbcTemplate template = testScenarioSupport.template;
    private AllocationDataGateway gateway = new AllocationDataGateway(testScenarioSupport.dataSource);

    @Before
    public void setup() {
        template.execute("delete from allocations;");
    }

    @Test
    public void testCreate() {
        AllocationFields fields = allocationFieldsBuilder()
            .projectId(22L)
            .userId(12L)
            .firstDay(LocalDate.parse("2016-01-13"))
            .lastDay(LocalDate.parse("2016-09-17"))
            .build();


        AllocationRecord created = gateway.create(fields);


        assertThat(created.id).isNotNull();
        assertThat(created.projectId).isEqualTo(22L);
        assertThat(created.userId).isEqualTo(12L);
        assertThat(created.firstDay).isEqualTo(LocalDate.parse("2016-01-13"));
        assertThat(created.lastDay).isEqualTo(LocalDate.parse("2016-09-17"));

        Map<String, Object> persisted = template.queryForMap("select * from allocations WHERE id = ?", created.id);

        assertThat(persisted.get("project_id")).isEqualTo(22L);
        assertThat(persisted.get("user_id")).isEqualTo(12L);
        assertThat(persisted.get("first_day")).isEqualTo(Timestamp.valueOf("2016-01-13 00:00:00"));
        assertThat(persisted.get("last_day")).isEqualTo(Timestamp.valueOf("2016-09-17 00:00:00"));
    }

    @Test
    public void testFindAllByProjectId() {
        template.execute("insert into allocations (id, project_id, user_id, first_day, last_day) values (97336, 22, 12, '2016-01-13', '2016-09-17')");


        List<AllocationRecord> result = gateway.findAllByProjectId(22L);


        assertThat(result).containsExactly(allocationRecordBuilder()
            .id(97336L)
            .projectId(22L)
            .userId(12L)
            .firstDay(LocalDate.parse("2016-01-13"))
            .lastDay(LocalDate.parse("2016-09-17"))
            .build());
    }
}
