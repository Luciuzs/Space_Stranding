// Danger aprasymai
package com.spacecourier.game;

public class SpaceEvent {
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

