package com.spacecourier.game.events;

import com.spacecourier.game.models.Player;
import com.spacecourier.game.GameInputHandler;


public abstract class SpaceEvent {
    
    protected final EventType type;
    protected final String name;
    protected final String description;
    
  
    protected SpaceEvent(EventType type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }
    
  
    public abstract boolean apply(Player player, GameInputHandler inputHandler);
    
   
    public EventType getType() {
        return type;
    }
    
   
    public String getName() {
        return name;
    }
    
    
    public String getDescription() {
        return description;
    }
}