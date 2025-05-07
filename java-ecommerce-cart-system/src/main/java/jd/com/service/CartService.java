package jd.com.service;

import jd.com.model.Cart;
import jd.com.model.Product;
import jd.com.util.FileUtil;

public class CartService {
    public void addToCart(Cart cart, Product product, int quantity) {
        cart.addItem(product, quantity);
        FileUtil.saveCart(cart);
    }

    public void removeFromCart(Cart cart, Product product) {
        cart.removeItem(product);
        FileUtil.saveCart(cart);
    }
}
