package jd.com.service;

import jd.com.model.Product;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class ProductService {
    private final List<Product> products;
    private static final String CSV_FILE = "products.csv";

    public ProductService() {
        this.products = new ArrayList<>();
        loadProducts();
    }

    // Add new product
    public boolean addProduct(Product product) {
        if (product == null || product.getId() == null || product.getId().isEmpty()) {
            return false;
        }
        products.add(product);
        return saveProducts();
    }

    // Delete product (admin-only)
    public boolean deleteProduct(String productId) {
        Optional<Product> productToRemove = products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst();

        if (productToRemove.isPresent()) {
            products.remove(productToRemove.get());
            return saveProducts();
        }
        return false;
    }

    // Get all products
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    // Find product by ID
    public Optional<Product> findProductById(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    // File persistence
    private void loadProducts() {
        try {
            if (!Files.exists(Paths.get(CSV_FILE))) return;

            List<String> lines = Files.readAllLines(Paths.get(CSV_FILE));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    products.add(new Product(
                            parts[0].trim(),
                            parts[1].trim(),
                            Double.parseDouble(parts[2].trim()),
                            parts[3].trim()
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading products: " + e.getMessage());
        }
    }

    private boolean saveProducts() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            products.forEach(p -> writer.println(
                    p.getId() + "," + p.getName() + "," +
                            p.getPrice() + "," + p.getCategory()
            ));
            return true;
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
            return false;
        }
    }
}