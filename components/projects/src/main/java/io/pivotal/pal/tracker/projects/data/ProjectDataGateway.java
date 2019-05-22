package io.pivotal.pal.tracker.projects.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

import static io.pivotal.pal.tracker.projects.data.ProjectRecord.projectRecordBuilder;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Repository
public class ProjectDataGateway {

    private final JdbcTemplate jdbcTemplate;

    public ProjectDataGateway(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public ProjectRecord create(ProjectFields fields) {
        KeyHolder keyholder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "insert into projects (account_id, name, active) values (?, ?, ?)", RETURN_GENERATED_KEYS);
            ps.setLong(1, fields.accountId);
            ps.setString(2, fields.name);
            ps.setBoolean(3, true);
            return ps;
        }, keyholder);

        return find(keyholder.getKey().longValue());
    }

    public List<ProjectRecord> findAllByAccountId(Long accountId) {
        return jdbcTemplate.query(
            "select id, account_id, name, active from projects where account_id = ? order by name asc",
            rowMapper, accountId
        );
    }

    public ProjectRecord find(long id) {
        List<ProjectRecord> list = jdbcTemplate.query(
            "select id, account_id, name, active from projects where id = ? order by name asc",
            rowMapper, id
        );

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }


    private RowMapper<ProjectRecord> rowMapper =
        (rs, num) -> projectRecordBuilder()
            .id(rs.getLong("id"))
            .accountId(rs.getLong("account_id"))
            .name(rs.getString("name"))
            .active(rs.getBoolean("active"))
            .build();
}
