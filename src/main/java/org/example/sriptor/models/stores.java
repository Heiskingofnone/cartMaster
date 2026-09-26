package org.example.sriptor.models;

public class stores {
    private String storeId;
    private String storeName;
    private String location;
    private Currency currency;
    private String inventoryId;
    private String dateOpened;
    private String companyId;

    public enum Currency{
        GHS("Cedis"),
        NGN("Naira"),
        USD("Dollar");

        private final String displayName;
        Currency(String displayName){this.displayName = displayName;}

        @Override
        public String toString() {
            return this.displayName;
        }
    }

    public stores(String storeId, String companyId, String inventoryId, String storeName, String location, Currency currency, String dateOpened){
        this.storeId = storeId;
        this.companyId = companyId;
        this.inventoryId = inventoryId;
        this.storeName = storeName;
        this.location = location;
        this.currency = currency;
        this.dateOpened = dateOpened;
    }

    //Getters
    public String getStoreId() {
        return storeId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public String getStoreName() {
        return storeName;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getLocation() {
        return location;
    }

    public String getDateOpened() {
        return dateOpened;
    }

    //Setters
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setDateOpened(String dateOpened) {
        this.dateOpened = dateOpened;
    }



}
