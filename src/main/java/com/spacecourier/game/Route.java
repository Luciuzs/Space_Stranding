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

