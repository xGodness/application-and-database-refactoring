package com.xgodness.itmodbcoursework.database.util.sql;

public enum DropSqlRequest {
    TABLE_USER_ITEMS("""
            DROP TABLE IF EXISTS user_items CASCADE;
            """),
    TABLE_RECIPE_CREATED_BY("""
            DROP TABLE IF EXISTS recipe_created_by CASCADE;
            """),
    TABLE_RECIPE("""
            DROP TABLE IF EXISTS recipe CASCADE;
            """),
    TABLE_TRADE_OFFER_ITEMS_OFFERED("""
            DROP TABLE IF EXISTS trade_offer_items_offered CASCADE;
            """),
    TABLE_TRADE_OFFER_ITEMS_WANTED("""
            DROP TABLE IF EXISTS trade_offer_items_wanted CASCADE;
            """),
    TABLE_TRADE_OFFER("""
            DROP TABLE IF EXISTS trade_offer CASCADE;
            """),
    TABLE_CHEST_CASINO("""
            DROP TABLE IF EXISTS chest_casino CASCADE;
            """),
    TABLE_ITEM("""
            DROP TABLE IF EXISTS item CASCADE;
            """),
    TABLE_APP_USER("""
            DROP TABLE IF EXISTS app_user CASCADE;
            """),
    FUNC_OPEN_CHEST("""
            DROP FUNCTION open_chest;
            """),
    TRIG_FUNC_OPEN_CHEST("""
            DROP FUNCTION open_chest_trigger_function;
            """),
    TRIG_OPEN_CHEST("""
            DROP TRIGGER open_chest_trigger ON chest_casino;
            """),
    INDEX_RECIPE_RESULT_ITEM("""
            DROP INDEX recipe_result_item_idx;
            """),
    INDEX_TRADE_OFFER_ITEMS_WANTED("""
            DROP INDEX trade_offer_items_wanted_idx;
            """),
    TYPE_USER_ROLE("""
            DROP TYPE app_role;
            """);

    private final String sqlString;

    DropSqlRequest(String sqlString) {
        this.sqlString = sqlString;
    }

    public static String toSqlString(DropSqlRequest pattern) {
        return pattern.sqlString;
    }

}
