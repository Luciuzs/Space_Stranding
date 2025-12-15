package com.spacecourier.game;

import com.spacecourier.game.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Player Fuel Tests")
class PlayerFuelTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Earth");
    }

    @Test
    @DisplayName("Player should start with 100 fuel")
    void testInitialFuel() {
        assertEquals(100, player.getCurrentFuel(), "Initial fuel should be 100");
    }

    @Test
    @DisplayName("Consuming fuel should reduce fuel amount")
    void testConsumeFuel() {
        player.consumeFuel(30);
        assertEquals(70, player.getCurrentFuel(), "Fuel should be reduced by 30");
    }

    @Test
    @DisplayName("Fuel should not go below zero")
    void testFuelCannotGoNegative() {
        player.consumeFuel(150);
        assertEquals(0, player.getCurrentFuel(), "Fuel cannot go below 0");
    }

    @Test
    @DisplayName("Adding fuel should increase fuel amount")
    void testAddFuel() {
        player.consumeFuel(40);
        player.addFuel(20);
        assertEquals(80, player.getCurrentFuel(), "Fuel should be 80 after consuming 40 and adding 20");
    }

    @Test
    @DisplayName("Player can travel when fuel is sufficient")
    void testCanTravelWithSufficientFuel() {
        assertTrue(player.canTravel(50), "Player should be able to travel with 50 fuel cost when having 100 fuel");
    }

    @Test
    @DisplayName("Player cannot travel when fuel is insufficient")
    void testCannotTravelWithInsufficientFuel() {
        player.consumeFuel(80);
        assertFalse(player.canTravel(50), "Player should not be able to travel with 50 fuel cost when having only 20 fuel");
    }

    @Test
    @DisplayName("Setting fuel directly should work correctly")
    void testSetFuel() {
        player.setCurrentFuel(75);
        assertEquals(75, player.getCurrentFuel(), "Fuel should be set to 75");
    }

    @Test
    @DisplayName("Setting negative fuel should result in zero")
    void testSetNegativeFuel() {
        player.setCurrentFuel(-20);
        assertEquals(0, player.getCurrentFuel(), "Setting negative fuel should result in 0");
    }
}