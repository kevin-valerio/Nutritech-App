package com.nutritech.models;


public class DateTuple {
    private int date;
    private int weight;



    DateTuple(int date,int weight){
        this.date = date;
        this.weight = weight;
    }

    public int getDate() {
        return date;
    }

    public int getWeight() {
        return weight;
    }
}
