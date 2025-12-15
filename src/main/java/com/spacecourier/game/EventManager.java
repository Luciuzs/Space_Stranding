package com.spacecourier.game;

import com.spacecourier.game.events.EventFactory;
import com.spacecourier.game.events.SpaceEvent;

/**
 * Event Manager - delegates to EventFactory for backward compatibility.
 * @deprecated Use EventFactory directly instead
 */
@Deprecated
public class EventManager {
    
    @Deprecated
    public static SpaceEvent generateRandomEvent() {
        return EventFactory.createRandomEvent();
    }
}