// marsrutai
package com.spacecourier.game;

public class Route {
    public final Planet from;
    public final Planet to;
    public final int fuelCost;
    public final float riskLevel;
    
    public Route(Planet from, Planet to, int fuelCost, float riskLevel) {
        this.from = from;
        this.to = to;
        this.fuelCost = fuelCost;
        this.riskLevel = Math.max(0.0f, Math.min(1.0f, riskLevel));
    }
}

class RouteSequence {
    private final java.util.List<String> planets;
    private final String name;
    
    public RouteSequence(String name, java.util.List<String> planets) {
        this.name = name;
        this.planets = new java.util.ArrayList<>(planets);
    }
    
    public String getName() {
        return name;
    }
    
    public java.util.List<String> getPlanets() {
        return new java.util.ArrayList<>(planets);
    }
    
    public boolean matchesStart(String from, String to) {
        if (planets.size() < 2) return false;
        return planets.get(0).equals(from) && planets.get(1).equals(to);
    }
    
    public static java.util.List<RouteSequence> getWinRoutes() {
        java.util.List<RouteSequence> routes = new java.util.ArrayList<>();
        java.util.List<String> earthRoute = new java.util.ArrayList<>();
        earthRoute.add("Earth");
        earthRoute.add("Mars");
        earthRoute.add("Jupiter");
        earthRoute.add("Saturn");
        earthRoute.add("Venus");
        routes.add(new RouteSequence("Earth Route", earthRoute));
        java.util.List<String> marsRoute = new java.util.ArrayList<>();
        marsRoute.add("Mars");
        marsRoute.add("Jupiter");
        marsRoute.add("Saturn");
        marsRoute.add("Venus");
        marsRoute.add("Earth");
        routes.add(new RouteSequence("Mars Route", marsRoute));
        return routes;
    }
}

