package com.xgodness.itmodbcoursework.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xgodness.itmodbcoursework.database.ConnectionInitializer;
import com.xgodness.itmodbcoursework.model.Item;
import com.xgodness.itmodbcoursework.model.ItemPack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.xgodness.itmodbcoursework.repository.RepositoryUtils.extractItem;
import static com.xgodness.itmodbcoursework.repository.RepositoryUtils.extractItemPack;

@Repository
public class ItemRepository {
    private static final String SELECT_ITEM = """
            SELECT * FROM item WHERE id = ?;
            """;
    private static final String SELECT_ALL_ITEMS = """
            SELECT * FROM item;
            """;
    private static final String SELECT_INVENTORY = """
            SELECT * FROM user_items INNER JOIN item ON user_items.item_id = item.id WHERE user_id = ?;
            """;
    private static final String DELETE_ITEM_FROM_INVENTORY = """
            DELETE FROM user_items WHERE user_id = ? AND item_id = ? AND cnt = ?;
            """;
    private static final String SUBSTRACT_ITEM_CNT_FROM_INVENTORY = """
            UPDATE user_items SET cnt = cnt - ? WHERE user_id = ? AND item_id = ?;
            """;
    private static final String SAVE_ITEM_TO_INVENTORY = """
            INSERT INTO user_items (user_id, item_id, cnt) VALUES (?, ?, ?)
            ON CONFLICT(user_id, item_id) DO UPDATE SET cnt = user_items.cnt + ? WHERE user_items.user_id = ? AND user_items.item_id = ?;
            """;
    private final Connection connection;

    public ItemRepository(@Autowired ConnectionInitializer connectionInitializer) {
        connection = connectionInitializer.getConnection();
    }

    public Item findById(long itemId) throws SQLException {
        try (var statement = connection.prepareStatement(SELECT_ITEM)) {
            statement.setLong(1, itemId);
            var rs = statement.executeQuery();
            return extractItem(rs);
        }
    }

    public boolean existsById(long itemId) throws SQLException {
        if (itemId < 0) return false;
        try (var statement = connection.prepareStatement(SELECT_ITEM)) {
            statement.setLong(1, itemId);
            var rs = statement.executeQuery();
            return rs.next();
        }
    }

    public List<Item> findAll() throws SQLException {
        try (var statement = connection.prepareStatement(SELECT_ALL_ITEMS)) {
            var rs = statement.executeQuery();
            List<Item> itemList = new ArrayList<>();
            Item item = extractItem(rs);
            while (item != null) {
                itemList.add(item);
                item = extractItem(rs);
            }
            return itemList;
        }
    }

    public List<ItemPack> getInventory(long userId) throws SQLException {
        try (var statement = connection.prepareStatement(SELECT_INVENTORY)) {
            statement.setLong(1, userId);
            var rs = statement.executeQuery();
            List<ItemPack> itemPackList = new ArrayList<>();
            ItemPack itemPack = extractItemPack(rs);
            while (itemPack != null) {
                itemPackList.add(itemPack);
                itemPack = extractItemPack(rs);
            }
            return itemPackList;
        }
    }

    public void deleteItemFromInventory(long userId, long itemId, int count) throws SQLException {
        try (var statement = connection.prepareStatement(DELETE_ITEM_FROM_INVENTORY)) {
            statement.setLong(1, userId);
            statement.setLong(2, itemId);
            statement.setInt(3, count);
            statement.executeUpdate();
        }
        try (var statement = connection.prepareStatement(SUBSTRACT_ITEM_CNT_FROM_INVENTORY)) {
            statement.setInt(1, count);
            statement.setLong(2, userId);
            statement.setLong(3, itemId);
            statement.executeUpdate();
        }
    }

    public void saveItemToInventory(long userId, long itemId, int count) throws SQLException {
        try (var statement = connection.prepareStatement(SAVE_ITEM_TO_INVENTORY)) {
            statement.setLong(1, userId);
            statement.setLong(2, itemId);
            statement.setInt(3, count);
            statement.setInt(4, count);
            statement.setLong(5, userId);
            statement.setLong(6, itemId);
            statement.executeUpdate();
        }
    }


}
