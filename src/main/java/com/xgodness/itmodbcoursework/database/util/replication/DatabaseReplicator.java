package com.xgodness.itmodbcoursework.database.util.replication;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.xgodness.itmodbcoursework.database.ConnectionInitializer;
import com.xgodness.itmodbcoursework.model.Item;
import com.xgodness.itmodbcoursework.model.Recipe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseReplicator {
    private static final String INSERT_ITEM_SQL_TEMPLATE = "INSERT INTO item VALUES (?, ?, ?) ON CONFLICT DO NOTHING;";
    private static final String INSERT_RECIPE_SQL_TEMPLATE = """
            INSERT INTO recipe (id, result_item_id, result_item_cnt, lt_id, ct_id, rt_id, lc_id, cc_id, rc_id, lb_id, cb_id, rb_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT DO NOTHING;
            """;
    private static final String INSERT_RECIPE_CREATED_BY_SQL_TEMPLATE = "INSERT INTO recipe_created_by VALUES (1, ?) ON CONFLICT DO NOTHING;";
    private static final String SELECT_NEXT_RECIPE_ID_SQL = "SELECT nextval('recipe_id_seq');";
    private static final String DELETE_ITEM_SQL_TEMPLATE = "DELETE FROM item WHERE name = ?";
    private final List<Item> itemList;
    private final List<Recipe> recipeList;
    private final List<String> itemToDeleteList;
    private final Connection connection;

    public DatabaseReplicator(@Autowired JsonParser jsonParser,
                              @Autowired ConnectionInitializer connectionInitializer) {
        itemList = jsonParser.getItemList();
        recipeList = jsonParser.getRecipeList();
        itemToDeleteList = jsonParser.getItemNamesToDeleteList();
        connection = connectionInitializer.getConnection();
    }

    @PostConstruct
    private void replicateAll() throws SQLException {
        replicateItems();
        replicateRecipes();
        deleteItemsWithUnresolvedPics();
    }

    private void replicateItems() throws SQLException {
        for (var item : itemList) {
            try (var statement = connection.prepareStatement(INSERT_ITEM_SQL_TEMPLATE)) {
                statement.setLong(1, item.getId());
                statement.setString(2, item.getName());
                statement.setString(3, item.getDisplayName());
                statement.executeUpdate();
            }
        }
    }

    private void replicateRecipes() throws SQLException {
        long[][] craftMatrix;
        long recipeId;
        ResultSet rs;

        for (var recipe : recipeList) {
            try (var statement = connection.createStatement()) {
                rs = statement.executeQuery(SELECT_NEXT_RECIPE_ID_SQL);
                rs.next();
                recipeId = rs.getLong("nextval");
            }

            try (var statement = connection.prepareStatement(INSERT_RECIPE_SQL_TEMPLATE)) {
                statement.setLong(1, recipeId);
                statement.setLong(2, recipe.getResultItemId());
                statement.setInt(3, recipe.getResultItemCnt());

                craftMatrix = recipe.getCraftMatrix();
                for (int i = 0; i < 9; i++)
                    statement.setLong(4 + i, craftMatrix[i / 3][i % 3]);

                statement.executeUpdate();
            }

            try (var statement = connection.prepareStatement(INSERT_RECIPE_CREATED_BY_SQL_TEMPLATE)) {
                statement.setLong(1, recipeId);
                statement.executeUpdate();
            }
        }
    }

    private void deleteItemsWithUnresolvedPics() throws SQLException {
        for (String itemName : itemToDeleteList) {
            try (var statement = connection.prepareStatement(DELETE_ITEM_SQL_TEMPLATE)) {
                statement.setString(1, itemName);
                statement.executeUpdate();
            }
        }
    }
}
