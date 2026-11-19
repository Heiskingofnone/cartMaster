package org.example.sriptor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import org.example.sriptor.dto.CategoryDTO;
import org.example.sriptor.models.product;
import org.example.sriptor.services.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class inventoryController {
    private static final Logger logger = LoggerFactory.getLogger(inventoryController.class);
    private final InventoryService inventoryService = new InventoryService();
    private final ObservableList<product> products = FXCollections.observableArrayList();
    private final FilteredList<product> filteredProducts = new FilteredList<>(products, item -> true);

    @FXML private TextField searchField;
    @FXML private TableView<product> productTable;
    @FXML private TableColumn<product, String> nameColumn;
    @FXML private TableColumn<product, String> categoryColumn;
    @FXML private TableColumn<product, String> quantityColumn;
    @FXML private TableColumn<product, String> priceColumn;
    @FXML private TableColumn<product, String> statusColumn;
    @FXML private TableColumn<product, Void> actionsColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cell ->
                new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue().getProductName()));
        categoryColumn.setCellValueFactory(cell ->
                new javafx.beans.property.ReadOnlyStringWrapper(
                        Objects.requireNonNullElse(cell.getValue().getCategoryName(), "Uncategorized")));
        quantityColumn.setCellValueFactory(cell ->
                new javafx.beans.property.ReadOnlyStringWrapper(
                        Integer.toString(cell.getValue().getProductQuantity())));
        priceColumn.setCellValueFactory(cell ->
                new javafx.beans.property.ReadOnlyStringWrapper(
                        String.format(Locale.US, "GHS %.2f", cell.getValue().getProductPrice())));
        statusColumn.setCellValueFactory(cell ->
                new javafx.beans.property.ReadOnlyStringWrapper(
                        cell.getValue().getProductQuantity() == 0 ? "Out of stock" : "In stock"));
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final javafx.scene.control.Button editButton = new javafx.scene.control.Button("Edit");

            {
                editButton.getStyleClass().add("secondary-button");
                editButton.setOnAction(event -> editProduct(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : editButton);
            }
        });
        productTable.setPlaceholder(new Label("No products found. Add a product to get started."));
        productTable.setItems(filteredProducts);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterProducts(newValue));
        refreshProducts();
    }

    @FXML
    private void addProduct() {
        showProductDialog(null);
    }

    private void editProduct(product item) {
        showProductDialog(item);
    }

    private void showProductDialog(product existingProduct) {
        try {
            List<CategoryDTO> categories = inventoryService.getCategories();

            Dialog<ButtonType> dialog = new Dialog<>();
            boolean editing = existingProduct != null;
            dialog.setTitle(editing ? "Edit Product" : "Add Product");
            dialog.setHeaderText(editing ? "Update product details" : "Enter the product details");
            ButtonType saveButton = new ButtonType(editing ? "Save Changes" : "Add Product",
                    ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

            TextField nameField = new TextField();
            nameField.setPromptText("Product name");
            if (editing) {
                nameField.setText(existingProduct.getProductName());
            }
            CategoryPicker categoryPicker = new CategoryPicker(
                    categories, editing ? existingProduct.getCategoryNames() : List.of());
            TextField costField = decimalField("0.00");
            TextField priceField = decimalField("0.00");
            if (editing) {
                costField.setText(String.format(Locale.ROOT, "%.2f", existingProduct.getProductCostPrice()));
                priceField.setText(String.format(Locale.ROOT, "%.2f", existingProduct.getProductPrice()));
            }
            TextField skuField = new TextField();
            skuField.setPromptText("Optional");
            if (editing) {
                skuField.setText(Objects.requireNonNullElse(existingProduct.getSku(), ""));
            }
            TextField quantityField = editing ? null : integerField("0");

            GridPane form = new GridPane();
            form.setHgap(12);
            form.setVgap(10);
            form.setPadding(new Insets(12));
            addFormRow(form, 0, "Product name", nameField);
            addFormRow(form, 1, "Categories", categoryPicker);
            addFormRow(form, 2, "Cost price (GHS)", costField);
            addFormRow(form, 3, "Selling price (GHS)", priceField);
            addFormRow(form, 4, "SKU / barcode", skuField);
            if (!editing) {
                addFormRow(form, 5, "Starting stock", quantityField);
            }
            GridPane.setHgrow(nameField, Priority.ALWAYS);
            dialog.getDialogPane().setContent(form);

            dialog.getDialogPane().lookupButton(saveButton).addEventFilter(
                    javafx.event.ActionEvent.ACTION, event -> {
                        try {
                            categoryPicker.addTypedCategory();
                            List<String> selectedCategories = categoryPicker.getSelectedNames();
                            double cost = Double.parseDouble(costField.getText());
                            double price = Double.parseDouble(priceField.getText());
                            if (editing) {
                                inventoryService.updateProduct(existingProduct, nameField.getText(),
                                        selectedCategories, cost, price, skuField.getText());
                            } else {
                                inventoryService.addProduct(nameField.getText(), selectedCategories,
                                        cost, price, skuField.getText(),
                                        quantityField.getText().isBlank()
                                                ? 0 : Integer.parseInt(quantityField.getText()));
                            }
                        } catch (NumberFormatException e) {
                            event.consume();
                            showError("Enter valid numeric values for prices and starting stock.");
                        } catch (IllegalArgumentException e) {
                            event.consume();
                            showError(e.getMessage());
                        } catch (SQLException e) {
                            event.consume();
                            logger.error("Failed to {} product",
                                    editing ? "update" : "add", e);
                            showError(editing ? "The product could not be updated." : "The product could not be saved.");
                        }
                    });

            if (dialog.showAndWait().filter(saveButton::equals).isPresent()) {
                refreshProducts();
            }
        } catch (SQLException e) {
            logger.error("Failed to load product categories", e);
            showError("Product categories could not be loaded.");
        }
    }

    @FXML
    private void manageCategories() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Manage Categories");
        dialog.setHeaderText("Add or rename product categories");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        ListView<CategoryDTO> categoryList = new ListView<>();
        categoryList.setPrefHeight(260);
        ButtonType addButton = new ButtonType("Add Category", ButtonBar.ButtonData.OTHER);
        ButtonType renameButton = new ButtonType("Rename Selected", ButtonBar.ButtonData.OTHER);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, renameButton);
        dialog.getDialogPane().setContent(categoryList);
        dialog.getDialogPane().lookupButton(addButton).addEventFilter(
                javafx.event.ActionEvent.ACTION, event -> {
            event.consume();
            try {
                TextInputDialog nameDialog = new TextInputDialog();
                nameDialog.setTitle("Add Category");
                nameDialog.setHeaderText("Create a product category");
                nameDialog.setContentText("Category name:");
                var result = nameDialog.showAndWait();
                if (result.isPresent()) {
                    inventoryService.addCategory(result.get());
                    categoryList.getItems().setAll(inventoryService.getCategories());
                }
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            } catch (SQLException e) {
                logger.error("Failed to add product category", e);
                showError("The category could not be saved.");
            }
        });
        dialog.getDialogPane().lookupButton(renameButton).addEventFilter(
                javafx.event.ActionEvent.ACTION, event -> {
            event.consume();
            CategoryDTO selected = categoryList.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Select a category to rename.");
                return;
            }
            TextInputDialog nameDialog = new TextInputDialog(selected.getName());
            nameDialog.setTitle("Rename Category");
            nameDialog.setHeaderText("Rename " + selected.getName());
            nameDialog.setContentText("Category name:");
            var result = nameDialog.showAndWait();
            if (result.isPresent()) {
                try {
                    inventoryService.renameCategory(selected, result.get());
                    categoryList.getItems().setAll(inventoryService.getCategories());
                    refreshProducts();
                } catch (IllegalArgumentException e) {
                    showError(e.getMessage());
                } catch (SQLException e) {
                    logger.error("Failed to rename product category", e);
                    showError("The category could not be renamed.");
                }
            }
        });
        try {
            categoryList.getItems().setAll(inventoryService.getCategories());
            dialog.showAndWait();
        } catch (SQLException e) {
            logger.error("Failed to load product categories", e);
            showError("Product categories could not be loaded.");
        }
    }

    private void refreshProducts() {
        try {
            products.setAll(inventoryService.getActiveProducts());
        } catch (SQLException e) {
            logger.error("Failed to load inventory products", e);
            showError("Inventory could not be loaded.");
        }
    }

    private void filterProducts(String searchText) {
        String query = searchText == null ? "" : searchText.trim().toLowerCase(Locale.ROOT);
        filteredProducts.setPredicate(item -> query.isEmpty()
                || item.getProductName().toLowerCase(Locale.ROOT).contains(query)
                || Objects.requireNonNullElse(item.getCategoryName(), "").toLowerCase(Locale.ROOT).contains(query)
                || Objects.requireNonNullElse(item.getSku(), "").toLowerCase(Locale.ROOT).contains(query));
    }

    private TextField decimalField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d*(\\.\\d{0,2})?") ? change : null));
        return field;
    }

    private TextField integerField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d*") ? change : null));
        return field;
    }

    private void addFormRow(GridPane form, int row, String label, javafx.scene.Node input) {
        form.addRow(row, new Label(label), input);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText("Inventory");
        alert.showAndWait();
    }

    private final class CategoryPicker extends VBox {
        private final List<CategoryDTO> availableCategories;
        private final List<String> selectedNames = new ArrayList<>();
        private final TextField categoryField = new TextField();
        private final Label hint = new Label();
        private final FlowPane selectedTags = new FlowPane(6, 6);
        private final ContextMenu suggestions = new ContextMenu();

        private CategoryPicker(List<CategoryDTO> availableCategories, List<String> initialNames) {
            super(6);
            this.availableCategories = availableCategories;
            selectedNames.addAll(initialNames);
            categoryField.setPromptText("Type or choose a category");
            Button dropdownButton = new Button("▼");
            dropdownButton.setOnAction(event -> showSuggestions());
            HBox inputRow = new HBox(4, categoryField, dropdownButton);
            HBox.setHgrow(categoryField, Priority.ALWAYS);
            getChildren().addAll(inputRow, selectedTags, hint);
            categoryField.textProperty().addListener((observable, oldValue, newValue) -> updateSuggestions());
            categoryField.setOnAction(event -> addTypedCategory());
            renderTags();
        }

        private List<String> getSelectedNames() {
            return List.copyOf(selectedNames);
        }

        private void addTypedCategory() {
            String value = categoryField.getText() == null ? "" : categoryField.getText().trim();
            if (value.isEmpty()) {
                return;
            }
            addCategoryName(value);
        }

        private void addCategoryName(String name) {
            if (selectedNames.stream().anyMatch(existing -> existing.equalsIgnoreCase(name))) {
                categoryField.clear();
                return;
            }
            selectedNames.add(name);
            categoryField.clear();
            suggestions.hide();
            hint.setText("");
            renderTags();
        }

        private void renderTags() {
            selectedTags.getChildren().clear();
            for (String name : selectedNames) {
                Button tag = new Button(name + "  ×");
                tag.getStyleClass().add("category-tag");
                tag.setOnAction(event -> {
                    selectedNames.remove(name);
                    renderTags();
                });
                selectedTags.getChildren().add(tag);
            }
        }

        private void updateSuggestions() {
            String query = categoryField.getText() == null ? "" : categoryField.getText().trim();
            suggestions.getItems().clear();
            if (query.isEmpty()) {
                hint.setText("");
                return;
            }

            List<CategoryDTO> matches = availableCategories.stream()
                    .filter(category -> category.getName().toLowerCase(Locale.ROOT)
                            .contains(query.toLowerCase(Locale.ROOT)))
                    .filter(category -> selectedNames.stream()
                            .noneMatch(selected -> selected.equalsIgnoreCase(category.getName())))
                    .toList();
            for (CategoryDTO category : matches) {
                MenuItem suggestion = new MenuItem(category.getName());
                suggestion.setOnAction(event -> addCategoryName(category.getName()));
                suggestions.getItems().add(suggestion);
            }

            boolean categoryExists = availableCategories.stream()
                    .anyMatch(category -> category.getName().equalsIgnoreCase(query));
            if (!categoryExists) {
                MenuItem createCategory = new MenuItem("Create category \"" + query + "\"");
                createCategory.setOnAction(event -> addCategoryName(query));
                suggestions.getItems().add(createCategory);
                hint.setText("\"" + query + "\" doesn't exist yet; it will be created when you save.");
            } else {
                hint.setText("Choose the existing category or press Enter to add it.");
            }
            if (categoryField.isFocused() && categoryField.getScene() != null) {
                showSuggestions();
            }
        }

        private void showSuggestions() {
            if (suggestions.getItems().isEmpty()) {
                updateSuggestions();
            }
            if (!suggestions.getItems().isEmpty() && categoryField.getScene() != null) {
                suggestions.show(categoryField, Side.BOTTOM, 0, 0);
            }
        }
    }
}
