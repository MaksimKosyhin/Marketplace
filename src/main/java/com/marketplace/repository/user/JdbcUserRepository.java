package com.marketplace.repository.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean userExists(String username) {
        String sql = "SELECT EXISTS(" +
                "SELECT 1 " +
                "FROM users " +
                "WHERE username = ? " +
                "LIMIT 1)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, username);
    }

    @Override
    public long addUser(DbUser dbUser) {
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
                dbUser.getUsername(),
                dbUser.getPassword(),
                dbUser.getRole().name()
        );
    }

    @Override
    public DbUser getUser(String username) {
        String sql = "SELECT username, password, role " +
                "FROM users " +
                "WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);
    }

    private static class UserRowMapper implements RowMapper<DbUser> {
        @Override
        public DbUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            DbUser dbUser = new DbUser();
            dbUser.setUsername(rs.getString("username"));
            dbUser.setPassword(rs.getString("password"));
            dbUser.setRole(UserRole.valueOf(rs.getString("role")));
            return dbUser;
        }
    }
}
