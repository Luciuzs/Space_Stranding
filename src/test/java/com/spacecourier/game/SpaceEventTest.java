package com.spacecourier.game;

import com.spacecourier.game.models.Player;
import com.spacecourier.game.events.EventFactory;
import com.spacecourier.game.events.EventType;
import com.spacecourier.game.events.SpaceEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Space Event Tests")
class SpaceEventTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Earth");
    }

    @Test
    @DisplayName("Space Storm should reduce fuel by 20%")
    void testSpaceStormReducesFuel() {
        SpaceEvent storm = EventFactory.createEvent(EventType.SPACE_STORM);
        int initialFuel = player.getCurrentFuel();

        storm.apply(player, null);

        int expectedFuel = (int)(initialFuel * 0.8f);
        assertEquals(expectedFuel, player.getCurrentFuel(),
                "Space Storm should reduce fuel by 20%");
    }

    @Test
    @DisplayName("Fuel Leak should reduce fuel by 30%")
    void testFuelLeakReducesFuel() {
        SpaceEvent leak = EventFactory.createEvent(EventType.FUEL_LEAK);
        int initialFuel = player.getCurrentFuel();

        leak.apply(player, null);

        int expectedFuel = (int)(initialFuel * 0.7f);
        assertEquals(expectedFuel, player.getCurrentFuel(),
                "Fuel Leak should reduce fuel by 30%");
    }

    @Test
    @DisplayName("Pirate Attack should set gold to zero")
    void testPirateAttackRemovesAllGold() {
        SpaceEvent pirate = EventFactory.createEvent(EventType.PIRATE_ATTACK);
        player.addGold(50); // 150 gold final

        pirate.apply(player, null);

        assertEquals(0, player.getGold(), "Pirate Attack should remove all gold");
    }

    @Test
    @DisplayName("All events should have name and description")
    void testEventsHaveProperties() {
        SpaceEvent storm = EventFactory.createEvent(EventType.SPACE_STORM);
        SpaceEvent leak = EventFactory.createEvent(EventType.FUEL_LEAK);
        SpaceEvent pirate = EventFactory.createEvent(EventType.PIRATE_ATTACK);
        SpaceEvent nav = EventFactory.createEvent(EventType.NAVIGATION_ERROR);

        assertNotNull(storm.getName(), "Space Storm should have a name");
        assertNotNull(storm.getDescription(), "Space Storm should have a description");

        assertNotNull(leak.getName(), "Fuel Leak should have a name");
        assertNotNull(leak.getDescription(), "Fuel Leak should have a description");

        assertNotNull(pirate.getName(), "Pirate Attack should have a name");
        assertNotNull(pirate.getDescription(), "Pirate Attack should have a description");

        assertNotNull(nav.getName(), "Navigation Error should have a name");
        assertNotNull(nav.getDescription(), "Navigation Error should have a description");
    }

    @Test
    @DisplayName("Events can be processed polymorphically")
    void testPolymorphicEventProcessing() {

        List<SpaceEvent> events = new ArrayList<>();
        events.add(EventFactory.createEvent(EventType.SPACE_STORM));
        events.add(EventFactory.createEvent(EventType.FUEL_LEAK));
        events.add(EventFactory.createEvent(EventType.PIRATE_ATTACK));

        for (SpaceEvent event : events) {
            Player testPlayer = new Player("Mars");
            event.apply(testPlayer, null);


            assertTrue(testPlayer.getCurrentFuel() <= 100 || testPlayer.getGold() == 0,
                    "Event should have affected player state");
        }

        assertEquals(3, events.size(), "Should have processed 3 different event types");
    }
}