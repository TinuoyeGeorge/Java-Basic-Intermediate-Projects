package jd.com.model;

import java.io.Serializable;
// Defines what is being sold
public class Product implements Serializable {
    private final String id;
    private final String name;
    private final double price;
    private final String category;

    public Product(String id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
}