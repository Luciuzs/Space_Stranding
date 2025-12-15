package com.spacecourier.game.events;

import com.spacecourier.game.models.Player;
import com.spacecourier.game.GameInputHandler;
import com.spacecourier.game.constants.GameConstants;


public class SpaceStormEvent extends SpaceEvent {
    
    public SpaceStormEvent() {
        super(EventType.SPACE_STORM,
              "Space Storm",
              "A dangerous space storm damaged your ship! Lost 20% fuel.");
    }
    
    @Override
    public boolean apply(Player player, GameInputHandler inputHandler) {
        int fuelLost = (int)(player.getCurrentFuel() * GameConstants.SPACE_STORM_FUEL_LOSS_PERCENT);
        player.consumeFuel(fuelLost);
        return true; 
    }
}