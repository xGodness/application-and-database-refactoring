package com.xgodness.itmodbcoursework.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.xgodness.itmodbcoursework.database.util.sql.DropSqlRequest;
import com.xgodness.itmodbcoursework.database.util.sql.InitSqlRequest;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConnectionInitializer {
    private final Connection connection;

    public ConnectionInitializer(@Value("${spring.datasource.url}") String host,
                                 @Value("${spring.datasource.username}") String username,
                                 @Value("${spring.datasource.password}") String password) throws SQLException {
        this.connection = DriverManager.getConnection(host, username, password);
    }

    @PostConstruct
    private void runSchemaInitialization() throws SQLException {
        for (var req : InitSqlRequest.values()) {
            try (var statement = connection.createStatement()) {
                statement.executeUpdate(InitSqlRequest.toSqlString(req));
            }
        }
    }

    @PreDestroy
    private void preDestroy() throws SQLException {
        for (var req : DropSqlRequest.values()) {
            try (var statement = connection.createStatement()) {
                statement.executeUpdate(DropSqlRequest.toSqlString(req));
            }
        }
        connection.close();
    }

    public Connection getConnection() {
        return connection;
    }
}
