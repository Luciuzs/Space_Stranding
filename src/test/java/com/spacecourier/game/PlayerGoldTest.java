package com.spacecourier.game;

import com.spacecourier.game.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Player Gold Tests")
class PlayerGoldTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Earth");
    }

    @Test
    @DisplayName("Player should start with 100 gold")
    void testInitialGold() {
        assertEquals(100, player.getGold(), "Initial gold should be 100");
    }

    @Test
    @DisplayName("Adding gold should increase gold amount")
    void testAddGold() {
        player.addGold(50);
        assertEquals(150, player.getGold(), "Gold should be 150 after adding 50");
    }

    @Test
    @DisplayName("Removing gold should decrease gold amount")
    void testRemoveGold() {
        player.removeGold(30);
        assertEquals(70, player.getGold(), "Gold should be 70 after removing 30");
    }

    @Test
    @DisplayName("Gold should not go below zero when removing")
    void testGoldCannotGoNegative() {
        player.removeGold(150);
        assertEquals(0, player.getGold(), "Gold cannot go below 0");
    }

    @Test
    @DisplayName("Setting gold directly should work correctly")
    void testSetGold() {
        player.setGold(200);
        assertEquals(200, player.getGold(), "Gold should be set to 200");
    }

    @Test
    @DisplayName("Setting negative gold should result in zero")
    void testSetNegativeGold() {
        player.setGold(-50);
        assertEquals(0, player.getGold(), "Setting negative gold should result in 0");
    }

    @Test
    @DisplayName("Player can afford purchase when gold is sufficient")
    void testCanAffordPurchase() {
        int itemCost = 80;
        assertTrue(player.getGold() >= itemCost, "Player should be able to afford item costing 80 gold");
    }

    @Test
    @DisplayName("Player cannot afford purchase when gold is insufficient")
    void testCannotAffordPurchase() {
        player.removeGold(60);
        int itemCost = 80;
        assertFalse(player.getGold() >= itemCost, "Player should not be able to afford item costing 80 gold when having only 40 gold");
    }
}