package jd.com.util;

import jd.com.model.Cart;
import java.io.*;

public class FileUtil {
    public static void saveCart(Cart cart) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(cart.getUser().getUsername() + ".cart"))) {
            oos.writeObject(cart);
        } catch (IOException e) {
            System.err.println("Failed to save cart: " + e.getMessage());
        }
    }

    public static Cart loadCart(String username) {
        File file = new File(username + ".cart");
        if (!file.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            return (Cart) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load cart: " + e.getMessage());
            return null;
        }
    }
}