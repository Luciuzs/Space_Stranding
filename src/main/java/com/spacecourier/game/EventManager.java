// Danger atvejai i ju tipai
package com.spacecourier.game;

import java.util.Random;

public class EventManager {
    private static final Random random = new Random();
    
    public static SpaceEvent generateRandomEvent() {
        int eventIndex = random.nextInt(4);
        
        switch (eventIndex) {
            case 0:
                return SpaceEvent.createPirateAttack();
            case 1:
                return SpaceEvent.createSpaceStorm();
            case 2:
                return SpaceEvent.createFuelLeak();
            case 3:
                return SpaceEvent.createNavigationError();
            default:
                return SpaceEvent.createSpaceStorm();
        }
    }
}

enum EventType {
    PIRATE_ATTACK,
    SPACE_STORM,
    FUEL_LEAK,
    NAVIGATION_ERROR
}

class SpaceEvent {
    public final EventType type;
    public final String name;
    public final String description;
    
    public SpaceEvent(EventType type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }
    
    public static SpaceEvent createPirateAttack() {
        return new SpaceEvent(EventType.PIRATE_ATTACK, 
                             "Pirate Attack!", 
                             "Pirates have stolen all your gold! Game Over.");
    }
    
    public static SpaceEvent createSpaceStorm() {
        return new SpaceEvent(EventType.SPACE_STORM, 
                             "Space Storm", 
                             "A dangerous space storm damaged your ship! Lost 20% fuel.");
    }
    
    public static SpaceEvent createFuelLeak() {
        return new SpaceEvent(EventType.FUEL_LEAK, 
                             "Fuel Leak", 
                             "Critical fuel leak detected! Lost 30% fuel.");
    }
    
    public static SpaceEvent createNavigationError() {
        return new SpaceEvent(EventType.NAVIGATION_ERROR, 
                             "Navigation Error", 
                             "Navigation systems malfunctioned! Returning to previous planet.");
    }
}

