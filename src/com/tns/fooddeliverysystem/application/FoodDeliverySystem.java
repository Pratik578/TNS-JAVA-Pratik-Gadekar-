package com.tns.fooddeliverysystem.application;

import com.tns.fooddeliverysystem.entities.*;
import com.tns.fooddeliverysystem.services.*;
import java.sql.SQLException;
import java.util.Scanner;

public class FoodDeliverySystem {
    private static final Scanner sc = new Scanner(System.in);
    private static final CustomerService customerService = new CustomerService();
    private static final FoodService foodService = new FoodService();
    private static final OrderService orderService = new OrderService();

    public static void main(String[] args) {
        System.out.println("===== FOOD DELIVERY SYSTEM =====");
        while (true) {
            System.out.println("\n1. Admin Menu");
            System.out.println("2. Customer Menu");
            System.out.println("3. Exit");
            int choice = readInt("Choose an option: ");

            try {
                switch (choice) {
                    case 1 -> adminMenu();
                    case 2 -> customerMenu();
                    case 3 -> { System.out.println("Thank you!"); return; }
                    default -> System.out.println("Invalid option.");
                }
            } catch (SQLException e) {
                System.out.println("Database Error: " + e.getMessage());
            }
        }
    }

    private static void adminMenu() throws SQLException {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Restaurant");
            System.out.println("2. Add Food Item to Restaurant");
            System.out.println("3. Remove Food Item from Restaurant");
            System.out.println("4. View Restaurants and Menus");
            System.out.println("5. View Orders");
            System.out.println("6. Add Delivery Person");
            System.out.println("7. Assign Delivery Person to Order");
            System.out.println("8. Exit");
            int ch = readInt("Choose an option: ");

            switch (ch) {
                case 1 -> {
                    int id = readInt("Enter Restaurant ID: ");
                    String name = readLine("Enter Restaurant Name: ");
                    foodService.addRestaurant(new Restaurant(id, name));
                    System.out.println("Restaurant added successfully!");
                }
                case 2 -> {
                    int rid = readInt("Enter Restaurant ID: ");
                    if (!foodService.restaurantExists(rid)) {
                        System.out.println("Restaurant not found."); break;
                    }
                    int fid = readInt("Enter Food Item ID: ");
                    String name = readLine("Enter Food Item Name: ");
                    double price = readDouble("Enter Food Item Price: ");
                    foodService.addFoodItemToRestaurant(rid, new FoodItem(fid, name, price));
                    System.out.println("Food item added successfully!");
                }
                case 3 -> {
                    int rid = readInt("Enter Restaurant ID: ");
                    int fid = readInt("Enter Food Item ID: ");
                    foodService.removeFoodItemFromRestaurant(rid, fid);
                    System.out.println("Food item removed successfully!");
                }
                case 4 -> printRestaurants();
                case 5 -> orderService.printOrders();
                case 6 -> {
                    int id = readInt("Enter Delivery Person ID: ");
                    String name = readLine("Enter Delivery Person Name: ");
                    long contact = readLong("Enter Contact No.: ");
                    orderService.addDeliveryPerson(new DeliveryPerson(id, name, contact));
                    System.out.println("Delivery person added successfully!");
                }
                case 7 -> {
                    int oid = readInt("Enter Order ID: ");
                    int did = readInt("Enter Delivery Person ID: ");
                    orderService.assignDeliveryPerson(oid, did);
                    System.out.println("Delivery person assigned to order successfully!");
                }
                case 8 -> { System.out.println("Exiting Admin Module"); return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void customerMenu() throws SQLException {
        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. Add Customer");
            System.out.println("2. View Food Items");
            System.out.println("3. Add Food to Cart");
            System.out.println("4. View Cart");
            System.out.println("5. Place Order");
            System.out.println("6. View Orders");
            System.out.println("7. Exit");
            int ch = readInt("Choose an option: ");

            switch (ch) {
                case 1 -> {
                    int id = readInt("Enter User ID: ");
                    String name = readLine("Enter Username: ");
                    long contact = readLong("Enter Contact No.: ");
                    customerService.addCustomer(new Customer(id, name, contact));
                    System.out.println("Customer created successfully!");
                }
                case 2 -> printRestaurants();
                case 3 -> {
                    int cid = readInt("Enter Customer ID: ");
                    if (customerService.getCustomer(cid) == null) {
                        System.out.println("Customer not found."); break;
                    }
                    int rid = readInt("Enter Restaurant ID: ");
                    int fid = readInt("Enter Food Item ID: ");
                    int qty = readInt("Enter Quantity: ");
                    if (qty <= 0) { System.out.println("Quantity must be positive."); break; }
                    if (foodService.getFoodItem(fid, rid) == null) {
                        System.out.println("Food item not found in that restaurant."); break;
                    }
                    orderService.addToCart(cid, fid, qty);
                    System.out.println("Food item added to cart!");
                }
                case 4 -> {
                    int cid = readInt("Enter Customer ID: ");
                    orderService.printCart(cid);
                }
                case 5 -> {
                    int cid = readInt("Enter Customer ID: ");
                    if (customerService.getCustomer(cid) == null) {
                        System.out.println("Customer not found."); break;
                    }
                    String address = readLine("Enter Delivery Address: ");
                    int orderId = orderService.placeOrder(cid, address);
                    System.out.println("Order placed successfully! Your order ID is: " + orderId);
                }
                case 6 -> {
                    int cid = readInt("Enter Customer ID: ");
                    orderService.printCustomerOrders(cid);
                }
                case 7 -> { System.out.println("Exiting Customer Module"); return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void printRestaurants() throws SQLException {
        System.out.println("\nRestaurants and Menus:");
        for (Restaurant r : foodService.getRestaurants()) {
            System.out.println("Restaurant ID: " + r.getId() + ", Name: " + r.getName());
            for (FoodItem f : r.getMenu())
                System.out.printf("- Food Item ID: %d, Name: %s, Price: Rs. %.2f%n",
                        f.getId(), f.getName(), f.getPrice());
        }
    }

    private static int readInt(String msg) {
        while (true) {
            try { System.out.print(msg); return Integer.parseInt(sc.nextLine().trim()); }
            catch (Exception e) { System.out.println("Enter a valid integer."); }
        }
    }

    private static long readLong(String msg) {
        while (true) {
            try { System.out.print(msg); return Long.parseLong(sc.nextLine().trim()); }
            catch (Exception e) { System.out.println("Enter a valid number."); }
        }
    }

    private static double readDouble(String msg) {
        while (true) {
            try { System.out.print(msg); return Double.parseDouble(sc.nextLine().trim()); }
            catch (Exception e) { System.out.println("Enter a valid price."); }
        }
    }

    private static String readLine(String msg) {
        System.out.print(msg);
        return sc.nextLine();
    }
}
