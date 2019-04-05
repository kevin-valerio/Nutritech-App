package com.nutritech.models;

/**
 * Exception for an unfound food
 */
public class UnfoundFoodException extends Throwable {
    public UnfoundFoodException() {
        super("L'aliment cherché est introuvable");
    }
}