package org.example.sriptor.models;

public class organizations {
    public String companyName;
    public String countryName;
    public String cityName;
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
}
