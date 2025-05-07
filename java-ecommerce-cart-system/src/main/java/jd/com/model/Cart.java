package jd.com.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// Manages shopping sessions
public class Cart implements Serializable {
    private final User user;
    private final Map<Product, Integer> items = new HashMap<>();

    public Cart(User user) {
        this.user = user;
    }

    public void addItem(Product product, int quantity) {
        items.merge(product, quantity, Integer::sum);
    }

    public void removeItem(Product product) {
        items.remove(product);
    }

    public Map<Product, Integer> getItems() { return new HashMap<>(items); }
    public User getUser() { return user; }

    public double getTotal() {
        return items.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }
}