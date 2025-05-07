package jd.com.service;

import jd.com.model.User;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private final Map<String, User> users = new HashMap<>();

    public AuthService() {
        // Sample admin user
        users.put("admin", new User("admin", Integer.toString("admin123".hashCode()), true));
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPasswordHash().equals(Integer.toString(password.hashCode()))) {
            return user;
        }
        return null;
    }
}