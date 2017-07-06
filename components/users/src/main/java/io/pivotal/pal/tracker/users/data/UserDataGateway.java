package io.pivotal.pal.tracker.users.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Repository
public class UserDataGateway {

    private final JdbcTemplate jdbcTemplate;

    public UserDataGateway(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public UserRecord create(String name) {
        KeyHolder keyholder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into users (name) values (?)", RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            return ps;
        }, keyholder);

        return find(keyholder.getKey().longValue());
    }

    public UserRecord find(long id) {
        List<UserRecord> list = jdbcTemplate.query("select id, name from users where id = ? limit 1", rowMapper, id);

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }


    private RowMapper<UserRecord> rowMapper =
        (rs, num) -> new UserRecord(rs.getLong("id"), rs.getString("name"));
}
