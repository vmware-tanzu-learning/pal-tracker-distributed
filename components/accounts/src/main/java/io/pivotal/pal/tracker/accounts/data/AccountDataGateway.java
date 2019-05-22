package io.pivotal.pal.tracker.accounts.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

import static io.pivotal.pal.tracker.accounts.data.AccountRecord.accountRecordBuilder;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Repository
public class AccountDataGateway {
    private final JdbcTemplate jdbcTemplate;

    public AccountDataGateway(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public AccountRecord create(long ownerId, String name) {
        KeyHolder keyholder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "insert into accounts (owner_id, name) values (?, ?)", RETURN_GENERATED_KEYS
            );

            ps.setLong(1, ownerId);
            ps.setString(2, name);
            return ps;
        }, keyholder);

        long id = keyholder.getKey().longValue();

        return jdbcTemplate.queryForObject("select id, owner_id, name from accounts where id = ?", rowMapper, id);
    }

    public List<AccountRecord> findAllByOwnerId(long ownerId) {
        return jdbcTemplate.query(
            "select id, owner_id, name from accounts where owner_id = ? order by name desc limit 1",
            rowMapper, ownerId
        );
    }

    private RowMapper<AccountRecord> rowMapper = (rs, num) -> accountRecordBuilder()
        .id(rs.getLong("id"))
        .ownerId(rs.getLong("owner_id"))
        .name(rs.getString("name"))
        .build();
}
