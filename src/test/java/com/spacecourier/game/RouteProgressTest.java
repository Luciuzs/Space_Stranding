package com.spacecourier.game;

import com.spacecourier.game.models.Player;
import com.spacecourier.game.models.RouteSequence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Route Progress Tests")
class RouteProgressTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Earth");
    }

    @Test
    @DisplayName("Player should start with not won state")
    void testInitialWinState() {
        assertFalse(player.hasWon(), "Player should not have won initially");
    }

    @Test
    @DisplayName("Route progress should update correctly")
    void testRouteProgressUpdate() {
        boolean routeCompleted = player.updateRouteProgress("Earth", "Mars");

        assertFalse(routeCompleted, "First step should not complete the route");
        Map<String, Integer> progress = player.getRouteProgress();
        assertTrue(progress.get("Earth Route") > 0, "Earth route progress should have increased");
    }

    @Test
    @DisplayName("Player should win after completing all route steps")
    void testWinCondition() {
        // Complete Earth route: Earth → Mars → Jupiter → Saturn → Venus
        player.updateRouteProgress("Earth", "Mars");
        player.setCurrentPlanet("Mars");

        player.updateRouteProgress("Mars", "Jupiter");
        player.setCurrentPlanet("Jupiter");

        player.updateRouteProgress("Jupiter", "Saturn");
        player.setCurrentPlanet("Saturn");

        boolean completed = player.updateRouteProgress("Saturn", "Venus");

        assertTrue(completed, "Route should be completed after all steps");
        assertTrue(player.hasWon(), "Player should have won after completing the route");
    }

    @Test
    @DisplayName("Earth route should have correct planet sequence")
    void testEarthRouteSequence() {
        List<RouteSequence> routes = RouteSequence.getWinRoutes();
        RouteSequence earthRoute = routes.stream()
                .filter(r -> r.getName().equals("Earth Route"))
                .findFirst()
                .orElse(null);

        assertNotNull(earthRoute, "Earth route should exist");
        List<String> planets = earthRoute.getPlanets();

        assertEquals("Earth", planets.get(0), "First planet should be Earth");
        assertEquals("Mars", planets.get(1), "Second planet should be Mars");
        assertEquals("Jupiter", planets.get(2), "Third planet should be Jupiter");
        assertEquals("Saturn", planets.get(3), "Fourth planet should be Saturn");
        assertEquals("Venus", planets.get(4), "Fifth planet should be Venus");
    }

    @Test
    @DisplayName("Mars route should have correct planet sequence")
    void testMarsRouteSequence() {
        List<RouteSequence> routes = RouteSequence.getWinRoutes();
        RouteSequence marsRoute = routes.stream()
                .filter(r -> r.getName().equals("Mars Route"))
                .findFirst()
                .orElse(null);

        assertNotNull(marsRoute, "Mars route should exist");
        List<String> planets = marsRoute.getPlanets();

        assertEquals("Mars", planets.get(0), "First planet should be Mars");
        assertEquals("Jupiter", planets.get(1), "Second planet should be Jupiter");
        assertEquals("Saturn", planets.get(2), "Third planet should be Saturn");
        assertEquals("Venus", planets.get(3), "Fourth planet should be Venus");
        assertEquals("Earth", planets.get(4), "Fifth planet should be Earth");
    }

    @Test
    @DisplayName("Route progress should match start planets correctly")
    void testRouteMatchesStart() {
        List<RouteSequence> routes = RouteSequence.getWinRoutes();
        RouteSequence earthRoute = routes.stream()
                .filter(r -> r.getName().equals("Earth Route"))
                .findFirst()
                .orElse(null);

        assertNotNull(earthRoute, "Earth route should exist");
        assertTrue(earthRoute.matchesStart("Earth", "Mars"),
                "Earth route should match Earth → Mars");
        assertFalse(earthRoute.matchesStart("Mars", "Earth"),
                "Earth route should not match Mars → Earth");
    }

    @Test
    @DisplayName("Reset should clear route progress")
    void testResetClearsProgress() {
        player.updateRouteProgress("Earth", "Mars");
        player.setCurrentPlanet("Mars");
        player.updateRouteProgress("Mars", "Jupiter");

        player.reset("Earth");

        assertFalse(player.hasWon(), "Player should not have won after reset");
        Map<String, Integer> progress = player.getRouteProgress();
        assertEquals(0, progress.get("Earth Route").intValue(),
                "Route progress should be reset to 0");
    }

    @Test
    @DisplayName("Player should track selected win planet")
    void testSelectedWinPlanet() {
        assertEquals("Earth", player.getSelectedWinPlanet(),
                "Initial selected win planet should be Earth");

        player.setSelectedWinPlanet("Mars");
        assertEquals("Mars", player.getSelectedWinPlanet(),
                "Selected win planet should be Mars after setting");
    }
}