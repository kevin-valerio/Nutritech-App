package com.nutritech.models;

import java.util.ArrayList;

public class FoodSuggestor {
    private static ArrayList<Food> foodList = new ArrayList<>();

    static {
        foodList.add(new Food("Pomme", 20, 40, 50));
        foodList.add(new Food("Poire", 41, 18, 22));

    }

    public static ArrayList<Food> getFoodList() {

        return foodList;
    }
}
