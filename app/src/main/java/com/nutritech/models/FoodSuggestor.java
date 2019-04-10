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

    public static ArrayList<String> getFoodStringList() {
        ArrayList<String> toReturn = new ArrayList<>();
        foodList.forEach(food -> toReturn.add(food.getName()));
        return toReturn;
    }

    public static Food getFoodByName(String foodName) {
        for (Food food : foodList) {
            if (food.getName().equals(foodName)) {
                return food;
            }
        }
        return null;
    }

}
