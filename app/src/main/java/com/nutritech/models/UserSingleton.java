package com.nutritech.models;

public class UserSingleton {

    private static User User = null;

    public static User getUser() {
        return User;
    }

    public void setUser(User user) {
        if (user == null) {
            User = user;
        }
    }
}
