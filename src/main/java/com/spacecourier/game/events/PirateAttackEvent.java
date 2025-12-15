package com.spacecourier.game.events;

import com.spacecourier.game.models.Player;
import com.spacecourier.game.GameInputHandler;
import com.spacecourier.game.GameState;


public class PirateAttackEvent extends SpaceEvent {
    
    public PirateAttackEvent() {
        super(EventType.PIRATE_ATTACK, 
              "Pirate Attack!", 
              "Pirates have stolen all your gold! Game Over.");
    }
    
    @Override
    public boolean apply(Player player, GameInputHandler inputHandler) {
        player.setGold(0);
        if (inputHandler != null) {
            inputHandler.setCurrentState(GameState.GAME_OVER);
            inputHandler.setShowTravelBackground(false);
        }
        return false; 
    }
}