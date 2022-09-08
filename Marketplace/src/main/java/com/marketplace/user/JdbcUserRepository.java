package com.marketplace.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUserRepository implements UserRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long addUser(User user) {
        String sql = "INSERT INTO users(" +
                    "username, " +
                    "password, " +
                    "role) " +
                "VALUES(" +
                    "?," +
                    "?," +
                    "CAST(? AS user_role)) " +
                "RETURNING user_id";

        return  jdbcTemplate.queryForObject(
                sql,
                Long.class,
                user.getUsername(),
                user.getPassword(),
                user.getRole().name()
        );
    }

    @Override
    public User getUser(String username) {
        String sql = "SELECT username, password, role " +
                "FROM users " +
                "WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(UserRole.valueOf(rs.getString("role")));
            return user;
        }
    }
}
