package test.pivotal.pal.tracker.users.data;


import io.pivotal.pal.tracker.testsupport.TestScenarioSupport;
import io.pivotal.pal.tracker.users.data.UserDataGateway;
import io.pivotal.pal.tracker.users.data.UserRecord;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDataGatewayTest {

    private TestScenarioSupport testScenarioSupport = new TestScenarioSupport("tracker_registration_test");
    private JdbcTemplate template = testScenarioSupport.template;
    private UserDataGateway gateway = new UserDataGateway(testScenarioSupport.dataSource);


    @Before
    public void setUp() throws Exception {
        template.execute("DELETE FROM projects;");
        template.execute("DELETE FROM accounts;");
        template.execute("DELETE FROM users;");
    }

    @Test
    public void testCreate() {
        UserRecord createdUser = gateway.create("aUser");


        assertThat(createdUser.id).isGreaterThan(0);
        assertThat(createdUser.name).isEqualTo("aUser");

        Map<String, Object> persistedFields = template.queryForMap("SELECT id, name FROM users WHERE id = ?", createdUser.id);
        assertThat(persistedFields.get("id")).isEqualTo(createdUser.id);
        assertThat(persistedFields.get("name")).isEqualTo(createdUser.name);
    }

    @Test
    public void testFind() {
        template.execute("INSERT INTO users(id, name) VALUES (42346, 'aName'), (42347, 'anotherName'), (42348, 'andAnotherName')");


        UserRecord record = gateway.find(42347L);


        assertThat(record).isEqualTo(new UserRecord(42347L, "anotherName"));
    }

    @Test
    public void testFind_WhenNotFound() {
        assertThat(gateway.find(42347L)).isNull();
    }
}
