package com.nutritech.models;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Calendar;

public class User {

    private String firstname;
    private String mail;
    private String lastname;
    private String password;
    private Goal goal;
    private int objProteines;
    private int objGlucides;
    private int objLipides;
    private ArrayList<DateTuple> weights = new ArrayList<>();
    private int height;
    private int age;
    private ArrayList<Food> eatenFood = new ArrayList<>();
    private String gender;
    private HashMap<Integer, FoodList> calendarFoodList;
    private int dateOfTheDay = 13;
    private int currentDay;
    private double kcalObj;
    private double kcalCurrent = 0;

    public User() {
 
         initDatas("","",Goal.MAINTIEN_DE_SON_POIDS, 70, 180, 0, "Homme", "", "");
    }


    /**
     * Create a user
     *
     * @param firstname user's firstname
     * @param lastname  user's lastname
     * @param goal      user's goal
     * @param weight    user's weight
     * @param height    user's height
     * @param age       user's age
     * @param gender    user's gender
     */


    public User(String firstname, String lastname, Goal goal, int weight, int height, int age, String gender, String mail, String password) {
        initDatas(firstname, lastname, goal, weight, height, age, gender, mail, password);
    }

    private void initDatas(String firstname, String lastname, Goal goal, int weight, int height, int age, String gender, String mail, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.goal = goal;
        this.height = height;
        this.mail = mail;
        this.password = password;
        this.kcalObj = (weight * 10 + height * 6.25) - (5 * age);

        addWeight(weight);
        if (gender.equals("Homme"))
            this.kcalObj += 5;
        else if (gender.equals("Femme"))
            this.kcalObj += 161;
        if (goal == Goal.PRISE_DE_MASSE)
            kcalObj += 300;
        else if (goal == Goal.PERTE_DE_MASSE)
            kcalObj -= 300;
        kcalObj += 300;
        objGlucides = (int) (kcalObj * 0.4) / 4;
        objProteines = (int) (kcalObj * 0.3) / 4;
        objLipides = (int) (kcalObj * 0.3) / 9;
        calendarFoodList = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendarFoodList.put(currentDay, new FoodList());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public ArrayList<DateTuple> getWeights() {
        return weights;
    }

    public void setWeights(ArrayList<DateTuple> weights) {
        this.weights = weights;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getObjProteines() {
        return objProteines;
    }

    public void setObjProteines(int objProteines) {
        this.objProteines = objProteines;
    }

    public int getObjGlucides() {
        return objGlucides;
    }

    public void setObjGlucides(int objGlucides) {
        this.objGlucides = objGlucides;
    }

    public int getObjLipides() {
        return objLipides;
    }

    public void setObjLipides(int objLipides) {
        this.objLipides = objLipides;
    }

    public double getKcalObj() {
        return kcalObj;
    }

    public void setKcalObj(double kcal) {
        this.kcalObj = kcal;
    }

    public ArrayList<Food> getEatenFood() {
        return eatenFood;
    }

    public void eat(Food food) {
        this.eatenFood.add(food);
        int todaysCalories = Math.toIntExact(UserSingleton.getUser().getCalendarFoodList().get(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).getCalorie());

        this.setKcalCurrent(todaysCalories);



    }

    public double getKcalCurrent() {
        int todaysCalories = Math.toIntExact(UserSingleton.getUser().getCalendarFoodList().get(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).getCalorie());
        return todaysCalories;
    }

    public void setKcalCurrent(double kcal) {
        this.kcalCurrent = kcal;
    }

    /**
     * Add a weight to the user
     *
     * @param weight user's weight
     */
    public void addWeight(int weight) {
        weights.add(new DateTuple(dateOfTheDay,weight));
        dateOfTheDay++;
    }

    public HashMap<Integer, FoodList> getCalendarFoodList() {
        return this.calendarFoodList;
    }

    public void setCalendarFoodList(HashMap<Integer, FoodList> calendarFoodList) {
        this.calendarFoodList = calendarFoodList;
    }

    public int getDateOfTheDayCalories() {
        return currentDay;
    }
}