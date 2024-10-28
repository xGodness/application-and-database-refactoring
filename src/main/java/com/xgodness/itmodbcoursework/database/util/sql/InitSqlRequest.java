package com.xgodness.itmodbcoursework.database.util.sql;

public enum InitSqlRequest {
    TABLE_APP_USER("""
            CREATE TABLE IF NOT EXISTS app_user (
                id SERIAL PRIMARY KEY,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                role APP_ROLE NOT NULL
            );
            """),
    TABLE_ITEM("""
            CREATE TABLE IF NOT EXISTS item (
                id BIGINT PRIMARY KEY,
                name TEXT UNIQUE NOT NULL,
                display_name TEXT NOT NULL
            );
            """),
    TABLE_CHEST_CASINO("""
            CREATE TABLE IF NOT EXISTS chest_casino (
                user_id BIGINT REFERENCES app_user (id),
                last_opened_time TIMESTAMP,
                last_received_item_id BIGINT REFERENCES item (id),
                PRIMARY KEY (user_id)
            );
            """),
    TABLE_TRADE_OFFER("""
            CREATE TABLE IF NOT EXISTS trade_offer (
                id SERIAL PRIMARY KEY,
                user_id BIGINT REFERENCES app_user (id)
            );
            """),
    TABLE_TRADE_OFFER_ITEMS_WANTED("""
            CREATE TABLE IF NOT EXISTS trade_offer_items_wanted (
                offer_id BIGINT REFERENCES trade_offer (id),
                item_id BIGINT REFERENCES item (id),
                cnt INTEGER NOT NULL,
                PRIMARY KEY (offer_id, item_id),
                CHECK (cnt > 0)
            );
            """),
    TABLE_TRADE_OFFER_ITEMS_OFFERED("""
            CREATE TABLE IF NOT EXISTS trade_offer_items_offered (
                offer_id BIGINT REFERENCES trade_offer (id),
                item_id BIGINT REFERENCES item (id),
                cnt INTEGER NOT NULL,
                PRIMARY KEY (offer_id, item_id),
                CHECK (cnt > 0)
            );
            """),
    TABLE_RECIPE("""
            CREATE TABLE IF NOT EXISTS recipe (
                id SERIAL PRIMARY KEY,
                result_item_id BIGINT REFERENCES item (id) ON DELETE CASCADE,
                result_item_cnt INTEGER NOT NULL,
                lt_id BIGINT REFERENCES item (id) ON DELETE CASCADE,
                lc_id BIGINT REFERENCES item (id) ON DELETE CASCADE,
                lb_id BIGINT REFERENCES item (id) ON DELETE CASCADE,
                ct_id BIGINT REFERENCES item (id) ON DELETE CASCADE,
                cc_id BIGINT REFERENCES item (id) ON DELETE CASCADE,
                cb_id BIGINT REFERENCES item (id) ON DELETE CASCADE,
                rt_id BIGINT REFERENCES item (id) ON DELETE CASCADE,
                rc_id BIGINT REFERENCES item (id) ON DELETE CASCADE,
                rb_id BIGINT REFERENCES item (id) ON DELETE CASCADE,
                CHECK (result_item_cnt > 0)
            );
            """),
    TABLE_RECIPE_CREATED_BY("""
            CREATE TABLE IF NOT EXISTS recipe_created_by (
                user_id BIGINT REFERENCES app_user (id),
                recipe_id BIGINT REFERENCES recipe (id) ON DELETE CASCADE,
                PRIMARY KEY (user_id, recipe_id)
            );
            """),
    TABLE_USER_ITEMS("""
            CREATE TABLE IF NOT EXISTS user_items (
                user_id BIGINT REFERENCES app_user (id),
                item_id BIGINT REFERENCES item (id),
                cnt INTEGER NOT NULL,
                PRIMARY KEY (user_id, item_id),
                CHECK (cnt > 0)
            );
            """),
    FUNC_OPEN_CHEST("""
            CREATE OR REPLACE FUNCTION open_chest(user_id_arg bigint)\s
                RETURNS void AS
                $BODY$
                    BEGIN
                        INSERT INTO chest_casino (user_id, last_opened_time, last_received_item_id)\s
                        VALUES (
                            user_id_arg, NOW(), (
                                SELECT id FROM item\s
                                WHERE id <> 0\s
                                ORDER BY RANDOM()
                                LIMIT 1
                        ))
                        ON CONFLICT (user_id)
                        DO\s
                            UPDATE SET last_opened_time = NOW(), last_received_item_id = (
                                SELECT id FROM item\s
                                WHERE id <> 0\s
                                ORDER BY RANDOM()
                                LIMIT 1
                            )
                            WHERE chest_casino.user_id = user_id_arg;
                    END;
                $BODY$
                LANGUAGE 'plpgsql' VOLATILE;
            """),
    TRIG_FUNC_OPEN_CHEST("""
            CREATE OR REPLACE FUNCTION open_chest_trigger_function()
                RETURNS trigger AS
                $BODY$
                BEGIN
                    INSERT INTO user_items (user_id, item_id, cnt)
                    VALUES (
                        (
                            SELECT user_id
                            FROM (
                                SELECT last_opened_time, user_id, last_received_item_id
                                FROM chest_casino
                                ORDER BY last_opened_time DESC
                                LIMIT 1
                            ) AS t1
                        ),
                        (
                            SELECT last_received_item_id
                            FROM (
                                SELECT last_opened_time, user_id, last_received_item_id
                                FROM chest_casino
                                ORDER BY last_opened_time DESC
                                LIMIT 1
                            ) AS t2
                        ),
                        1
                    )
                    ON CONFLICT (user_id, item_id)
                    DO
                        UPDATE SET cnt = user_items.cnt + 1
                        WHERE user_items.user_id = (
                            SELECT user_id
                            FROM (
                                SELECT last_opened_time, user_id, last_received_item_id
                                FROM chest_casino
                                ORDER BY last_opened_time DESC
                                LIMIT 1
                            ) AS t3
                        )
                    AND
                    user_items.item_id = (
                        SELECT last_received_item_id
                        FROM (
                            SELECT last_opened_time, user_id, last_received_item_id
                            FROM chest_casino
                            ORDER BY last_opened_time DESC
                            LIMIT 1
                        ) AS t4
                    );
                    RETURN NULL;
                END;
                $BODY$
                LANGUAGE 'plpgsql' VOLATILE;
            """),
    TRIG_OPEN_CHEST("""
            CREATE OR REPLACE TRIGGER open_chest_trigger
                AFTER UPDATE ON chest_casino
                EXECUTE PROCEDURE open_chest_trigger_function();
            """),
    INDEX_RECIPE_RESULT_ITEM("""
            CREATE INDEX IF NOT EXISTS recipe_result_item_idx ON recipe USING hash (result_item_id);
            """),
    INDEX_TRADE_OFFER_ITEMS_WANTED("""
            CREATE INDEX IF NOT EXISTS trade_offer_items_wanted_idx ON trade_offer_items_wanted USING hash (item_id);
            """),
    SUPER_USER("""
            INSERT INTO app_user (username, password, role) VALUES ('superuser', 'superuser', 'ADMIN') ON CONFLICT DO NOTHING;
            """);

    private final String sqlString;

    InitSqlRequest(String sqlString) {
        this.sqlString = sqlString;
    }

    public static String toSqlString(InitSqlRequest pattern) {
        return pattern.sqlString;
    }

}
