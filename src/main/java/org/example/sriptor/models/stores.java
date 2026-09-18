package org.example.sriptor.models;

public class stores {
    private String storeName;
    private String location;
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
}
