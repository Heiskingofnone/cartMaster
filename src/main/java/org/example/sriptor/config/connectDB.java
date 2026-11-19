package org.example.sriptor.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class connectDB {


private final Connection connection;

private static final Logger logger = LoggerFactory.getLogger(connectDB.class);


    public connectDB() throws SQLException{
        String url = "jdbc:sqlite:pos_store.db";//database url
        logger.debug("Attempting to connect to SQLite database at {}", url);
        try {
            connection = DriverManager.getConnection(url);
            logger.debug("Connected to SQLite database");
        } catch (SQLException e) {
            logger.error("Failed to connect to SQLite database", e);
            throw e;
        }

    }
    public Connection getConnection(){
        return connection;
    }
    public void closeConnection(){
        try{
            if(connection != null){
                connection.close();
                logger.debug("Database connection closed");

            }
        } catch (SQLException e) {
            logger.error("Failed to close database connection", e);
            throw new RuntimeException(e);
        }
    }

    public boolean tableExists(Connection getConnection) throws SQLException{
        DatabaseMetaData meta = getConnection.getMetaData();
        logger.debug("Checking whether the users table exists");
        try(ResultSet rs = meta.getTables(null, null, "USERS", new String[]{"TABLE"})){
            return rs.next();
        }
    }

    public void createTableInstance(Connection getConnection) throws SQLException {
        logger.debug("Ensuring database tables exist");
        String sql = "CREATE TABLE IF NOT EXISTS USERS ("
                    +"user_id TEXT NOT NULL ,"
                    +"first_name TEXT NOT NULL,"
                    +"last_name TEXT NOT NULL,"
                    +"email TEXT NOT NULL,"
                    +"password TEXT NOT NULL,"
                    +"role TEXT NOT NULL CHECK(role IN ('ADMIN', 'CASHIER')),"
                    +"phone_number TEXT," +
                    "is_active INTEGER NOT NULL CHECK(is_active IN(0,1)) DEFAULT 1,"
                    +"created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    +"updated_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP); " +
                "CREATE TABLE IF NOT EXISTS SALE(" +
                "sale_id TEXT PRIMARY KEY DEFAULT (hex(randomblob(4)))," +
                "user_id INTEGER REFERENCES USERS(user_id)," +
                "sale_date TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "total_amount DECIMAL(10,2) NOT NULL ); " +
                "CREATE TABLE IF NOT EXISTS CATEGORY(" +
                "category_id TEXT PRIMARY KEY DEFAULT (hex(randomblob(4)))," +
                "category_name TEXT NOT NULL ); " +
                "CREATE TABLE IF NOT EXISTS PRODUCT(" +
                "product_id TEXT PRIMARY KEY DEFAULT (hex(randomblob(6)))," +
                "product_name TEXT NOT NULL," +
                "category_id TEXT REFERENCES CATEGORY(category_id)," +
                "price DECIMAL(10,2) NOT NULL," +
                "cost_price DECIMAL(10,2) NOT NULL DEFAULT 0," +
                "quantity INTEGER NOT NULL," +
                "image_path TEXT," +
                "sku TEXT," +
                "is_archived INTEGER NOT NULL DEFAULT 0 CHECK(is_archived IN (0,1))); " +
                "CREATE TABLE IF NOT EXISTS SALE_ITEMS(" +
                "sale_item_id TEXT PRIMARY KEY DEFAULT (hex(randomblob(4)))," +
                "sale_id TEXT REFERENCES SALE(sale_id)," +
                "product_id TEXT REFERENCES PRODUCT(product_id)," +
                "quantity INTEGER NOT NULL," +
                "unit_price DECIMAL(10,2) NOT NULL," +
                "cost_price_at_sale DECIMAL(10,2)); " +
                "create table IF NOT EXISTS ORGANIZATIONS \n" +
                "(\n" +
                "    org_name            TEXT not null,\n" +
                "    org_id              TEXT\n" +
                "        constraint ORGANIZATIONS_pk\n" +
                "            primary key,\n" +
                "    owner_user_id       TEXT not null,\n" +
                "    industry            TEXT    default 'RETAIL',\n" +
                "    country             TEXT not null," +
                "    subscription_plan   TEXT    default 'FREE',\n" +
                "    subscription_status TEXT    default 'ACTIVE',\n" +
                "    max_stores_allowed  integer default 1,\n" +
                "    is_active           integer default 1,\n" +
                "    created_at          TEXT    default CURRENT_TIMESTAMP NOT NULL ,\n" +
                "    updated_at          TEXT    default CURRENT_TIMESTAMP NOT NULL \n" +
                "        \n" +
                ");\n" +
                " create table IF NOT EXISTS STORES\n" +
                "                (\n" +
                "                   store_name      TEXT                           not null,\n" +
                "                   store_id        TEXT                        not null\n" +
                "                       constraint STORES_pk\n" +
                "                           primary key ,\n" +
                "                   location        TEXT                           not null,\n" +
                "                   inventory_id TEXT                           ,\n" +
                "                   date_opened     TEXT default CURRENT_TIMESTAMP not null,\n" +
                "                currency        TEXT                           not null CHECK ( currency IN('GHS', 'NGN', 'USD')  )" +
                ");";
        try(Statement stmt = getConnection.createStatement()){
            stmt.executeUpdate(sql);
            ensureColumn(getConnection, "PRODUCT", "cost_price",
                    "ALTER TABLE PRODUCT ADD COLUMN cost_price DECIMAL(10,2) NOT NULL DEFAULT 0");
            ensureColumn(getConnection, "PRODUCT", "sku",
                    "ALTER TABLE PRODUCT ADD COLUMN sku TEXT");
            ensureColumn(getConnection, "PRODUCT", "is_archived",
                    "ALTER TABLE PRODUCT ADD COLUMN is_archived INTEGER NOT NULL DEFAULT 0");
            ensureColumn(getConnection, "SALE_ITEMS", "cost_price_at_sale",
                    "ALTER TABLE SALE_ITEMS ADD COLUMN cost_price_at_sale DECIMAL(10,2)");
            try (Statement statement = getConnection.createStatement()) {
                int updated = statement.executeUpdate(
                        "UPDATE SALE_ITEMS SET cost_price_at_sale = " +
                                "(SELECT cost_price FROM PRODUCT WHERE PRODUCT.product_id = SALE_ITEMS.product_id) " +
                                "WHERE cost_price_at_sale IS NULL");
                if (updated > 0) {
                    logger.info("Backfilled cost snapshots for {} existing sale items", updated);
                }
            }
            try (Statement migration = getConnection.createStatement()) {
                migration.executeUpdate("CREATE TABLE IF NOT EXISTS PRODUCT_CATEGORIES (" +
                        "product_id TEXT NOT NULL REFERENCES PRODUCT(product_id) ON DELETE CASCADE," +
                        "category_id TEXT NOT NULL REFERENCES CATEGORY(category_id) ON DELETE CASCADE," +
                        "PRIMARY KEY(product_id, category_id))");
                migration.executeUpdate("INSERT OR IGNORE INTO PRODUCT_CATEGORIES(product_id, category_id) " +
                        "SELECT product_id, category_id FROM PRODUCT WHERE category_id IS NOT NULL");
            }
            logger.info("Database tables ensured");
        } catch (SQLException e) {
            logger.error("Failed to ensure database tables exist", e);
            throw e;
        }
    }

    private void ensureColumn(Connection connection, String table, String column, String alterSql)
            throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet columns = statement.executeQuery("PRAGMA table_info(" + table + ")")) {
            while (columns.next()) {
                if (column.equalsIgnoreCase(columns.getString("name"))) {
                    return;
                }
            }
        }
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(alterSql);
            logger.info("Added missing {}.{} column", table, column);
        }
    }
}
