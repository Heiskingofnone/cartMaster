package org.example.sriptor.dao;

import org.example.sriptor.config.connectDB;
import org.example.sriptor.dto.CategoryDTO;
import org.example.sriptor.models.product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class productDAO {
    private static final Logger logger = LoggerFactory.getLogger(productDAO.class);

    private Connection openConnection() throws SQLException {
        return new connectDB().getConnection();
    }

    public List<product> getActiveProducts() throws SQLException {
        String sql = "SELECT p.product_id, p.product_name, p.category_id, " +
                "GROUP_CONCAT(c.category_name, char(31)) AS category_names, " +
                "GROUP_CONCAT(pc.category_id, ',') AS category_ids, p.price, " +
                "p.cost_price, p.quantity, p.image_path, p.sku " +
                "FROM PRODUCT p " +
                "LEFT JOIN PRODUCT_CATEGORIES pc ON pc.product_id = p.product_id " +
                "LEFT JOIN CATEGORY c ON c.category_id = pc.category_id " +
                "WHERE p.is_archived = 0 " +
                "GROUP BY p.product_id " +
                "ORDER BY p.product_name COLLATE NOCASE";
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            List<product> products = new ArrayList<>();
            while (results.next()) {
                product item = new product(
                        results.getString("product_name"),
                        results.getDouble("price"),
                        results.getDouble("cost_price"),
                        results.getString("product_id"),
                        results.getString("image_path"),
                        results.getString("category_id"),
                        results.getString("category_names"),
                        results.getString("sku"),
                        results.getInt("quantity"));
                String categoryIds = results.getString("category_ids");
                if (categoryIds != null && !categoryIds.isBlank()) {
                    item.setCategoryIds(List.of(categoryIds.split(",")));
                }
                String categoryNames = results.getString("category_names");
                if (categoryNames != null && !categoryNames.isBlank()) {
                    item.setCategoryNames(List.of(categoryNames.split("\u001f", -1)));
                }
                products.add(item);
            }
            logger.debug("Retrieved {} active inventory products", products.size());
            return products;
        } catch (SQLException e) {
            logger.error("Failed to retrieve active inventory products", e);
            throw e;
        }
    }

    public List<CategoryDTO> getCategories() throws SQLException {
        String sql = "SELECT category_id, category_name FROM CATEGORY ORDER BY category_name COLLATE NOCASE";
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            List<CategoryDTO> categories = new ArrayList<>();
            while (results.next()) {
                categories.add(new CategoryDTO(
                        results.getString("category_id"),
                        results.getString("category_name")));
            }
            return categories;
        } catch (SQLException e) {
            logger.error("Failed to retrieve product categories", e);
            throw e;
        }
    }

    public void addCategory(String categoryName) throws SQLException {
        String sql = "INSERT INTO CATEGORY(category_id, category_name) VALUES (?, ?)";
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2, categoryName);
            statement.executeUpdate();
            logger.info("Added inventory category");
        } catch (SQLException e) {
            logger.error("Failed to add inventory category", e);
            throw e;
        }
    }

    public void renameCategory(String categoryId, String categoryName) throws SQLException {
        String sql = "UPDATE CATEGORY SET category_name = ? WHERE category_id = ?";
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, categoryName);
            statement.setString(2, categoryId);
            if (statement.executeUpdate() == 0) {
                throw new SQLException("Category no longer exists");
            }
            logger.info("Renamed inventory category");
        } catch (SQLException e) {
            logger.error("Failed to rename inventory category", e);
            throw e;
        }
    }

    public void addProduct(product item, List<String> categoryNames) throws SQLException {
        String sql = "INSERT INTO PRODUCT(product_id, product_name, category_id, price, cost_price, quantity, " +
                "image_path, sku) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String productId = UUID.randomUUID().toString();
        try (Connection connection = openConnection()) {
            connection.setAutoCommit(false);
            try {
                List<CategoryDTO> categories = resolveCategories(connection, categoryNames);
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, productId);
                    statement.setString(2, item.getProductName());
                    statement.setString(3, categories.get(0).getId());
                    statement.setDouble(4, item.getProductPrice());
                    statement.setDouble(5, item.getProductCostPrice());
                    statement.setInt(6, item.getProductQuantity());
                    statement.setString(7, item.getImagePath());
                    statement.setString(8, item.getSku());
                    statement.executeUpdate();
                }
                try (PreparedStatement categoryStatement = connection.prepareStatement(
                        "INSERT INTO PRODUCT_CATEGORIES(product_id, category_id) VALUES (?, ?)")) {
                    for (CategoryDTO category : categories) {
                        categoryStatement.setString(1, productId);
                        categoryStatement.setString(2, category.getId());
                        categoryStatement.addBatch();
                    }
                    categoryStatement.executeBatch();
                }
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            }
            logger.info("Added product to inventory");
        } catch (SQLException e) {
            logger.error("Failed to add product to inventory", e);
            throw e;
        }
    }

    public void updateProduct(product item, List<String> categoryNames) throws SQLException {
            String updateSql = "UPDATE PRODUCT SET product_name = ?, category_id = ?, price = ?, cost_price = ?, " +
                    "sku = ? WHERE product_id = ? AND is_archived = 0";
            try (Connection connection = openConnection()) {
                connection.setAutoCommit(false);
                try {
                    List<CategoryDTO> categories = resolveCategories(connection, categoryNames);
                    try (PreparedStatement statement = connection.prepareStatement(updateSql)) {
                        statement.setString(1, item.getProductName());
                        statement.setString(2, categories.get(0).getId());
                        statement.setDouble(3, item.getProductPrice());
                        statement.setDouble(4, item.getProductCostPrice());
                        statement.setString(5, item.getSku());
                        statement.setString(6, item.getProductId());
                        if (statement.executeUpdate() == 0) {
                            throw new SQLException("Product no longer exists or is archived");
                        }
                    }
                    try (PreparedStatement deleteCategories = connection.prepareStatement(
                            "DELETE FROM PRODUCT_CATEGORIES WHERE product_id = ?")) {
                        deleteCategories.setString(1, item.getProductId());
                        deleteCategories.executeUpdate();
                    }
                    try (PreparedStatement insertCategory = connection.prepareStatement(
                            "INSERT INTO PRODUCT_CATEGORIES(product_id, category_id) VALUES (?, ?)")) {
                        for (CategoryDTO category : categories) {
                            insertCategory.setString(1, item.getProductId());
                            insertCategory.setString(2, category.getId());
                            insertCategory.addBatch();
                        }
                        insertCategory.executeBatch();
                    }
                    connection.commit();
                    logger.info("Updated inventory product");
                } catch (SQLException e) {
                    try {
                        connection.rollback();
                    } catch (SQLException rollbackError) {
                        e.addSuppressed(rollbackError);
                    }
                    throw e;
                }
            } catch (SQLException e) {
                logger.error("Failed to update inventory product", e);
                throw e;
        }
    }

    private List<CategoryDTO> resolveCategories(Connection connection, List<String> categoryNames)
                throws SQLException {
        List<CategoryDTO> categories = new ArrayList<>();
        String selectSql = "SELECT category_id, category_name FROM CATEGORY WHERE category_name = ? COLLATE NOCASE";
        String insertSql = "INSERT INTO CATEGORY(category_id, category_name) VALUES (?, ?)";
        for (String name : categoryNames) {
                try (PreparedStatement select = connection.prepareStatement(selectSql)) {
                    select.setString(1, name);
                    try (ResultSet result = select.executeQuery()) {
                        if (result.next()) {
                            categories.add(new CategoryDTO(
                                    result.getString("category_id"), result.getString("category_name")));
                            continue;
                        }
                    }
                }
                String id = UUID.randomUUID().toString();
                try (PreparedStatement insert = connection.prepareStatement(insertSql)) {
                    insert.setString(1, id);
                    insert.setString(2, name);
                    insert.executeUpdate();
                }
                categories.add(new CategoryDTO(id, name));
                logger.info("Created category while saving product");
        }
        return categories;
    }
}
