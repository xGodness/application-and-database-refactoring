package com.xgodness.itmodbcoursework.repository;

import java.sql.Connection;
import java.sql.SQLException;

import com.xgodness.itmodbcoursework.database.ConnectionInitializer;
import com.xgodness.itmodbcoursework.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private static final String SAVE_USER = """
            INSERT INTO app_user (username, password, role) VALUES (?, ?, 'USER');
            """;
    private static final String SELECT_USER_BY_USERNAME = """
            SELECT * FROM app_user WHERE username = ?;
            """;
    private static final String SELECT_USER_BY_ID = """
            SELECT * FROM app_user WHERE id = ?;
            """;
    private static final String OP_USER = """
            UPDATE app_user SET role = 'ADMIN' WHERE username = ?;
            """;
    private final Connection connection;

    public UserRepository(@Autowired ConnectionInitializer connectionInitializer) {
        connection = connectionInitializer.getConnection();
    }

    public long login(User user) throws SQLException {
        try (var statement = connection.prepareStatement(SELECT_USER_BY_USERNAME)) {
            statement.setString(1, user.getUsername());
            var rs = statement.executeQuery();
            if (rs.next()
                    && rs.getString("username").equals(user.getUsername())
                    && rs.getString("password").equals(user.getPassword())) {
                return rs.getLong("id");
            }
            return -1L;
        }
    }

    public long findUserIdByUsername(String username) throws SQLException {
        try (var statement = connection.prepareStatement(SELECT_USER_BY_USERNAME)) {
            statement.setString(1, username);
            var rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getLong("id");
            }
            return -1;
        }
    }

    public String findUsernameById(long userId) throws SQLException {
        try (var statement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            statement.setLong(1, userId);
            var rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
            return null;
        }
    }

    public void save(User user) throws SQLException {
        try (var statement = connection.prepareStatement(SAVE_USER)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
        }
    }

    public boolean existsByUsername(String username) throws SQLException {
        if (username == null) return false;
        try (var statement = connection.prepareStatement(SELECT_USER_BY_USERNAME)) {
            statement.setString(1, username);
            return statement.executeQuery().next();
        }
    }

    public boolean isAdmin(User user) throws SQLException {
        try (var statement = connection.prepareStatement(SELECT_USER_BY_USERNAME)) {
            statement.setString(1, user.getUsername());
            var rs = statement.executeQuery();
            return rs.next() && rs.getObject("role").toString().equals("ADMIN");
        }
    }

    public void opUser(User user) throws SQLException {
        try (var statement = connection.prepareStatement(OP_USER)) {
            statement.setString(1, user.getUsername());
            statement.executeUpdate();
        }
    }
}
