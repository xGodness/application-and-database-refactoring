package com.xgodness.itmodbcoursework.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xgodness.itmodbcoursework.model.Item;
import com.xgodness.itmodbcoursework.model.ItemPack;

public class RepositoryUtils {

    private RepositoryUtils() { }

    public static Item extractItem(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new Item(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("display_name")
            );
        }
        return null;
    }

    public static ItemPack extractItemPack(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new ItemPack(
                    new Item(
                            rs.getLong("item_id"),
                            rs.getString("name"),
                            rs.getString("display_name")
                    ),
                    rs.getInt("cnt")
            );
        }
        return null;
    }

    public static List<ItemPack> extractItemPackList(ResultSet rs) throws SQLException {
        List<ItemPack> result = new ArrayList<>();
        ItemPack itemPack = extractItemPack(rs);
        while (itemPack != null) {
            result.add(itemPack);
            itemPack = extractItemPack(rs);
        }
        return result;
    }
}
