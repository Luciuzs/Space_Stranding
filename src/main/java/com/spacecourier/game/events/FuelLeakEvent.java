package com.spacecourier.game.events;

import com.spacecourier.game.models.Player;
import com.spacecourier.game.GameInputHandler;
import com.spacecourier.game.constants.GameConstants;


public class FuelLeakEvent extends SpaceEvent {
    
    public FuelLeakEvent() {
        super(EventType.FUEL_LEAK,
              "Fuel Leak",
              "Critical fuel leak detected! Lost 30% fuel.");
    }
    
    @Override
    public boolean apply(Player player, GameInputHandler inputHandler) {
        int fuelLost = (int)(player.getCurrentFuel() * GameConstants.FUEL_LEAK_LOSS_PERCENT);
        player.consumeFuel(fuelLost);
        return true; // Travel continues
    }
}