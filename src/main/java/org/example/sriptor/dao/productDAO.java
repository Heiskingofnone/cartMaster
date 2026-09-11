package org.example.sriptor.dao;

import org.example.sriptor.config.connectDB;
import org.example.sriptor.models.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class productDAO {
    private String sql;
    private connectDB databaseConnection;
    public List<product> getAllProducts(){
        List<product> productList = new ArrayList<>();
        sql = "SELECT product_id, product_name, category_id, price, quantity, image_path FROM PRODUCT";
        try(Connection conn = databaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    String productId = rs.getString("product_id");
                    String productName = rs.getString("product_name");
                    String categoryId = rs.getString("category_id");
                    Double price = rs.getDouble("price");
                    int quantity = rs.getInt("quantity");
                    String imagePath = rs.getString("image_path");
                    productList.add(new product(productName, price, productId, imagePath, categoryId));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return productList;
    }

    public Boolean addProduct( product product){
        sql = "INSERT INTO  PRODUCT(product_name, category_id, price, quantity, image_path) VALUES (?,?,?,?,?)";
        try (Connection conn = databaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getCategoryId());
            pstmt.setDouble(3, product.getProductPrice());
            pstmt.setInt(4, product.getProductQuantity());
            pstmt.setString(5, product.getImagePath());

            return pstmt.executeUpdate() > 0;
            //returns the number of tables updated, which results in a true statement since booleans are only false
            //when return is 0 and true otherwise
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public product getProductById(String id){
        sql = "SELECT product_id, product_name, category_id, price, quantity, image_path FROM PRODUCT " +
                "WHERE product_id = ?";
        try(Connection conn = databaseConnection.getConnection();
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

                    return new product(productName, price, productId, imagePath, categoryId);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
