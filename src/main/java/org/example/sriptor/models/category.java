package org.example.sriptor.models;

public class category {
    private String categoryId;
    private String categoryName;



    public category(String categoryId, String categoryName){
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }


    //setters

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    //getters

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
