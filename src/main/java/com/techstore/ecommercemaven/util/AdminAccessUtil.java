package com.techstore.ecommercemaven.util;

import com.techstore.ecommercemaven.model.User;
import jakarta.servlet.http.HttpSession;

public class AdminAccessUtil {

    public static boolean isAdmin(HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }
}