// Player class: likuciai
package com.spacecourier.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Player {
    private int currentFuel;
    private int gold;
    private String currentPlanet;
    private String previousPlanet;
    private Set<String> visitedPlanets;
    
    private Map<String, Integer> routeProgress;
    
    private static final int INITIAL_FUEL = 300;
    private static final int INITIAL_GOLD = 100;
    
    public Player(String startingPlanet) {
        this.currentFuel = INITIAL_FUEL;
        this.gold = INITIAL_GOLD;
        this.currentPlanet = startingPlanet;
        this.previousPlanet = null;
        this.visitedPlanets = new HashSet<>();
        this.routeProgress = new HashMap<>();
        for (RouteSequence route : RouteSequence.getWinRoutes()) {
            routeProgress.put(route.getName(), 0);
        }
        if (startingPlanet != null) {
            this.visitedPlanets.add(startingPlanet);
        }
    }
    
    public int getCurrentFuel() {
        return currentFuel;
    }
    
    public int getGold() {
        return gold;
    }
    
    public void setGold(int gold) {
        this.gold = Math.max(0, gold);
    }
    
    public void addGold(int amount) {
        this.gold += amount;
    }
    
    public void removeGold(int amount) {
        this.gold = Math.max(0, this.gold - amount);
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
    
    
    public void setCurrentPlanet(String planet) {
        if (planet != null && !planet.equals(this.currentPlanet)) {
            this.previousPlanet = this.currentPlanet;
            this.currentPlanet = planet;
            this.visitedPlanets.add(planet);
        }
    }
    
    public void setCurrentPlanetWithoutTracking(String planet) {
        if (planet != null) {
            this.currentPlanet = planet;
            this.visitedPlanets.add(planet);
        }
    }
    
    public String getPreviousPlanet() {
        return previousPlanet;
    }
    
    public boolean canTravel(int fuelCost) {
        return currentFuel >= fuelCost;
    }
    
    public boolean updateRouteProgress(String from, String to) {
        boolean routeCompleted = false;
        
        for (RouteSequence route : RouteSequence.getWinRoutes()) {
            String routeName = route.getName();
            int currentProgress = routeProgress.getOrDefault(routeName, 0);
            java.util.List<String> planets = route.getPlanets();
            
            if (currentProgress < planets.size()) {
                String expectedPlanet = planets.get(currentProgress);
                if (from != null && from.equals(expectedPlanet)) {
                    if (currentProgress + 1 < planets.size()) {
                        String nextPlanet = planets.get(currentProgress + 1);
                        if (to != null && to.equals(nextPlanet)) {
                            int newProgress = currentProgress + 1;
                            routeProgress.put(routeName, newProgress);
                            if (newProgress == planets.size() - 1) {
                                routeCompleted = true;
                            }
                        }
                    }
                }
            }
        }
        
        return routeCompleted;
    }
    
    public boolean hasWon() {
        for (RouteSequence route : RouteSequence.getWinRoutes()) {
            String routeName = route.getName();
            int progress = routeProgress.getOrDefault(routeName, 0);
            if (progress < route.getPlanets().size() - 1) {
                return false;
            }
        }
        return true;
    }
    
    public Map<String, Integer> getRouteProgress() {
        return new HashMap<>(routeProgress);
    }
    
    public void reset(String startingPlanet) {
        this.currentFuel = INITIAL_FUEL;
        this.gold = INITIAL_GOLD;
        this.currentPlanet = startingPlanet;
        this.previousPlanet = null;
        this.visitedPlanets.clear();
        this.routeProgress.clear();
        for (RouteSequence route : RouteSequence.getWinRoutes()) {
            routeProgress.put(route.getName(), 0);
        }
        if (startingPlanet != null) {
            this.visitedPlanets.add(startingPlanet);
        }
    }
}

