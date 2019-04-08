package com.nutritech.models;

import java.util.ArrayList;

public class FoodSuggestor {
    private ArrayList<String> foodList = new ArrayList<>();

    {
        foodList.add("Mandarine");
        foodList.add("Pomme");
        foodList.add("Poire");
        foodList.add("Poulet");
        foodList.add("Amande");
        foodList.add("Steack");
        foodList.add("Steack");
        foodList.add("Frites");
        foodList.add("La mère d'Hugo");
        foodList.add("Haricots verts");
        foodList.add("Saumon fumé");
        foodList.add("Amandes");
        foodList.add("Pates");

    }
    public ArrayList<String> getFoodList() {

        return foodList;
    }
}
