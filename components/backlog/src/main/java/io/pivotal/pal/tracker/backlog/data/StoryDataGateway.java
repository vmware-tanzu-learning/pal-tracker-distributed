package io.pivotal.pal.tracker.backlog.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

import static io.pivotal.pal.tracker.backlog.data.StoryRecord.storyRecordBuilder;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Repository
public class StoryDataGateway {
    private final JdbcTemplate jdbcTemplate;

    public StoryDataGateway(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public StoryRecord create(StoryFields fields) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "insert into stories (project_id, name) values (?, ?)", RETURN_GENERATED_KEYS
            );

            ps.setLong(1, fields.projectId);
            ps.setString(2, fields.name);
            return ps;
        }, keyHolder);

        return find(keyHolder.getKey().longValue());
    }

    public List<StoryRecord> findAllByProjectId(Long projectId) {
        return jdbcTemplate.query(
            "select id, project_id, name from stories where project_id = ?",
            rowMapper, projectId
        );
    }


    private StoryRecord find(long id) {
        return jdbcTemplate.queryForObject(
            "select id, project_id, name from stories where id = ?",
            rowMapper, id
        );
    }

    private RowMapper<StoryRecord> rowMapper
        = (rs, num) -> storyRecordBuilder()
        .id(rs.getLong("id"))
        .projectId(rs.getLong("project_id"))
        .name(rs.getString("name"))
        .build();
}
