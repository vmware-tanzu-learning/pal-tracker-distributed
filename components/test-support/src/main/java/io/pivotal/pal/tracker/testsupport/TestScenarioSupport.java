package io.pivotal.pal.tracker.testsupport;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.TimeZone;

public class TestScenarioSupport {

    public final JdbcTemplate template;
    public final DataSource dataSource;

    public TestScenarioSupport(String dbName) {
        dataSource = TestDataSourceFactory.create(dbName);
        template = new JdbcTemplate(dataSource);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void clearAllDatabases() {
        clearTables("tracker_allocations_test", "allocations");
        clearTables("tracker_backlog_test", "stories");
        clearTables("tracker_registration_test", "projects", "accounts", "users");
        clearTables("tracker_timesheets_test", "time_entries");
    }

    private static void clearTables(String dbName, String... tableNames) {
        JdbcTemplate template = new JdbcTemplate(TestDataSourceFactory.create(dbName));

        for (String tableName : tableNames) {
            template.execute("delete from " + tableName);
        }
    }
}
