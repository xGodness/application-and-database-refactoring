package com.xgodness.itmodbcoursework.repository;

import java.sql.Connection;
import java.sql.SQLException;

import com.xgodness.itmodbcoursework.database.ConnectionInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ChestRepository {
    private static final String OPEN_CHEST = """
            SELECT open_chest(?);
            """;
    private final Connection connection;

    public ChestRepository(@Autowired ConnectionInitializer connectionInitializer) {
        connection = connectionInitializer.getConnection();
    }

    public void openChest(long userId) throws SQLException {
        try (var statement = connection.prepareStatement(OPEN_CHEST)) {
            statement.setLong(1, userId);
            statement.execute();
        }
    }
}
