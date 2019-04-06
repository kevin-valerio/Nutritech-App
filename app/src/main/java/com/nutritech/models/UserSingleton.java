package com.nutritech.models;

public class UserSingleton {

    private static User User = new User();

    public static User getUser() {
        return User;
    }

}
