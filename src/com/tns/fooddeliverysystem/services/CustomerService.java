package com.tns.fooddeliverysystem.services;

import com.tns.fooddeliverysystem.db.DBConnection;
import com.tns.fooddeliverysystem.entities.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    public void addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO users(user_id, username, contact_no) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customer.getUserId());
            ps.setString(2, customer.getUsername());
            ps.setLong(3, customer.getContactNo());
            ps.executeUpdate();
        }
    }

    public Customer getCustomer(int userId) throws SQLException {
        String sql = "SELECT user_id, username, contact_no FROM users WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return new Customer(rs.getInt(1), rs.getString(2), rs.getLong(3));
            }
        }
        return null;
    }

    public List<Customer> getCustomers() throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT user_id, username, contact_no FROM users ORDER BY user_id";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next())
                list.add(new Customer(rs.getInt(1), rs.getString(2), rs.getLong(3)));
        }
        return list;
    }
}
