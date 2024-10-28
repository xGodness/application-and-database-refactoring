package com.xgodness.itmodbcoursework.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xgodness.itmodbcoursework.database.ConnectionInitializer;
import com.xgodness.itmodbcoursework.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RecipeRepository {
    private static final String SAVE_RECIPE = """
            INSERT INTO recipe
            (result_item_id, result_item_cnt, lt_id, ct_id, rt_id, lc_id, cc_id, rc_id, lb_id, cb_id, rb_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
            """;
    private static final String SAVE_CREATED_BY = """
            INSERT INTO recipe_created_by (user_id, recipe_id) VALUES (?, ?);
            """;
    private static final String SELECT_RECIPE = """
            SELECT * FROM recipe
            WHERE lt_id = ? AND ct_id = ? AND rt_id = ? AND lc_id = ? AND cc_id = ? AND rc_id = ? AND lb_id = ? AND cb_id = ? AND rb_id = ?;
            """;
    private static final String SELECT_ALL_RECIPES = """
            SELECT * FROM recipe;
            """;
    private final Connection connection;

    public RecipeRepository(@Autowired ConnectionInitializer connectionInitializer) {
        connection = connectionInitializer.getConnection();
    }

    public void save(long userId, Recipe recipe) throws SQLException {
        try (var statement = connection.prepareStatement(SAVE_RECIPE)) {
            statement.setLong(1, recipe.getResultItemId());
            statement.setLong(2, recipe.getResultItemCnt());
            for (int i = 0; i < 9; i++) {
                statement.setLong(3 + i, recipe.getCraftMatrix()[i / 3][i % 3]);
            }
            statement.executeUpdate();
        }

        long recipeId = findByCraftMatrix(recipe.getCraftMatrix()).getId();

        try (var statement = connection.prepareStatement(SAVE_CREATED_BY)) {
            statement.setLong(1, userId);
            statement.setLong(2, recipeId);
            statement.executeUpdate();
        }
    }

    public Recipe findByCraftMatrix(long[][] craftMatrix) throws SQLException {
        try (var statement = connection.prepareStatement(SELECT_RECIPE)) {
            for (int i = 0; i < 9; i++) {
                statement.setLong(i + 1, craftMatrix[i / 3][i % 3]);
            }
            var rs = statement.executeQuery();
            if (rs.next()) {
                return new Recipe(
                        rs.getLong("id"),
                        rs.getLong("result_item_id"),
                        rs.getInt("result_item_cnt"),
                        craftMatrix
                );
            }
            return null;
        }
    }

    public boolean existsByCraftMatrix(long[][] craftMatrix) throws SQLException {
        return findByCraftMatrix(craftMatrix) != null;
    }

    public List<Recipe> findAll() throws SQLException {
        try (var statement = connection.prepareStatement(SELECT_ALL_RECIPES)) {
            var rs = statement.executeQuery();
            List<Recipe> recipeList = new ArrayList<>();
            while (rs.next()) {
                long[][] craftMatrix = new long[3][3];
                craftMatrix[0][0] = rs.getLong("lt_id");
                craftMatrix[0][1] = rs.getLong("ct_id");
                craftMatrix[0][2] = rs.getLong("rt_id");
                craftMatrix[1][0] = rs.getLong("lc_id");
                craftMatrix[1][1] = rs.getLong("cc_id");
                craftMatrix[1][2] = rs.getLong("rc_id");
                craftMatrix[2][0] = rs.getLong("lb_id");
                craftMatrix[2][1] = rs.getLong("cb_id");
                craftMatrix[2][2] = rs.getLong("rb_id");
                recipeList.add(new Recipe(
                        rs.getLong("id"),
                        rs.getLong("result_item_id"),
                        rs.getInt("result_item_cnt"),
                        craftMatrix
                ));
            }
            return recipeList;
        }
    }
}
