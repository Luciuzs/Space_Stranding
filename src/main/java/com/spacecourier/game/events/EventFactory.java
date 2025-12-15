package com.spacecourier.game.events;

import java.util.Random;


public class EventFactory {
    
    private static final Random random = new Random();
    
    // Prevent instantiation
    private EventFactory() {
        throw new AssertionError("Cannot instantiate factory class");
    }
    
   
    public static SpaceEvent createEvent(EventType type) {
        switch (type) {
            case PIRATE_ATTACK:
                return new PirateAttackEvent();
            case SPACE_STORM:
                return new SpaceStormEvent();
            case FUEL_LEAK:
                return new FuelLeakEvent();
            case NAVIGATION_ERROR:
                return new NavigationErrorEvent();
            default:
                throw new IllegalArgumentException("Unknown event type: " + type);
        }
    }
    
   
    public static SpaceEvent createRandomEvent() {
        EventType[] types = EventType.values();
        EventType randomType = types[random.nextInt(types.length)];
        return createEvent(randomType);
    }
    
  
    public static SpaceEvent createWeightedRandomEvent(int dangerRating) {
        int roll = random.nextInt(100);
        
        int pirateThreshold = Math.min(dangerRating * 5, 30);
        int fuelLeakThreshold = pirateThreshold + Math.min(dangerRating * 7, 35);
        int stormThreshold = fuelLeakThreshold + 20;
        
        if (roll < pirateThreshold) {
            return new PirateAttackEvent();
        } else if (roll < fuelLeakThreshold) {
            return new FuelLeakEvent();
        } else if (roll < stormThreshold) {
            return new SpaceStormEvent();
        } else {
            return new NavigationErrorEvent();
        }
    }
}