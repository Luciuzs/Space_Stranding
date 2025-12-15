package com.spacecourier.game.models;

public class Planet {
    public final String name;
    public final int fuelCost;
    public final int dangerRating;
    
    public Planet(String name, int fuelCost, int dangerRating) {
        this.name = name;
        this.fuelCost = fuelCost;
        this.dangerRating = dangerRating;
    }
}