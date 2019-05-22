package io.pivotal.pal.tracker.testsupport;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;


public class TestDataSourceFactory {

    public static DataSource create(String name) {
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setUrl("jdbc:mysql://localhost:3306/" + name + "?useSSL=false&useTimezone=true&serverTimezone=UTC&useLegacyDatetimeCode=false");
        dataSource.setUser("tracker");

        return dataSource;
    }
}
