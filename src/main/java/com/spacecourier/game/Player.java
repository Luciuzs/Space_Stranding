// Player class: likuciai
package com.spacecourier.game;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private int currentFuel;
    private boolean hasCargo;
    private String currentPlanet;
    private Set<String> visitedPlanets;
    
    private static final int INITIAL_FUEL = 100;
    
    public Player(String startingPlanet) {
        this.currentFuel = INITIAL_FUEL;
        this.hasCargo = false;
        this.currentPlanet = startingPlanet;
        this.visitedPlanets = new HashSet<>();
        this.visitedPlanets.add(startingPlanet);
    }
    
    public int getCurrentFuel() {
        return currentFuel;
    }
    
    public boolean hasCargo() {
        return hasCargo;
    }
    
    public String getCurrentPlanet() {
        return currentPlanet;
    }
    
    public Set<String> getVisitedPlanets() {
        return visitedPlanets;
    }
    
    public int getVisitedPlanetCount() {
        return visitedPlanets.size();
    }
    
    public void setCurrentFuel(int fuel) {
        this.currentFuel = Math.max(0, fuel);
    }
    
    public void consumeFuel(int amount) {
        this.currentFuel = Math.max(0, this.currentFuel - amount);
    }
    
    public void addFuel(int amount) {
        this.currentFuel += amount;
    }
    
    public void setHasCargo(boolean hasCargo) {
        this.hasCargo = hasCargo;
    }
    
    public void setCurrentPlanet(String planet) {
        if (planet != null && !planet.equals(this.currentPlanet)) {
            this.currentPlanet = planet;
            this.visitedPlanets.add(planet);
        }
    }
    
    public boolean canTravel(int fuelCost) {
        return currentFuel >= fuelCost;
    }
    
    public void reset(String startingPlanet) {
        this.currentFuel = INITIAL_FUEL;
        this.hasCargo = false;
        this.currentPlanet = startingPlanet;
        this.visitedPlanets.clear();
        this.visitedPlanets.add(startingPlanet);
    }
}

