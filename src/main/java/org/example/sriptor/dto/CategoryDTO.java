package org.example.sriptor.dto;

public class CategoryDTO {
    private final String id;
    private final String name;

    public CategoryDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
