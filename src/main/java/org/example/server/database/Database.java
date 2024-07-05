package org.example.server.database;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.example.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class Database {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public Database(@Qualifier("SpringDataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Boolean check(User obj) throws DataAccessException {
            String sql = "SELECT * FROM users WHERE login = ?";
            String login = obj.getLogin();
            String pass = obj.getPass();
            ArrayList<User> users = new ArrayList<>();
            jdbcTemplate.query(sql, new Object[] {login}, (rs, rowNum) -> {
            User user = User.builder()
                            .login(rs.getString("login"))
                            .pass(rs.getString("pass"))
                            .ip(obj.getIp())
                            .build();
            users.add(user);
            return users;
        });

        if (users.isEmpty() || obj.getLogin().equals("") || obj.getPass().equals("")) return false;
        if(!pass.equals(users.get(0).getPass())) return false;
        System.err.println(obj);
        String updateSql = "UPDATE users SET ip = ? WHERE login = ?";
        jdbcTemplate.update(updateSql, obj.getIp(), login);
        return true;
    }

    
    public void addUser(User obj) throws DataAccessException {
            String login = obj.getLogin();
            String ip = obj.getIp();
            String pass = obj.getPass();
            String status = obj.getAuthentication().toString();
            String sql = "INSERT INTO users (status,login,pass,ip) " +
                         "VALUES (?,?,?,?)";
            jdbcTemplate.update(sql,status,login,pass,ip);
    }

    public void remove(User obj) throws DataAccessException {
            String login = obj.getLogin();
            String sql = "DELETE FROM users WHERE login = ?";
            jdbcTemplate.update(sql, login);
    }
}