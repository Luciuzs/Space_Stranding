package com.spacecourier.game.events;

import com.spacecourier.game.models.Player;
import com.spacecourier.game.GameInputHandler;
import com.spacecourier.game.constants.GameConstants;


public class NavigationErrorEvent extends SpaceEvent {
    
    public NavigationErrorEvent() {
        super(EventType.NAVIGATION_ERROR,
              "Navigation Error",
              "Navigation systems malfunctioned! Returning to previous planet.");
    }
    
    @Override
    public boolean apply(Player player, GameInputHandler inputHandler) {
        String previousPlanet = player.getPreviousPlanet();
        
        if (previousPlanet != null) {
           
            int refundFuel = inputHandler.getLastTravelFuelCost();
            if (refundFuel > 0) {
                player.addFuel(refundFuel);
                inputHandler.clearLastTravelFuelCost();
            }
            
            
            inputHandler.clearTravelOrigin();
            player.setCurrentPlanetWithoutTracking(previousPlanet);
            inputHandler.setCurrentPlanet(previousPlanet);
            
          
            inputHandler.setFuelCostMultiplier(GameConstants.NAVIGATION_ERROR_FUEL_MULTIPLIER);
            
          
            if (previousPlanet.equals("Earth")) {
                inputHandler.setShowEarthBackground(true);
            }
        }
        
        return false;
    }
}