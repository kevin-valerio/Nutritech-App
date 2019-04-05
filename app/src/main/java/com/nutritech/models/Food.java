package com.nutritech.models;

public class Food {
    private String name;
    private long protein;
    private long lipid;
    private long carb;
    private long calorie;
    private int quantite;

    public Food(String name, long protein, long lipid, long carb) {
        this.name = name;
        this.protein = protein;
        this.lipid = lipid;
        this.carb = carb;
        this.quantite = 0;
        this.calorie = protein * 4 + lipid * 9 + carb * 4;
    }

    public Food(String name) throws UnfoundFoodException {
        if (name != null) {

            Food findFood = FoodFactory.getFoodByName(name);

            this.name = findFood.getName();
            this.protein = findFood.getProtein();
            this.lipid = findFood.getLipid();
            this.carb = findFood.getCarb();
            this.calorie = protein * 4 + lipid * 9 + carb * 4;
        }
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getProtein() {
        return protein;
    }

    public void setProtein(long protein) {
        this.protein = protein;
    }

    public long getLipid() {
        return lipid;
    }

    public void setLipid(long lipid) {
        this.lipid = lipid;
    }

    public long getCarb() {
        return carb;
    }

    public void setCarb(long carb) {
        this.carb = carb;
    }

    @Override
    public String toString() {
        return name + " : " + quantite + "g";
    }

    public long getCalorie() {
        return calorie;
    }
}