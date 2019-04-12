package com.nutritech.models;


import java.util.ArrayList;

public class FoodList {

    private long carbs;
    private long proteins;
    private long lipid;
    private long calorie;

    private ArrayList<Food> dailyFood;

    public FoodList() {
        dailyFood = new ArrayList<>();
    }

    public FoodList(ArrayList<Food> foodList) {
        dailyFood = foodList;
    }

    /**
     * Add food and it's quantity to the foodList
     *
     * @param foodToDel food to add
     * @param quantity quantity of food
     */
    public void addFood(Food foodToDel, long quantity) {
        calorie = (calorie + ((foodToDel.getCalorie() * quantity) / 100));
        carbs = (carbs + ((foodToDel.getCarb() * quantity) / 100));
        proteins = (proteins + ((foodToDel.getProtein() * quantity) / 100));
        lipid = (lipid + ((foodToDel.getLipid() * quantity) / 100));
        foodToDel.setQuantite((int) quantity);
        dailyFood.add(foodToDel);
    }

    /**
     * Create a list with numbers of :  calories | proteins | carbs | lipids
     *
     * @return ArrayList of total nutrients number
     */
    public ArrayList<Long> getTotals() {
        ArrayList<Long> totals = new ArrayList<Long>();
        totals.add(getCalorie());
        totals.add(getProteins());
        totals.add(getCarbs());
        totals.add(getLipid());
        return totals;
    }

    public long getCalorie() {
        return calorie;
    }

    public ArrayList<Food> getDailyFood() {
        return this.dailyFood;
    }

    public long getCarbs() {
        return carbs;
    }

    public long getProteins() {
        return proteins;
    }

    public long getLipid() {
        return lipid;
    }

    /**
     * Remove the given iten from the daily ate food
     *
     * @param rangeSelectedItem
     */
    public void removeItem(int rangeSelectedItem) {

        Food foodToDel = dailyFood.get(rangeSelectedItem);
        calorie = (calorie - ((foodToDel.getCalorie() * foodToDel.getQuantite()) / 100));
        carbs = (carbs - ((foodToDel.getCarb() * foodToDel.getQuantite()) / 100));
        proteins = (proteins - ((foodToDel.getProtein() * foodToDel.getQuantite()) / 100));
        lipid = (lipid) - ((foodToDel.getLipid() * foodToDel.getQuantite()) / 100);
        dailyFood.remove(foodToDel);

    }
}