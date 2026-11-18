package org.example.sriptor.dao;

import org.example.sriptor.config.connectDB;
import org.example.sriptor.models.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class productDAO {
    private static final Logger logger = LoggerFactory.getLogger(productDAO.class);

    private Connection openConnection() throws SQLException {
        return new connectDB().getConnection();
    }

    public List<product> getAllProducts(){
        List<product> productList = new ArrayList<>();
        String sql = "SELECT product_id, product_name, category_id, price, quantity, image_path FROM PRODUCT";
        try(Connection conn = openConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    String productId = rs.getString("product_id");
                    String productName = rs.getString("product_name");
                    String categoryId = rs.getString("category_id");
                    Double price = rs.getDouble("price");
                    String imagePath = rs.getString("image_path");
                    productList.add(new product(productName, price, productId, imagePath, categoryId));
                }
            }
            logger.debug("Retrieved {} products", productList.size());
        }catch (SQLException e){
            logger.error("Failed to retrieve products", e);
            throw new IllegalStateException("Could not retrieve products", e);
        }
        return productList;
    }

    public Boolean addProduct( product product){
        String sql = "INSERT INTO  PRODUCT(product_name, category_id, price, quantity, image_path) VALUES (?,?,?,?,?)";
        try (Connection conn = openConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getCategoryId());
            pstmt.setDouble(3, product.getProductPrice());
            pstmt.setInt(4, product.getProductQuantity());
            pstmt.setString(5, product.getImagePath());

            boolean inserted = pstmt.executeUpdate() > 0;
            if (inserted) {
                logger.info("Added product to inventory");
            } else {
                logger.warn("Product insert completed without adding a row");
            }
            return inserted;
            //returns the number of tables updated, which results in a true statement since booleans are only false
            //when return is 0 and true otherwise
        }
        catch (SQLException e){
            logger.error("Failed to add product to inventory", e);
            return false;
        }
    }
    public product getProductById(String id){
        String sql = "SELECT product_id, product_name, category_id, price, quantity, image_path FROM PRODUCT " +
                "WHERE product_id = ?";
        try(Connection conn = openConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);

            try(ResultSet rs = pstmt.executeQuery()){
                //if there is a returned statement, execute code. else the entry isn't in database
                if(rs.next()){
                    String productId = rs.getString("product_id");
                    String productName = rs.getString("product_name");
                    String categoryId = rs.getString("category_id");
                    Double price = rs.getDouble("price");
                    int quantity = rs.getInt("quantity");
                    String imagePath = rs.getString("image_path");

                    logger.debug("Found product by identifier");
                    return new product(productName, price, productId, imagePath, categoryId);
                }
            }
        } catch (SQLException e){
            logger.error("Failed to retrieve product by identifier", e);
            throw new IllegalStateException("Could not retrieve product", e);
        }
        logger.debug("No product found for the requested identifier");
        return null;
    }
}
