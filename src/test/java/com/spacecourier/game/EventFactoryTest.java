package com.spacecourier.game;

import com.spacecourier.game.events.EventFactory;
import com.spacecourier.game.events.EventType;
import com.spacecourier.game.events.SpaceEvent;
import com.spacecourier.game.events.PirateAttackEvent;
import com.spacecourier.game.events.SpaceStormEvent;
import com.spacecourier.game.events.FuelLeakEvent;
import com.spacecourier.game.events.NavigationErrorEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Event Factory Tests")
class EventFactoryTest {

    @Test
    @DisplayName("Factory should create PIRATE_ATTACK event")
    void testCreatePirateAttack() {
        SpaceEvent event = EventFactory.createEvent(EventType.PIRATE_ATTACK);
        assertNotNull(event, "Event should not be null");
        assertTrue(event instanceof PirateAttackEvent, "Event should be PirateAttackEvent");
        assertEquals(EventType.PIRATE_ATTACK, event.getType(), "Event type should be PIRATE_ATTACK");
    }

    @Test
    @DisplayName("Factory should create SPACE_STORM event")
    void testCreateSpaceStorm() {
        SpaceEvent event = EventFactory.createEvent(EventType.SPACE_STORM);
        assertNotNull(event, "Event should not be null");
        assertTrue(event instanceof SpaceStormEvent, "Event should be SpaceStormEvent");
        assertEquals(EventType.SPACE_STORM, event.getType(), "Event type should be SPACE_STORM");
    }

    @Test
    @DisplayName("Factory should create FUEL_LEAK event")
    void testCreateFuelLeak() {
        SpaceEvent event = EventFactory.createEvent(EventType.FUEL_LEAK);
        assertNotNull(event, "Event should not be null");
        assertTrue(event instanceof FuelLeakEvent, "Event should be FuelLeakEvent");
        assertEquals(EventType.FUEL_LEAK, event.getType(), "Event type should be FUEL_LEAK");
    }

    @Test
    @DisplayName("Factory should create NAVIGATION_ERROR event")
    void testCreateNavigationError() {
        SpaceEvent event = EventFactory.createEvent(EventType.NAVIGATION_ERROR);
        assertNotNull(event, "Event should not be null");
        assertTrue(event instanceof NavigationErrorEvent, "Event should be NavigationErrorEvent");
        assertEquals(EventType.NAVIGATION_ERROR, event.getType(), "Event type should be NAVIGATION_ERROR");
    }

    @Test
    @DisplayName("Factory should create random events successfully")
    void testCreateRandomEvent() {
        SpaceEvent event = EventFactory.createRandomEvent();
        assertNotNull(event, "Random event should not be null");
        assertNotNull(event.getType(), "Random event should have a type");
        assertNotNull(event.getName(), "Random event should have a name");
        assertNotNull(event.getDescription(), "Random event should have a description");
    }

    @Test
    @DisplayName("Factory should demonstrate polymorphism")
    void testPolymorphism() {
        // Create multiple events and store them in a list as SpaceEvent type
        java.util.List<SpaceEvent> events = new java.util.ArrayList<>();

        for (int i = 0; i < 20; i++) {
            events.add(EventFactory.createRandomEvent());
        }

        // All events should be valid SpaceEvent objects
        for (SpaceEvent event : events) {
            assertNotNull(event, "Event should not be null");
            assertNotNull(event.getType(), "Event should have a type");
            assertNotNull(event.getName(), "Event should have a name");
            assertNotNull(event.getDescription(), "Event should have a description");
        }

        assertEquals(20, events.size(), "Should have created 20 events");
    }
}