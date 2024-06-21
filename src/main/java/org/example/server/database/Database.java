package org.example.server.database;

import org.example.server.model.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.example.server.model.User;

import java.util.ArrayList;

import javax.sql.DataSource;

@Component
public class Database {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public Database(@Qualifier("SpringDataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Boolean check(User obj) {
        try {
            String sql = "SELECT * FROM users WHERE login = ?";
            String login = obj.getLogin();
            ArrayList<User> users = new ArrayList<>();
            jdbcTemplate.query(sql, new Object[] {login}, (rs, rowNum) -> {
            User user = new User();
            user.setLogin(rs.getString("login"));
            users.add(user);
            return users;
        });

        if (users.isEmpty()) return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    final public void addUser(User obj) throws Exception {
            String login = obj.getLogin();
            String ip = obj.getIp();
            String pass = obj.getPass();
            String sql = "INSERT INTO users (login,pass,ip) " +
                         "VALUES (?,?,?)";
            jdbcTemplate.update(sql,login,pass,ip);
    }
}