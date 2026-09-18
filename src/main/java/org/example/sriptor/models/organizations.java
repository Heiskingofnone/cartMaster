package org.example.sriptor.models;

public class organizations {
    private String companyId;
    private String companyName;
    private String ownerId;
    private String countryName;
    private Industry industry;
    private String subscriptionPlan;
    private String subscriptionStatus;
    private String maxStoresAllowed;
    private int isActive;
    private String createdAt;
    private String updatedAt;

    public enum Industry{
        PHARMACY("Pharmacy & Healthcare"),
        COSMETIC("Cosmetics & Beauty Supply"),
        RETAIL("General Retail / Mini-Mart"),
        WHOLESALE("Wholesale & Distribution"),
        HYBRID("Wholesale & Retail (Hybrid)"),
        BOUTIQUE("Boutique & Apparel"),
        STATIONARY("Bookstore & Stationeries"),
        SUPERMARKET("Supermarket & Groceries"),
        ELECTRONIC("Electronics & Hardware"),
        RESTAURANT("Restaurant & food services"),
        CUSTOM("Other / Custom");

        private final String displayName;
        Industry(String displayName){this.displayName = displayName;}
        public String toString(){return this.displayName;}
    }


    public organizations(String companyId, String companyName, String ownerId, String countryName, Industry industry, String subscriptionPlan, String subscriptionStatus, String maxStoresAllowed, int isActive, String createdAt, String updatedAt){
        this.companyId = companyId;
        this.companyName = companyName;
        this.ownerId = ownerId;
        this.countryName = countryName;
        this.industry = industry;
        this.subscriptionPlan = subscriptionPlan;
        this.subscriptionStatus = subscriptionStatus;
        this.maxStoresAllowed = maxStoresAllowed;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public organizations(String companyId, String companyName, String ownerId, String countryName, Industry industry){
        this.companyId = companyId;
        this.companyName = companyName;
        this.ownerId = ownerId;
        this.countryName = countryName;
        this.industry = industry;
    }

    //Getters
    public String getCompanyId(){return companyId;}
    public String getCompanyName(){return companyName;}
    public String getOwnerId() {return ownerId;}
    public String getCountryName(){return countryName;}
    public Industry getIndustry() {return industry;}
    public String getSubscriptionPlan(){return subscriptionPlan;}
    public String getSubscriptionStatus(){return subscriptionStatus;}
    public String getMaxStoresAllowed(){return maxStoresAllowed;}
    public int getIsActive() {return isActive;}
    public String getCreatedAt() {return createdAt;}
    public String getUpdatedAt() {return updatedAt;}


    //Setter
    public void setCompanyId(String companyId) {this.companyId = companyId;}
    public void setCompanyName(String companyName) {this.companyName = companyName;}
    public void setOwnerId(String ownerId) {this.ownerId = ownerId;}
    public void setCountryName(String countryName) {this.countryName = countryName;}
    public void setIndustry(Industry industry) {this.industry = industry;}
    public void setSubscriptionPlan(String subscriptionPlan) {this.subscriptionPlan = subscriptionPlan;}
    public void setSubscriptionStatus(String subscriptionStatus) {this.subscriptionStatus = subscriptionStatus;}
    public void setMaxStoresAllowed(String maxStoresAllowed) {this.maxStoresAllowed = maxStoresAllowed;}
    public void setIsActive(int isActive) {this.isActive = isActive;}
    public void setCreatedAt(String createdAt) {this.createdAt = createdAt;}
    public void setUpdatedAt(String updatedAt) {this.updatedAt = updatedAt;}
}
