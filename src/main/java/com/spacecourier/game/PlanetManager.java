// Planet manageris: planet info and cost
package com.spacecourier.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanetManager {
    public static final Planet EARTH = new Planet("Earth", 0, 1);
    public static final Planet MARS = new Planet("Mars", 20, 2);
    public static final Planet JUPITER = new Planet("Jupiter", 50, 6);
    public static final Planet SATURN = new Planet("Saturn", 40, 5);
    public static final Planet VENUS = new Planet("Venus", 15, 3);
    
    private static final String[] PLANET_NAMES = {"Mars", "Jupiter", "Saturn", "Venus"};
    private static final Map<String, Planet> PLANET_MAP = new HashMap<>();
    private static final Map<String, Integer> FUEL_COSTS = new HashMap<>();
    private static final Map<String, Integer> DANGER_RATINGS = new HashMap<>();
    
    private static final Map<String, List<Route>> ROUTE_GRAPH = new HashMap<>();
    
    static {
        PLANET_MAP.put("Earth", EARTH);
        PLANET_MAP.put("Mars", MARS);
        PLANET_MAP.put("Jupiter", JUPITER);
        PLANET_MAP.put("Saturn", SATURN);
        PLANET_MAP.put("Venus", VENUS);
        
        FUEL_COSTS.put("Mars", 20);
        FUEL_COSTS.put("Jupiter", 50);
        FUEL_COSTS.put("Saturn", 40);
        FUEL_COSTS.put("Venus", 15);
        
        DANGER_RATINGS.put("Mars", 2);
        DANGER_RATINGS.put("Jupiter", 6);
        DANGER_RATINGS.put("Saturn", 5);
        DANGER_RATINGS.put("Venus", 3);
        
        List<Route> earthRoutes = new ArrayList<>();
        earthRoutes.add(new Route(EARTH, MARS, 30, 0.2f));
        earthRoutes.add(new Route(EARTH, VENUS, 20, 0.4f));
        ROUTE_GRAPH.put("Earth", earthRoutes);
        
        List<Route> marsRoutes = new ArrayList<>();
        marsRoutes.add(new Route(MARS, JUPITER, 25, 0.7f));
        marsRoutes.add(new Route(MARS, EARTH, 30, 0.2f));
        ROUTE_GRAPH.put("Mars", marsRoutes);
        
        List<Route> jupiterRoutes = new ArrayList<>();
        jupiterRoutes.add(new Route(JUPITER, SATURN, 35, 0.3f));
        jupiterRoutes.add(new Route(JUPITER, MARS, 20, 0.65f));
        ROUTE_GRAPH.put("Jupiter", jupiterRoutes);
        
        List<Route> saturnRoutes = new ArrayList<>();
        saturnRoutes.add(new Route(SATURN, VENUS, 25, 0.5f));
        saturnRoutes.add(new Route(SATURN, JUPITER, 35, 0.3f));
        ROUTE_GRAPH.put("Saturn", saturnRoutes);
        
        List<Route> venusRoutes = new ArrayList<>();
        venusRoutes.add(new Route(VENUS, EARTH, 30, 0.25f));
        venusRoutes.add(new Route(VENUS, SATURN, 22, 0.55f));
        ROUTE_GRAPH.put("Venus", venusRoutes);
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
        return FUEL_COSTS.containsKey(planetName) || planetName.equals("Earth");
    }
    
    public static Planet getPlanetByName(String planetName) {
        return PLANET_MAP.get(planetName);
    }
    
    public static List<Route> getAvailableRoutes(String currentPlanet) {
        if (currentPlanet == null) {
            return new ArrayList<>();
        }
        List<Route> routes = ROUTE_GRAPH.get(currentPlanet);
        return routes != null ? new ArrayList<>(routes) : new ArrayList<>();
    }
}

