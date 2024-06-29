package org.example.server.database;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.example.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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
            String pass = obj.getPass();
            ArrayList<User> users = new ArrayList<>();
            jdbcTemplate.query(sql, new Object[] {login}, (rs, rowNum) -> {
            User user = User.builder()
                            .login(rs.getString("login"))
                            .pass(rs.getString("pass"))
                            .build();
            users.add(user);
            return users;
        });

        if (users.isEmpty() || obj.getLogin().equals("") || obj.getPass().equals("")) return false;
        if(!pass.equals(users.get(0).getPass())) return false;

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