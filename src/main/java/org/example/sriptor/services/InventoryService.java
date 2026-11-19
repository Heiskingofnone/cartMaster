package org.example.sriptor.services;

import org.example.sriptor.dao.productDAO;
import org.example.sriptor.dto.CategoryDTO;
import org.example.sriptor.models.product;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InventoryService {
    private final productDAO productDAO = new productDAO();

    public List<product> getActiveProducts() throws SQLException {
        return productDAO.getActiveProducts();
    }

    public List<CategoryDTO> getCategories() throws SQLException {
        return productDAO.getCategories();
    }

    public void addCategory(String name) throws SQLException {
        String normalizedName = validateNewCategoryName(name);
        productDAO.addCategory(normalizedName);
    }

    public void renameCategory(CategoryDTO category, String name) throws SQLException {
        if (category == null) {
            throw new IllegalArgumentException("Select a category");
        }
        String normalizedName = requireText(name, "Category name");
        boolean alreadyExists = productDAO.getCategories().stream()
                .anyMatch(existing -> !existing.getId().equals(category.getId())
                        && existing.getName().equalsIgnoreCase(normalizedName));
        if (alreadyExists) {
            throw new IllegalArgumentException("A category with this name already exists");
        }
        productDAO.renameCategory(category.getId(), normalizedName);
    }

    public void addProduct(String name, List<String> categories, double costPrice, double sellingPrice,
                           String sku, int startingQuantity) throws SQLException {
        List<String> normalizedCategories = normalizeCategoryNames(categories);
        String normalizedName = validateProductFields(name, normalizedCategories, costPrice, sellingPrice,
                startingQuantity);
        productDAO.addProduct(new product(
                normalizedName, sellingPrice, costPrice, null, null, null,
                String.join(", ", normalizedCategories), normalizeOptionalText(sku), startingQuantity),
                normalizedCategories);
    }

    public void updateProduct(product item, String name, List<String> categories, double costPrice,
                              double sellingPrice, String sku) throws SQLException {
        if (item == null || item.getProductId() == null) {
            throw new IllegalArgumentException("Select a product to edit");
        }
        String normalizedName = validateProductFields(name, categories, costPrice, sellingPrice,
                item.getProductQuantity());
        List<String> normalizedCategories = normalizeCategoryNames(categories);
        String categoryNames = String.join(", ", normalizedCategories);
        product updatedProduct = new product(
                normalizedName, sellingPrice, costPrice, item.getProductId(), item.getImagePath(),
                item.getCategoryId(), categoryNames, normalizeOptionalText(sku), item.getProductQuantity());
        productDAO.updateProduct(updatedProduct, normalizedCategories);
        item.setProductName(normalizedName);
        item.setCategoryNames(normalizedCategories);
        item.setProductCostPrice(costPrice);
        item.setProductPrice(sellingPrice);
        item.setSku(updatedProduct.getSku());
    }

    private String validateProductFields(String name, List<String> categories,
                                         double costPrice, double sellingPrice, int quantity) {
        String normalizedName = requireText(name, "Product name");
        normalizeCategoryNames(categories);
        if (!Double.isFinite(costPrice) || costPrice < 0 || !Double.isFinite(sellingPrice) || sellingPrice < 0) {
            throw new IllegalArgumentException("Prices must be valid non-negative amounts");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        return normalizedName;
    }

    private List<String> normalizeCategoryNames(List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            throw new IllegalArgumentException("Add at least one category");
        }
        Set<String> names = new HashSet<>();
        List<String> normalizedNames = new java.util.ArrayList<>();
        for (String category : categories) {
            String normalized = requireText(category, "Category name");
            if (!names.add(normalized.toLowerCase(java.util.Locale.ROOT))) {
                throw new IllegalArgumentException("Category names must be unique");
            }
            normalizedNames.add(normalized);
        }
        return List.copyOf(normalizedNames);
    }

    private String validateNewCategoryName(String name) throws SQLException {
        String normalizedName = requireText(name, "Category name");
        boolean alreadyExists = productDAO.getCategories().stream()
                .anyMatch(category -> category.getName().equalsIgnoreCase(normalizedName));
        if (alreadyExists) {
            throw new IllegalArgumentException("A category with this name already exists");
        }
        return normalizedName;
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return value.trim();
    }

    private String normalizeOptionalText(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
