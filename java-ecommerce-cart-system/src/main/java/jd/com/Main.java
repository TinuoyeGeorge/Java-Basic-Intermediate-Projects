package jd.com;

import jd.com.model.*;
import jd.com.service.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.time.format.DateTimeFormatter;

public class Main {
    // Services
    private static final ProductService productService = new ProductService();
    private static final CartService cartService = new CartService();
    private static final PaymentService paymentService = new PaymentService();
    private static final OrderService orderService = new OrderService();

    private static final Scanner scanner = new Scanner(System.in);
    private static User currentUser;

    public static void main(String[] args) {
        initializeSystem();
        showMainMenu();
    }

    // ========================
    // INITIALIZATION
    // ========================
    private static void initializeSystem() {
        createDirectories();
        System.out.println("🛍️  Java E-Commerce System v2.0");
    }

    private static void createDirectories() {
        try {
            Files.createDirectories(Paths.get("orders"));
            Files.createDirectories(Paths.get("receipts"));
        } catch (IOException e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }
    }

    // ========================
    // MAIN MENU SYSTEM
    // ========================
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Admin Login");
            System.out.println("2. Customer Login");
            System.out.println("3. Exit");

            switch (readIntInput("Select option: ")) {
                case 1 -> handleAdminLogin();
                case 2 -> handleCustomerLogin();
                case 3 -> {
                    System.out.println("👋 Exiting system...");
                    System.exit(0);
                }
                default -> System.out.println("❌ Invalid selection");
            }
        }
    }

    // ========================
    // ADMIN FLOW
    // ========================
    private static void handleAdminLogin() {
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            System.out.print("\nEnter admin password: ");
            String enteredPassword = scanner.nextLine();

            if (enteredPassword.equals(prop.getProperty("admin.password"))) {
                currentUser = new User("admin", "admin123", true);
                showAdminDashboard();
            } else {
                System.out.println("❌ Access denied");
            }
        } catch (IOException e) {
            System.err.println("Error reading config: " + e.getMessage());
            System.out.println("❌ System error - contact developer");
        }
    }

    private static void showAdminDashboard() {
        while (true) {
            System.out.println("\n=== ADMIN DASHBOARD ===");
            System.out.println("1. Manage Products");
            System.out.println("2. View All Orders");  // ORDER HISTORY LOCATED HERE
            System.out.println("3. Logout");

            switch (readIntInput("Select action: ")) {
                case 1 -> showProductManagement();
                case 2 -> showOrderHistory();  // ORDER HISTORY MENU
                case 3 -> { return; }
                default -> System.out.println("❌ Invalid selection");
            }
        }
    }

    // ========================
    // ORDER HISTORY FEATURE (ADMIN)
    // ========================
    private static void showOrderHistory() {
        System.out.println("\n📦 ORDER HISTORY");
        List<String> orders = orderService.getOrderHistory();

        if (orders.isEmpty()) {
            System.out.println("No orders found");
            return;
        }

        // Display order summaries
        for (int i = 0; i < orders.size(); i++) {
            String[] parts = orders.get(i).split(",");
            System.out.printf("\n%d. %s | %s | $%s",
                    i + 1,
                    parts[0].replace("\"", ""),  // Order ID
                    parts[1].replace("\"", ""),  // Date
                    parts[2]                      // Total
            );
        }

        // Option to view details
        System.out.print("\n\nEnter order number to view details (or 0 to go back): ");
        int selection = readIntInput("");

        if (selection > 0 && selection <= orders.size()) {
            String orderId = orders.get(selection - 1).split(",")[0].replace("\"", "");
            viewOrderDetails(orderId);
        }
    }

    private static void viewOrderDetails(String orderId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("orders/" + orderId + ".csv"));
            System.out.println("\n📄 ORDER DETAILS");
            System.out.println("====================");
            lines.forEach(System.out::println);
            System.out.println("====================");
        } catch (IOException e) {
            System.out.println("❌ Order not found");
        }
        readStringInput("\nPress Enter to continue...");
    }

    // ========================
    // PRODUCT MANAGEMENT (ADMIN)
    // ========================
    private static void showProductManagement() {
        while (true) {
            System.out.println("\n📦 PRODUCT MANAGEMENT");
            System.out.println("1. Add Product");
            System.out.println("2. List Products");
            System.out.println("3. Delete Product");
            System.out.println("4. Back to Dashboard");

            switch (readIntInput("Select action: ")) {
                case 1 -> addProduct();
                case 2 -> listProducts();
                case 3 -> deleteProduct();
                case 4 -> { return; }
                default -> System.out.println("❌ Invalid selection");
            }
        }
    }

    private static void addProduct() {
        System.out.println("\n➕ ADD NEW PRODUCT");
        String id = readStringInput("Product ID: ");
        String name = readStringInput("Name: ");
        double price = readDoubleInput("Price: ");
        String category = readStringInput("Category: ");

        if (productService.addProduct(new Product(id, name, price, category))) {
            System.out.println("✅ Product added");
        } else {
            System.out.println("❌ Failed to add product");
        }
    }

    private static void deleteProduct() {
        listProducts();
        String id = readStringInput("Enter Product ID to delete: ");

        if (productService.deleteProduct(id)) {
            System.out.println("✅ Product deleted");
        } else {
            System.out.println("❌ Product not found");
        }
    }

    // ========================
    // CUSTOMER FLOW
    // ========================
    private static void handleCustomerLogin() {
        System.out.print("\nEnter your name: ");
        String username = scanner.nextLine().trim();

        if (!username.isEmpty()) {
            currentUser = new User(username, "N/A", false);
            showCustomerMenu();
        } else {
            System.out.println("❌ Invalid name");
        }
    }

    private static void showCustomerMenu() {
        Cart cart = new Cart(currentUser);

        while (true) {
            System.out.println("\n=== SHOPPING MENU ===");
            System.out.println("1. Browse Products");
            System.out.println("2. View Cart");
            System.out.println("3. Checkout");
            System.out.println("4. Logout");

            switch (readIntInput("Select action: ")) {
                case 1 -> browseProducts(cart);
                case 2 -> viewCart(cart);
                case 3 -> checkout(cart);
                case 4 -> { return; }
                default -> System.out.println("❌ Invalid selection");
            }
        }
    }

    // ========================
    // CUSTOMER OPERATIONS
    // ========================
    private static void browseProducts(Cart cart) {
        System.out.println("\n🛍️  AVAILABLE PRODUCTS");
        productService.getAllProducts().forEach(p ->
                System.out.printf("[%s] %-20s $%.2f (%s)%n",
                        p.getId(), p.getName(), p.getPrice(), p.getCategory())
        );

        System.out.println("\n1. Add to Cart");
        System.out.println("2. Back to Menu");

        if (readIntInput("Select action: ") == 1) {
            addToCart(cart);
        }
    }

    private static void addToCart(Cart cart) {
        String productId = readStringInput("Enter Product ID: ");
        int quantity = readIntInput("Quantity: ");

        productService.findProductById(productId).ifPresentOrElse(
                product -> {
                    cartService.addToCart(cart, product, quantity);
                    System.out.printf("✅ Added %d x %s to cart%n", quantity, product.getName());
                },
                () -> System.out.println("❌ Product not found")
        );
    }

    private static void viewCart(Cart cart) {
        System.out.println("\n🛒 YOUR CART");
        if (cart.getItems().isEmpty()) {
            System.out.println("(Your cart is empty)");
            return;
        }

        cart.getItems().forEach((product, qty) ->
                System.out.printf("- %-20s x%d @ $%.2f = $%.2f%n",
                        product.getName(), qty, product.getPrice(), product.getPrice() * qty)
        );
        System.out.printf("💵 TOTAL: $%.2f%n", cart.getTotal());

        System.out.println("\n1. Remove Item");
        System.out.println("2. Proceed to Checkout");
        System.out.println("3. Back to Menu");

        switch (readIntInput("Select action: ")) {
            case 1 -> removeFromCart(cart);
            case 2 -> checkout(cart);
            case 3 -> { return; }
        }
    }

    private static void removeFromCart(Cart cart) {
        String productId = readStringInput("Enter Product ID to remove: ");
        productService.findProductById(productId).ifPresentOrElse(
                product -> {
                    cartService.removeFromCart(cart, product);
                    System.out.println("✅ Item removed");
                },
                () -> System.out.println("❌ Product not in cart")
        );
    }

    private static void checkout(Cart cart) {
        if (cart.getItems().isEmpty()) {
            System.out.println("❌ Your cart is empty");
            return;
        }

        // Collect shipping info
        System.out.println("\n🚚 SHIPPING INFORMATION");
        String address = readStringInput("Shipping Address: ");

        // Confirm order
        System.out.println("\n📦 ORDER SUMMARY");
        cart.getItems().forEach((p, qty) ->
                System.out.printf("- %-20s x%d $%.2f%n", p.getName(), qty, p.getPrice() * qty)
        );
        System.out.printf("💵 TOTAL: $%.2f%n", cart.getTotal());

        if (!readStringInput("Confirm order (y/n)? ").equalsIgnoreCase("y")) {
            System.out.println("Order canceled");
            return;
        }

        // Process payment
        Order order = processPayment(cart, address);
        if (order != null) {
            generateReceipt(order);
            cart.getItems().clear();
        }
    }

    // ========================
    // PAYMENT PROCESSING
    // ========================
    private static Order processPayment(Cart cart, String address) {
        System.out.println("\n💳 PAYMENT PROCESSING");
        System.out.println("(Use test card: 4242 4242 4242 4242)");

        String cardNumber = readStringInput("Card Number: ").replaceAll("\\s+", "");
        String expiry = readStringInput("Expiry (MM/YY): ");
        String cvc = readStringInput("CVC: ");

        Order order = new Order(
                "ORD-" + System.currentTimeMillis(),
                cart.getItems(),
                cart.getTotal()
        );
        order.setShippingAddress(address);

        try {
            String status = paymentService.processPayment(order);
            order.setPaymentStatus(status);

            if ("succeeded".equals(status)) {
                orderService.saveOrder(order);
                System.out.println("\n🎉 PAYMENT SUCCESSFUL!");
                System.out.printf("Order #%s confirmed%n", order.getId());
                return order;
            } else {
                System.out.println("❌ Payment failed: " + status);
            }
        } catch (Exception e) {
            System.out.println("❌ Payment error: " + e.getMessage());
        }
        return null;
    }

    private static void generateReceipt(Order order) {
        String receiptFile = "receipts/" + order.getId() + ".txt";
        try (PrintWriter writer = new PrintWriter(receiptFile)) {
            writer.println("══════════════════════════════");
            writer.println("        ORDER RECEIPT         ");
            writer.println("══════════════════════════════");
            writer.printf("Order #: %s%n", order.getId());
            writer.printf("Date: %s%n",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            writer.printf("Status: %s%n", order.getPaymentStatus());
            writer.printf("Shipping To: %s%n%n", order.getShippingAddress());

            writer.println("ITEMS:");
            order.getItems().forEach((p, qty) ->
                    writer.printf("- %-20s x%d @ $%.2f%n", p.getName(), qty, p.getPrice())
            );

            writer.println("\n══════════════════════════════");
            writer.printf("TOTAL: $%.2f%n", order.getTotal());
            writer.println("══════════════════════════════");
            writer.println("Thank you for your order!");

            System.out.println("\n📄 Receipt generated: " + receiptFile);
        } catch (IOException e) {
            System.err.println("Error generating receipt: " + e.getMessage());
        }
    }

    // ========================
    // HELPER METHODS
    // ========================
    private static void listProducts() {
        System.out.println("\n📋 PRODUCT LIST");
        productService.getAllProducts().forEach(p ->
                System.out.printf("- [%s] %-20s $%.2f%n", p.getId(), p.getName(), p.getPrice())
        );
    }

    private static String readStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int readIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Please enter a valid number");
            }
        }
    }

    private static double readDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Please enter a valid price");
            }
        }
    }
}