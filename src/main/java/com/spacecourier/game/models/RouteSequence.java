package com.spacecourier.game.models;

import java.util.ArrayList;
import java.util.List;

public class RouteSequence {
    private final List<String> planets;
    private final String name;
    
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
        if (planets.size() < 2) {
            return false;
        }
        return planets.get(0).equals(from) && planets.get(1).equals(to);
    }
    
    public static List<RouteSequence> getWinRoutes() {
        List<RouteSequence> routes = new ArrayList<>();
        
        List<String> earthRoute = new ArrayList<>();
        earthRoute.add("Earth");
        earthRoute.add("Mars");
        earthRoute.add("Jupiter");
        earthRoute.add("Saturn");
        earthRoute.add("Venus");
        routes.add(new RouteSequence("Earth Route", earthRoute));
        
        List<String> marsRoute = new ArrayList<>();
        marsRoute.add("Mars");
        marsRoute.add("Jupiter");
        marsRoute.add("Saturn");
        marsRoute.add("Venus");
        marsRoute.add("Earth");
        routes.add(new RouteSequence("Mars Route", marsRoute));
        
        return routes;
    }
}