package com.tns.fooddeliverysystem.entities;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
    private Map<FoodItem, Integer> items = new LinkedHashMap<>();

    public Cart() {}

    public Map<FoodItem, Integer> getItems() { return items; }

    public void addItem(FoodItem foodItem, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
        items.put(foodItem, items.getOrDefault(foodItem, 0) + quantity);
    }

    public void removeItem(FoodItem foodItem) {
        items.remove(foodItem);
    }

    @Override
    public String toString() {
        return items.toString();
    }
}
