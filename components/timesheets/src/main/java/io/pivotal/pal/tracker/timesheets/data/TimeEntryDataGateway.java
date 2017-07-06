package io.pivotal.pal.tracker.timesheets.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

import static io.pivotal.pal.tracker.timesheets.data.TimeEntryRecord.timeEntryRecordBuilder;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Repository
public class TimeEntryDataGateway {

    private JdbcTemplate jdbcTemplate;

    public TimeEntryDataGateway(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public TimeEntryRecord create(TimeEntryFields fields) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
            connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "insert into time_entries (project_id, user_id, date, hours) values (?, ?, ?, ?)", RETURN_GENERATED_KEYS);
                ps.setLong(1, fields.projectId);
                ps.setLong(2, fields.userId);
                ps.setDate(3, Date.valueOf(fields.date));
                ps.setInt(4, fields.hours);
                return ps;
            }, keyHolder);

        return find(keyHolder.getKey().longValue());
    }

    public List<TimeEntryRecord> findAllByUserId(long userId) {
        return jdbcTemplate.query(
            "select id, project_id, user_id, date, hours from time_entries where user_id = ?",
            rowMapper, userId
        );
    }


    private TimeEntryRecord find(long id) {
        return jdbcTemplate.queryForObject(
            "select id, project_id, user_id, date, hours from time_entries where id = ?",
            rowMapper, id
        );
    }

    private RowMapper<TimeEntryRecord> rowMapper = (rs, num) ->
        timeEntryRecordBuilder()
            .id(rs.getLong("id"))
            .projectId(rs.getLong("project_id"))
            .userId(rs.getLong("user_id"))
            .date(rs.getDate("date").toLocalDate())
            .hours(rs.getInt("hours"))
            .build();
}
