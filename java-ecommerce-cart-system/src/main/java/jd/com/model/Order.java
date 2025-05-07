package jd.com.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Order {
    private final String id;
    private final LocalDateTime date;
    private final Map<Product, Integer> items;
    private final double total;
    private String paymentStatus;
    private String shippingAddress;

    public Order(String id, Map<Product, Integer> items, double total) {
        this.id = id;
        this.date = LocalDateTime.now();
        this.items = Map.copyOf(items);
        this.total = total;
    }

    // Getters
    public String getId() { return id; }
    public String getDate() {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
    public double getTotal() { return total; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getShippingAddress() { return shippingAddress; }
    public Map<Product, Integer> getItems() { return Map.copyOf(items); }

    // Setters
    public void setPaymentStatus(String status) { this.paymentStatus = status; }
    public void setShippingAddress(String address) { this.shippingAddress = address; }
}