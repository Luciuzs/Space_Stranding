// Marsrutu win condition
package com.spacecourier.game;

import java.util.ArrayList;
import java.util.List;

public class RouteSequence {
    private final String name;
    private final List<String> planets;
    
    public RouteSequence(String name, List<String> planets) {
        this.name = name;
        this.planets = new ArrayList<>(planets);
    }
    
    public String getName() {
        return name;
    }
    
    public List<String> getPlanets() {
        return new ArrayList<>(planets);
    }
    
    public boolean matchesStart(String from, String to) {
        if (planets.size() < 2) return false;
        return planets.get(0).equals(from) && planets.get(1).equals(to);
    }
    
    public static List<RouteSequence> getWinRoutes() {
        List<RouteSequence> routes = new ArrayList<>();
        
        List<String> earthRoute1 = new ArrayList<>();
        earthRoute1.add("Earth");
        earthRoute1.add("Mars");
        earthRoute1.add("Jupiter");
        routes.add(new RouteSequence("Earth Route 1", earthRoute1));
        
        List<String> earthRoute2 = new ArrayList<>();
        earthRoute2.add("Earth");
        earthRoute2.add("Venus");
        earthRoute2.add("Saturn");
        routes.add(new RouteSequence("Earth Route 2", earthRoute2));
        
        List<String> marsRoute1 = new ArrayList<>();
        marsRoute1.add("Mars");
        marsRoute1.add("Jupiter");
        marsRoute1.add("Saturn");
        routes.add(new RouteSequence("Mars Route 1", marsRoute1));
        
        List<String> marsRoute2 = new ArrayList<>();
        marsRoute2.add("Mars");
        marsRoute2.add("Earth");
        marsRoute2.add("Venus");
        routes.add(new RouteSequence("Mars Route 2", marsRoute2));
        
        return routes;
    }
}

