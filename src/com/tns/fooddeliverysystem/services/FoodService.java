package com.tns.fooddeliverysystem.services;

import com.tns.fooddeliverysystem.db.DBConnection;
import com.tns.fooddeliverysystem.entities.FoodItem;
import com.tns.fooddeliverysystem.entities.Restaurant;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FoodService {

    public void addRestaurant(Restaurant restaurant) throws SQLException {
        String sql = "INSERT INTO restaurants(id, name) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, restaurant.getId());
            ps.setString(2, restaurant.getName());
            ps.executeUpdate();
        }
    }

    public List<Restaurant> getRestaurants() throws SQLException {
        List<Restaurant> list = new ArrayList<>();
        String sql = "SELECT id, name FROM restaurants ORDER BY id";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(new Restaurant(rs.getInt(1), rs.getString(2)));
        }
        loadMenus(list);
        return list;
    }

    private void loadMenus(List<Restaurant> restaurants) throws SQLException {
        String sql = "SELECT id, name, price, restaurant_id FROM food_items ORDER BY id";
        Map<Integer, Restaurant> map = new LinkedHashMap<>();
        for (Restaurant r : restaurants) map.put(r.getId(), r);

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Restaurant r = map.get(rs.getInt(4));
                if (r != null)
                    r.addFoodItem(new FoodItem(rs.getInt(1), rs.getString(2), rs.getDouble(3)));
            }
        }
    }

    public List<FoodItem> getAllFoodItems() throws SQLException {
        List<FoodItem> list = new ArrayList<>();
        String sql = "SELECT id, name, price FROM food_items ORDER BY id";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next())
                list.add(new FoodItem(rs.getInt(1), rs.getString(2), rs.getDouble(3)));
        }
        return list;
    }

    public FoodItem getFoodItem(int foodId, int restaurantId) throws SQLException {
        String sql = "SELECT id, name, price FROM food_items WHERE id=? AND restaurant_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, foodId);
            ps.setInt(2, restaurantId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return new FoodItem(rs.getInt(1), rs.getString(2), rs.getDouble(3));
            }
        }
        return null;
    }

    public void addFoodItemToRestaurant(int restaurantId, FoodItem foodItem) throws SQLException {
        String sql = "INSERT INTO food_items(id, name, price, restaurant_id) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, foodItem.getId());
            ps.setString(2, foodItem.getName());
            ps.setDouble(3, foodItem.getPrice());
            ps.setInt(4, restaurantId);
            ps.executeUpdate();
        }
    }

    public void removeFoodItemFromRestaurant(int restaurantId, int foodItemId) throws SQLException {
        String sql = "DELETE FROM food_items WHERE id=? AND restaurant_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, foodItemId);
            ps.setInt(2, restaurantId);
            ps.executeUpdate();
        }
    }

    public boolean restaurantExists(int id) throws SQLException {
        String sql = "SELECT 1 FROM restaurants WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }
}
