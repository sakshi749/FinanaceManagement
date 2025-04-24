package com.mycompany.financemanagementsystem;

public class session {
    private static int userId;
    private static String username;

    public static void setUser(int id, String name) {
        userId = id;
        username = name;
    }

    public static int getUserId() {
        return userId;
    }

    public static String getUsername() {
        return username;
    }

    public static void clear() {
        userId = 0;
        username = null;
    }
}

