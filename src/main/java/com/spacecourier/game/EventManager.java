// Danger atvejai
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

