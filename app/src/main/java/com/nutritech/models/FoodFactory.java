package com.nutritech.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FoodFactory {


    private static ArrayList<Food> foodList = new ArrayList<>();

    /**
     * Get or create an observable food list
     *
     * @param path        path ton the JSON
     * @param newFoodList Create a food list
     * @return list of food
     */
    public static ArrayList<Food> getFoodList(String path, boolean newFoodList) {
        if (newFoodList) {
            foodList = new ArrayList<>();
        }
        addAllFoodToList(path);
        return foodList;
    }

    /**
     * Get food from the given name
     *
     * @param name of the food
     * @return food
     * @throws UnfoundFoodException if not found
     */
    static Food getFoodByName(String name) throws UnfoundFoodException {
        for (Food food : foodList) {
            if (food.getName().equals(name)) {
                return food;
            }
        }
        throw new UnfoundFoodException();
    }

    /**
     * Retrieve the Json from the given path and init the foodList
     *
     * @param path
     */
    private static void addAllFoodToList(String path) {

        Food food;
        try {
            FileReader reader = new FileReader(path);
            JSONObject jsonObject = new JSONObject(reader.toString());

            JSONArray lang = (JSONArray) jsonObject.get("ingredients");

            for (int i = 0; i < lang.length(); ++i) {
                JSONObject innerObj = lang.getJSONObject(i);
                String name = (String) innerObj.get("Name");
                long protein = (long) innerObj.get("Protein");
                long lipid = (long) innerObj.get("Lipids");
                long carb = (long) innerObj.get("Carb");

                food = new Food(name, protein, lipid, carb);
                foodList.add(food);

            }

        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}