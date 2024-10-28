package com.xgodness.itmodbcoursework.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xgodness.itmodbcoursework.database.ConnectionInitializer;
import com.xgodness.itmodbcoursework.model.ItemPack;
import com.xgodness.itmodbcoursework.model.Offer;
import com.xgodness.itmodbcoursework.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.xgodness.itmodbcoursework.repository.RepositoryUtils.extractItemPackList;

@Repository
public class OfferRepository {
    private static final String SELECT_NEXT_TRADE_OFFER_ID_SEQ = """
            SELECT nextval('trade_offer_id_seq');
            """;
    private static final String SAVE_OFFER = """
            INSERT INTO trade_offer (id, user_id) VALUES (?, ?);
            """;
    private static final String SAVE_ITEM_OFFERED = """
            INSERT INTO trade_offer_items_offered (offer_id, item_id, cnt) VALUES (?, ?, ?);
            """;
    private static final String SAVE_ITEM_WANTED = """
            INSERT INTO trade_offer_items_wanted (offer_id, item_id, cnt) VALUES (?, ?, ?);
            """;
    private static final String DELETE_OFFER = """
            DELETE FROM trade_offer WHERE id = ?;
            """;
    private static final String DELETE_ITEMS_OFFERED = """
            DELETE FROM trade_offer_items_offered WHERE offer_id = ?;
            """;
    private static final String DELETE_ITEMS_WANTED = """
            DELETE FROM trade_offer_items_wanted WHERE offer_id = ?;
            """;
    private static final String SELECT_OFFER_BY_ID = """
            SELECT trade_offer.id, user_id, username FROM
            trade_offer INNER JOIN app_user ON app_user.id = trade_offer.user_id
            WHERE trade_offer.id = ?;
            """;
    private static final String SELECT_OFFER_ITEMS_WANTED = """
            SELECT * FROM trade_offer_items_wanted
            INNER JOIN item ON trade_offer_items_wanted.item_id = item.id WHERE offer_id = ?;
            """;
    private static final String SELECT_OFFER_ITEMS_OFFERED = """
            SELECT * FROM trade_offer_items_offered
            INNER JOIN item ON trade_offer_items_offered.item_id = item.id WHERE offer_id = ?;
            """;
    private static final String SELECT_ALL_OFFERS = """
            SELECT * FROM trade_offer;
            """;
    private final Connection connection;

    public OfferRepository(@Autowired ConnectionInitializer connectionInitializer) {
        connection = connectionInitializer.getConnection();
    }

    public void save(Offer offer, long userId) throws SQLException {
        long offerId;

        try (var statement = connection.createStatement()) {
            var rs = statement.executeQuery(SELECT_NEXT_TRADE_OFFER_ID_SEQ);
            rs.next();
            offerId = rs.getLong("nextval");
        }

        try (var statement = connection.prepareStatement(SAVE_OFFER)) {
            statement.setLong(1, offerId);
            statement.setLong(2, userId);
            statement.executeUpdate();
        }

        for (var itemPack : offer.getItemsOffered()) {
            try (var statement = connection.prepareStatement(SAVE_ITEM_OFFERED)) {
                statement.setLong(1, offerId);
                statement.setLong(2, itemPack.getItem().getId());
                statement.setInt(3, itemPack.getCount());
                statement.executeUpdate();
            }
        }

        for (var itemPack : offer.getItemsWanted()) {
            try (var statement = connection.prepareStatement(SAVE_ITEM_WANTED)) {
                statement.setLong(1, offerId);
                statement.setLong(2, itemPack.getItem().getId());
                statement.setInt(3, itemPack.getCount());
                statement.executeUpdate();
            }
        }
    }

    public void delete(long offerId) throws SQLException {
        try (var statement = connection.prepareStatement(DELETE_ITEMS_OFFERED)) {
            statement.setLong(1, offerId);
            statement.executeUpdate();
        }

        try (var statement = connection.prepareStatement(DELETE_ITEMS_WANTED)) {
            statement.setLong(1, offerId);
            statement.executeUpdate();
        }

        try (var statement = connection.prepareStatement(DELETE_OFFER)) {
            statement.setLong(1, offerId);
            statement.executeUpdate();
        }
    }

    public boolean existsById(long offerId) throws SQLException {
        try (var statement = connection.prepareStatement(SELECT_OFFER_BY_ID)) {
            statement.setLong(1, offerId);
            var rs = statement.executeQuery();
            return rs.next();
        }
    }

    public Offer findById(long offerId) throws SQLException {
        User user = new User();
        List<ItemPack> itemsOffered;
        List<ItemPack> itemsWanted;

        try (var statement = connection.prepareStatement(SELECT_OFFER_ITEMS_OFFERED)) {
            statement.setLong(1, offerId);
            var rs = statement.executeQuery();
            itemsOffered = extractItemPackList(rs);
        }

        try (var statement = connection.prepareStatement(SELECT_OFFER_ITEMS_WANTED)) {
            statement.setLong(1, offerId);
            var rs = statement.executeQuery();
            itemsWanted = extractItemPackList(rs);
        }

        try (var statement = connection.prepareStatement(SELECT_OFFER_BY_ID)) {
            statement.setLong(1, offerId);
            var rs = statement.executeQuery();
            if (rs.next()) {
                user.setUsername(rs.getString("username"));
            }
        }

        return new Offer(offerId, user, itemsWanted, itemsOffered);
    }

    public List<Offer> findAll() throws SQLException {
        List<Offer> offerList = new ArrayList<>();

        try (var statement = connection.prepareStatement(SELECT_ALL_OFFERS)) {
            var rs = statement.executeQuery();
            while (rs.next()) {
                Offer offer = new Offer();
                offer.setId(rs.getLong("id"));
                offer.setUser(new User(rs.getLong("user_id"), null, null));

                try (var innerStatement = connection.prepareStatement(SELECT_OFFER_ITEMS_OFFERED)) {
                    innerStatement.setLong(1, offer.getId());
                    var innerRs = innerStatement.executeQuery();
                    offer.setItemsOffered(extractItemPackList(innerRs));
                }

                try (var innerStatement = connection.prepareStatement(SELECT_OFFER_ITEMS_WANTED)) {
                    innerStatement.setLong(1, offer.getId());
                    var innerRs = innerStatement.executeQuery();
                    offer.setItemsWanted(extractItemPackList(innerRs));
                }

                offerList.add(offer);
            }
        }

        return offerList;
    }
}
