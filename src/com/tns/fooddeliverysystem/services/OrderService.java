package com.tns.fooddeliverysystem.services;

import com.tns.fooddeliverysystem.db.DBConnection;
import com.tns.fooddeliverysystem.entities.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    public int placeOrder(int customerId, String deliveryAddress) throws SQLException {
        String findCart = "SELECT food_id, quantity FROM cart_items WHERE customer_id=?";
        String insertOrder = "INSERT INTO orders(customer_id, status, delivery_address) VALUES (?, 'Pending', ?)";
        String insertItem = "INSERT INTO order_items(order_id, food_id, quantity) VALUES (?, ?, ?)";
        String clearCart = "DELETE FROM cart_items WHERE customer_id=?";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                List<int[]> cart = new ArrayList<>();
                try (PreparedStatement ps = con.prepareStatement(findCart)) {
                    ps.setInt(1, customerId);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) cart.add(new int[]{rs.getInt(1), rs.getInt(2)});
                    }
                }
                if (cart.isEmpty()) throw new SQLException("Cart is empty.");

                int orderId;
                try (PreparedStatement ps = con.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, customerId);
                    ps.setString(2, deliveryAddress);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        orderId = keys.getInt(1);
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(insertItem)) {
                    for (int[] item : cart) {
                        ps.setInt(1, orderId);
                        ps.setInt(2, item[0]);
                        ps.setInt(3, item[1]);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                try (PreparedStatement ps = con.prepareStatement(clearCart)) {
                    ps.setInt(1, customerId);
                    ps.executeUpdate();
                }
                con.commit();
                return orderId;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public void addToCart(int customerId, int foodId, int quantity) throws SQLException {
        String sql = "INSERT INTO cart_items(customer_id, food_id, quantity) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setInt(2, foodId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        }
    }

    public void printCart(int customerId) throws SQLException {
        String sql = "SELECT f.name, f.price, c.quantity, f.price*c.quantity total " +
                     "FROM cart_items c JOIN food_items f ON c.food_id=f.id WHERE c.customer_id=?";
        double total = 0;
        System.out.println("Cart:");
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean empty = true;
                while (rs.next()) {
                    empty = false;
                    double cost = rs.getDouble(4);
                    total += cost;
                    System.out.printf("Food Item: %s, Quantity: %d, Cost: Rs. %.2f%n",
                            rs.getString(1), rs.getInt(3), cost);
                }
                if (empty) System.out.println("Cart is empty.");
            }
        }
        System.out.printf("Total Cost: Rs. %.2f%n", total);
    }

    public void printOrders() throws SQLException {
        String sql = "SELECT o.order_id, u.username, o.status, " +
                     "COALESCE(d.name, 'Not Assigned'), o.delivery_address " +
                     "FROM orders o JOIN users u ON o.customer_id=u.user_id " +
                     "LEFT JOIN delivery_persons d ON o.delivery_person_id=d.id ORDER BY o.order_id";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Order{orderId=" + rs.getInt(1) +
                        ", customer=" + rs.getString(2) +
                        ", status='" + rs.getString(3) +
                        "', deliveryPerson=" + rs.getString(4) +
                        ", address='" + rs.getString(5) + "'}");
            }
        }
    }

    public void addDeliveryPerson(DeliveryPerson dp) throws SQLException {
        String sql = "INSERT INTO delivery_persons(id, name, contact_no) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dp.getDeliveryPersonId());
            ps.setString(2, dp.getName());
            ps.setLong(3, dp.getContactNo());
            ps.executeUpdate();
        }
    }

    public void assignDeliveryPerson(int orderId, int deliveryPersonId) throws SQLException {
        String sql = "UPDATE orders SET delivery_person_id=? WHERE order_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, deliveryPersonId);
            ps.setInt(2, orderId);
            if (ps.executeUpdate() == 0) throw new SQLException("Order not found.");
        }
    }

    public void printCustomerOrders(int customerId) throws SQLException {
        String sql = "SELECT o.order_id, o.status, COALESCE(d.name, 'Not Assigned') " +
                     "FROM orders o LEFT JOIN delivery_persons d ON o.delivery_person_id=d.id " +
                     "WHERE o.customer_id=? ORDER BY o.order_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.println("Order ID: " + rs.getInt(1) +
                            ", Status: " + rs.getString(2) +
                            ", Delivery Person: " + rs.getString(3));
                }
                if (!found) System.out.println("No orders found.");
            }
        }
    }
}
