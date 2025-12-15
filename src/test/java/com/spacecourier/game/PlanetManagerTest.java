package com.spacecourier.game;

import com.spacecourier.game.managers.PlanetManager;
import com.spacecourier.game.models.Planet;
import com.spacecourier.game.models.Route;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Planet Manager Tests")
class PlanetManagerTest {

    @Test
    @DisplayName("Should retrieve planet by name")
    void testGetPlanetByName() {
        Planet earth = PlanetManager.getPlanetByName("Earth");
        assertNotNull(earth, "Earth should be found");
        assertEquals("Earth", earth.name, "Planet name should be Earth");

        Planet mars = PlanetManager.getPlanetByName("Mars");
        assertNotNull(mars, "Mars should be found");
        assertEquals("Mars", mars.name, "Planet name should be Mars");
    }

    @Test
    @DisplayName("Should check if planet exists")
    void testPlanetExists() {
        assertTrue(PlanetManager.planetExists("Earth"), "Earth should exist");
        assertTrue(PlanetManager.planetExists("Mars"), "Mars should exist");
        assertTrue(PlanetManager.planetExists("Jupiter"), "Jupiter should exist");
        assertFalse(PlanetManager.planetExists("Pluto"), "Pluto should not exist");
    }

    @Test
    @DisplayName("Earth should have 2 available routes")
    void testEarthRoutes() {
        List<Route> routes = PlanetManager.getAvailableRoutes("Earth");
        assertEquals(2, routes.size(), "Earth should have 2 available routes");
    }

    @Test
    @DisplayName("Mars should have 2 available routes")
    void testMarsRoutes() {
        List<Route> routes = PlanetManager.getAvailableRoutes("Mars");
        assertEquals(2, routes.size(), "Mars should have 2 available routes");
    }

    @Test
    @DisplayName("Earth should have fuel cost of 0")
    void testEarthFuelCost() {
        int fuelCost = PlanetManager.getFuelCost("Earth");
        assertEquals(0, fuelCost, "Earth should have fuel cost of 0");
    }

    @Test
    @DisplayName("Mars should have fuel cost of 20")
    void testMarsFuelCost() {
        int fuelCost = PlanetManager.getFuelCost("Mars");
        assertEquals(20, fuelCost, "Mars should have fuel cost of 20");
    }

    @Test
    @DisplayName("Earth should have danger rating of 1")
    void testEarthDangerRating() {
        int dangerRating = PlanetManager.getDangerRating("Earth");
        assertEquals(1, dangerRating, "Earth should have danger rating of 1");
    }

    @Test
    @DisplayName("Jupiter should have danger rating of 6")
    void testJupiterDangerRating() {
        int dangerRating = PlanetManager.getDangerRating("Jupiter");
        assertEquals(6, dangerRating, "Jupiter should have danger rating of 6");
    }
}