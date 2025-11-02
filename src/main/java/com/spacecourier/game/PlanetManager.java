// Planet manageris: planet info and cost
package com.spacecourier.game;

import java.util.HashMap;
import java.util.Map;

public class PlanetManager {
    private static final String[] PLANET_NAMES = {"Mars", "Jupiter", "Saturn", "Venus"};
    private static final Map<String, Integer> FUEL_COSTS = new HashMap<>();
    private static final Map<String, Integer> DANGER_RATINGS = new HashMap<>();
    
    static {
        FUEL_COSTS.put("Mars", 20);
        FUEL_COSTS.put("Jupiter", 50);
        FUEL_COSTS.put("Saturn", 40);
        FUEL_COSTS.put("Venus", 15);
        
        DANGER_RATINGS.put("Mars", 2);
        DANGER_RATINGS.put("Jupiter", 6);
        DANGER_RATINGS.put("Saturn", 5);
        DANGER_RATINGS.put("Venus", 3);
    }
    
    public static String[] getPlanetNames() {
        return PLANET_NAMES;
    }
    
    public static int getFuelCost(String planetName) {
        return FUEL_COSTS.getOrDefault(planetName, 0);
    }
    
    public static int getDangerRating(String planetName) {
        return DANGER_RATINGS.getOrDefault(planetName, 5);
    }
    
    public static boolean planetExists(String planetName) {
        return FUEL_COSTS.containsKey(planetName);
    }
}

