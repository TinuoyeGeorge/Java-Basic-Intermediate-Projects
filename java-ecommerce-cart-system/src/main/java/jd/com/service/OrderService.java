package jd.com.service;

import jd.com.model.Order;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class OrderService {
    private static final String ORDERS_DIR = "orders/";

    public OrderService() {
        try {
            Files.createDirectories(Paths.get(ORDERS_DIR));
        } catch (IOException e) {
            System.err.println("Error creating orders directory: " + e.getMessage());
        }
    }

    // Changed parameter to match usage
    public List<String> getOrderHistory() {
        try {
            return Files.list(Paths.get(ORDERS_DIR))
                    .filter(path -> path.toString().endsWith(".csv"))
                    .map(path -> {
                        try {
                            return Files.readAllLines(path).get(0); // Read header line
                        } catch (IOException e) {
                            return "Error reading: " + path;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error listing orders: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveOrder(Order order) {
        String filename = ORDERS_DIR + order.getId() + ".csv";
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.println("OrderID,Date,Total,Status,Address");
            writer.printf("\"%s\",\"%s\",%.2f,\"%s\",\"%s\"%n",
                    order.getId(),
                    order.getDate(),
                    order.getTotal(),
                    order.getPaymentStatus(),
                    order.getShippingAddress());

            // Write items
            writer.println("ProductID,Name,Quantity,Price");
            order.getItems().forEach((product, qty) ->
                    writer.printf("\"%s\",\"%s\",%d,%.2f%n",
                            product.getId(),
                            product.getName(),
                            qty,
                            product.getPrice())
            );
        } catch (IOException e) {
            System.err.println("Error saving order: " + e.getMessage());
        }
    }
}