package org.example.sriptor.models;

import java.util.ArrayList;
import java.util.List;

public class cart {
    private List<saleItem> items = new ArrayList<>();

    public void addProduct(product product, int quantity){
        for(saleItem item : items){
            if (item.getProduct().getId() == product.getId()){
                item.setSaleItemQuantity(item.getSaleItemQuantity() + quantity);
                return;

            }
        }
        items.add(new saleItem(product, quantity));
    }

    public List<saleItem> getItems() {
        return items;
    }
    public double calculateTotal(){
        double total =0.0;
        for(saleItem item: items){
            total += item.getSubtotal();
        }
        return total;
    }
    public void clear(){
        items.clear();
    }
}
