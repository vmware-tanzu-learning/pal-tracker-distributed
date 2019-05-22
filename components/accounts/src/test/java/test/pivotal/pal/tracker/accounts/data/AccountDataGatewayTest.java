package test.pivotal.pal.tracker.accounts.data;

import io.pivotal.pal.tracker.accounts.data.AccountDataGateway;
import io.pivotal.pal.tracker.accounts.data.AccountRecord;
import io.pivotal.pal.tracker.testsupport.TestScenarioSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static io.pivotal.pal.tracker.accounts.data.AccountRecord.accountRecordBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountDataGatewayTest {

    private TestScenarioSupport testScenarioSupport = new TestScenarioSupport("tracker_registration_test");
    private JdbcTemplate template = testScenarioSupport.template;
    private AccountDataGateway gateway = new AccountDataGateway(testScenarioSupport.dataSource);

    @Before
    public void setup() {
        template.execute("DELETE FROM projects;");
        template.execute("DELETE FROM accounts;");
        template.execute("DELETE FROM users;");
    }

    @Test
    public void testCreate() {
        template.execute("insert into users (id, name) values (12, 'Jack')");


        AccountRecord created = gateway.create(12L, "anAccount");


        assertThat(created.id).isNotNull();
        assertThat(created.name).isEqualTo("anAccount");
        assertThat(created.ownerId).isEqualTo(12);

        Map<String, Object> persisted = template.queryForMap("SELECT * FROM accounts WHERE id = ?", created.id);
        assertThat(persisted.get("name")).isEqualTo("anAccount");
        assertThat(persisted.get("owner_id")).isEqualTo(12L);
    }

    @Test
    public void testFindBy() {
        template.execute("insert into users (id, name) values (12, 'Jack')");
        template.execute("insert into accounts (id, owner_id, name) values (1, 12, 'anAccount')");


        List<AccountRecord> result = gateway.findAllByOwnerId(12L);


        assertThat(result).containsExactly(accountRecordBuilder()
            .id(1L)
            .ownerId(12L)
            .name("anAccount")
            .build()
        );
    }
}
