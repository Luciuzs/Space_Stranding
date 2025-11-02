// Render klases - draw arba render ekrano elementus
package com.spacecourier.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameRenderer {
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final Texture gameBackground;
    private final Texture earthBackground;
    private final Texture spaceTravelBackground;
    private final Player player;
    private final Map<String, Texture> planetBackgrounds;
    
    public float earthCenterX, earthCenterY, earthRadius;
    public float marsCenterX, marsCenterY, marsRadius;
    
    private ArrayList<PlanetOptionBounds> planetOptionBounds = new ArrayList<>();
    
    public GameRenderer(SpriteBatch batch, ShapeRenderer shapeRenderer, BitmapFont font,
                       Texture gameBackground, Texture earthBackground, Texture spaceTravelBackground,
                       Player player, Map<String, Texture> planetBackgrounds) {
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.font = font;
        this.gameBackground = gameBackground;
        this.earthBackground = earthBackground;
        this.spaceTravelBackground = spaceTravelBackground;
        this.player = player;
        this.planetBackgrounds = planetBackgrounds;
    }
    
    public void render(boolean showEarthBackground, boolean showTravelBackground, boolean showFuelMessage, float fuelCostMultiplier, boolean showEarthHoverPopup, com.badlogic.gdx.math.Matrix4 cameraCombined) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        if (showTravelBackground) {
            batch.draw(spaceTravelBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else if (showEarthBackground) {
            batch.draw(earthBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else {
            String currentPlanet = player.getCurrentPlanet();
            if (currentPlanet == null) {
                batch.draw(gameBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            } else {
                Texture planetBg = planetBackgrounds.get(currentPlanet);
                if (planetBg != null) {
                    batch.draw(planetBg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                } else {
                    batch.draw(gameBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                }
            }
        }
        batch.end();
        
        renderPlayerStats(cameraCombined);
        
        if (!showEarthBackground && !showTravelBackground && player.getCurrentPlanet() == null) {
            float earthX = 1225f;
            float earthY = 892f;
            earthRadius = 90f;
            
            earthCenterX = earthX;
            earthCenterY = earthY;
            
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            float marsX = screenWidth - 837f;
            float marsY = screenHeight - 805f;
            marsRadius = 107f;
            
            marsCenterX = marsX;
            marsCenterY = marsY;
            
            shapeRenderer.setProjectionMatrix(cameraCombined);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1f, 1f, 1f, 1f);
            shapeRenderer.circle(earthX, earthY, earthRadius);
            shapeRenderer.setColor(1f, 1f, 1f, 1f); 
            shapeRenderer.circle(marsX, marsY, marsRadius);
            shapeRenderer.end();
        }
        
        String currentPlanet = player.getCurrentPlanet();
        boolean shouldShowSelectionBox = ((showEarthBackground && currentPlanet != null && currentPlanet.equals("Earth")) ||
                                         (currentPlanet != null && !currentPlanet.equals("Earth"))) && !showTravelBackground;
        if (shouldShowSelectionBox) {
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            
            float boxWidth = 700f;
            float boxHeight = 900f;
            float boxX = (screenWidth - boxWidth) / 2f + 200f;
            float boxY = (screenHeight - boxHeight) / 2f;
            
            shapeRenderer.setProjectionMatrix(cameraCombined);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0f, 0f, 0f, 0.7f);
            shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
            shapeRenderer.end();
            
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1f, 1f, 1f, 0.8f);
            shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
            shapeRenderer.end();
            
            batch.begin();
            font.getData().setScale(1.8f);
            font.setColor(Color.WHITE);
            
            String title = "Select Destination Planet";
            GlyphLayout titleLayout = new GlyphLayout(font, title);
            float titleX = boxX + (boxWidth - titleLayout.width) / 2f;
            float titleY = boxY + boxHeight - 50f;
            font.draw(batch, titleLayout, titleX, titleY);
            
            font.getData().setScale(1.2f);
            String displayPlanet = showEarthBackground ? "Earth" : (currentPlanet != null ? currentPlanet : "Space");
            String currentPlanetText = "Current: " + displayPlanet;
            GlyphLayout currentLayout = new GlyphLayout(font, currentPlanetText);
            float currentX = boxX + (boxWidth - currentLayout.width) / 2f;
            float currentY = titleY - 50f;
            font.draw(batch, currentLayout, currentX, currentY);
            
            font.getData().setScale(1.3f);
            float startY = currentY - 80f;
            float optionSpacing = 100f;
            planetOptionBounds.clear();
            
            String currentPlanetName = showEarthBackground ? "Earth" : (currentPlanet != null ? currentPlanet : null);
            List<Route> availableRoutes = PlanetManager.getAvailableRoutes(currentPlanetName);
            
            if (availableRoutes.isEmpty()) {
                font.getData().setScale(1.2f);
                String noRoutesText = "No routes available from here";
                GlyphLayout noRoutesLayout = new GlyphLayout(font, noRoutesText);
                float noRoutesX = boxX + (boxWidth - noRoutesLayout.width) / 2f;
                font.draw(batch, noRoutesLayout, noRoutesX, startY);
            } else {
                for (int i = 0; i < availableRoutes.size(); i++) {
                    Route route = availableRoutes.get(i);
                    String destinationName = route.to.name;
                    int baseFuelCost = route.fuelCost;
                    int displayFuelCost = (int)(baseFuelCost * fuelCostMultiplier);
                    float riskPercent = route.riskLevel * 100f;
                    
                    String fuelDisplay = fuelCostMultiplier > 1.0f ? displayFuelCost + " (increased)" : String.valueOf(displayFuelCost);
                    String routeInfo = destinationName + " - Fuel: " + fuelDisplay + " | Risk: " + String.format("%.0f", riskPercent) + "%";
                    
                    GlyphLayout routeLayout = new GlyphLayout(font, routeInfo);
                    float routeX = boxX + (boxWidth - routeLayout.width) / 2f;
                    float routeY = startY - (i * optionSpacing);
                    
                    font.draw(batch, routeLayout, routeX, routeY);
                    
                    PlanetOptionBounds bounds = new PlanetOptionBounds();
                    bounds.x = routeX;
                    bounds.y = routeY - routeLayout.height;
                    bounds.width = routeLayout.width;
                    bounds.height = routeLayout.height + 20f;
                    bounds.planetName = destinationName;
                    planetOptionBounds.add(bounds);
                }
            }
            
            font.getData().setScale(1.0f);
            batch.end();
        }
        
        if (showFuelMessage) {
            renderFuelMessage(cameraCombined);
        }
        
        if (showEarthHoverPopup && !showEarthBackground && !showTravelBackground && player.getCurrentPlanet() == null) {
            renderEarthHoverPopup(cameraCombined);
        }
        
        if ((showEarthBackground || (player.getCurrentPlanet() != null)) && !showTravelBackground) {
            renderEarthRouteProgress(cameraCombined);
        }
    }
    
    private void renderEarthRouteProgress(com.badlogic.gdx.math.Matrix4 cameraCombined) {
        java.util.List<RouteSequence> allRoutes = RouteSequence.getWinRoutes();
        java.util.List<RouteSequence> displayRoutes = new java.util.ArrayList<>();
        
        for (RouteSequence route : allRoutes) {
            if (route.getName().contains("Earth") || route.getName().contains("Mars")) {
                displayRoutes.add(route);
            }
        }
        
        if (displayRoutes.isEmpty()) {
            return;
        }
        
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        
        float panelWidth = 380f;
        float panelHeight = 250f + (displayRoutes.size() * 50f);
        float panelX = screenWidth - panelWidth - 20f;
        float panelY = screenHeight - panelHeight - 20f;
        
        shapeRenderer.setProjectionMatrix(cameraCombined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.75f);
        shapeRenderer.rect(panelX, panelY, panelWidth, panelHeight);
        shapeRenderer.end();
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1f, 1f, 1f, 0.9f);
        shapeRenderer.rect(panelX, panelY, panelWidth, panelHeight);
        shapeRenderer.end();
        
        batch.begin();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);
        
        String title = "Win Routes";
        GlyphLayout titleLayout = new GlyphLayout(font, title);
        float titleX = panelX + (panelWidth - titleLayout.width) / 2f;
        float titleY = panelY + panelHeight - 25f;
        font.draw(batch, titleLayout, titleX, titleY);
        
        float currentY = titleY - 40f;
        font.getData().setScale(1.0f);
        
        java.util.Map<String, Integer> routeProgress = player.getRouteProgress();
        
        java.util.List<RouteSequence> earthRoutes = new java.util.ArrayList<>();
        java.util.List<RouteSequence> marsRoutes = new java.util.ArrayList<>();
        
        for (RouteSequence route : displayRoutes) {
            if (route.getName().contains("Earth")) {
                earthRoutes.add(route);
            } else if (route.getName().contains("Mars")) {
                marsRoutes.add(route);
            }
        }
        
        if (!earthRoutes.isEmpty()) {
            font.getData().setScale(1.2f);
            font.setColor(Color.CYAN);
            String earthSectionTitle = "Earth Routes:";
            GlyphLayout earthTitleLayout = new GlyphLayout(font, earthSectionTitle);
            float earthTitleX = panelX + 15f;
            font.draw(batch, earthTitleLayout, earthTitleX, currentY);
            currentY -= 30f;
            
            font.getData().setScale(1.0f);
            font.setColor(Color.WHITE);
            
            for (RouteSequence route : earthRoutes) {
                currentY = drawRouteProgress(route, routeProgress, panelX, currentY, panelWidth);
            }
            
            currentY -= 15f;
        }
        
        if (!marsRoutes.isEmpty()) {
            font.getData().setScale(1.2f);
            font.setColor(Color.ORANGE);
            String marsSectionTitle = "Mars Routes:";
            GlyphLayout marsTitleLayout = new GlyphLayout(font, marsSectionTitle);
            float marsTitleX = panelX + 15f;
            font.draw(batch, marsTitleLayout, marsTitleX, currentY);
            currentY -= 30f;
            
            font.getData().setScale(1.0f);
            font.setColor(Color.WHITE);
            
            for (RouteSequence route : marsRoutes) {
                currentY = drawRouteProgress(route, routeProgress, panelX, currentY, panelWidth);
            }
        }
        
        font.setColor(Color.WHITE);
        font.getData().setScale(1.0f);
        batch.end();
    }
    
    private float drawRouteProgress(RouteSequence route, java.util.Map<String, Integer> routeProgress, float panelX, float currentY, float panelWidth) {
        java.util.List<String> planets = route.getPlanets();
        int progress = routeProgress.getOrDefault(route.getName(), 0);
        int totalSteps = planets.size() - 1;
        boolean isCompleted = progress >= totalSteps;
        
        StringBuilder routeText = new StringBuilder();
        for (int i = 0; i < planets.size(); i++) {
            if (i > 0) {
                routeText.append(" → ");
            }
            if (i <= progress) {
                routeText.append("[");
                routeText.append(planets.get(i));
                routeText.append("]");
            } else {
                routeText.append(planets.get(i));
            }
        }
        
        String statusText;
        if (isCompleted) {
            statusText = " ✓";
            font.setColor(Color.GREEN);
        } else {
            int remaining = totalSteps - progress;
            statusText = " (" + remaining + " left)";
            font.setColor(Color.WHITE);
        }
        
        float routeX = panelX + 15f;
        float maxWidth = panelWidth - 30f;
        
        String fullText = routeText.toString() + statusText;
        GlyphLayout fullLayout = new GlyphLayout(font, fullText);
        
        if (fullLayout.width <= maxWidth) {
            font.draw(batch, fullLayout, routeX, currentY);
            currentY -= 45f;
        } else {
            GlyphLayout routeLayout = new GlyphLayout(font, routeText.toString());
            font.draw(batch, routeLayout, routeX, currentY);
            currentY -= 25f;
            
            GlyphLayout statusLayout = new GlyphLayout(font, statusText);
            font.draw(batch, statusLayout, routeX, currentY);
            currentY -= 25f;
        }
        
        return currentY;
    }
    
    private void renderEarthHoverPopup(com.badlogic.gdx.math.Matrix4 cameraCombined) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        
        java.util.List<RouteSequence> allRoutes = RouteSequence.getWinRoutes();
        java.util.List<RouteSequence> earthRoutes = new java.util.ArrayList<>();
        for (RouteSequence route : allRoutes) {
            if (route.getName().contains("Earth")) {
                earthRoutes.add(route);
            }
        }
        
        float popupX = earthCenterX + earthRadius + 20f;
        float popupY = earthCenterY;
        float popupWidth = 280f;
        float popupHeight = 200f + (earthRoutes.size() * 35f);
        
        if (popupX + popupWidth > screenWidth) {
            popupX = earthCenterX - earthRadius - popupWidth - 20f;
        }
        if (popupY + popupHeight > screenHeight) {
            popupY = screenHeight - popupHeight - 20f;
        }
        if (popupY < 0) {
            popupY = 20f;
        }
        
        shapeRenderer.setProjectionMatrix(cameraCombined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.85f);
        shapeRenderer.rect(popupX, popupY, popupWidth, popupHeight);
        shapeRenderer.end();
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1f, 1f, 1f, 1f);
        shapeRenderer.rect(popupX, popupY, popupWidth, popupHeight);
        shapeRenderer.end();
        
        batch.begin();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);
        
        String title = "Earth";
        GlyphLayout titleLayout = new GlyphLayout(font, title);
        float titleX = popupX + (popupWidth - titleLayout.width) / 2f;
        float titleY = popupY + popupHeight - 25f;
        font.draw(batch, titleLayout, titleX, titleY);
        
        font.getData().setScale(1.0f);
        float currentY = titleY - 35f;
        
        font.getData().setScale(1.1f);
        String winRoutesTitle = "Win Routes:";
        GlyphLayout routesTitleLayout = new GlyphLayout(font, winRoutesTitle);
        float routesTitleX = popupX + 10f;
        font.draw(batch, routesTitleLayout, routesTitleX, currentY);
        currentY -= 25f;
        
        font.getData().setScale(0.9f);
        for (RouteSequence route : earthRoutes) {
            java.util.List<String> planets = route.getPlanets();
            StringBuilder routeText = new StringBuilder();
            for (int i = 0; i < planets.size(); i++) {
                if (i > 0) {
                    routeText.append(" → ");
                }
                routeText.append(planets.get(i));
            }
            
            GlyphLayout routeLayout = new GlyphLayout(font, routeText.toString());
            float routeX = popupX + 10f;
            font.draw(batch, routeLayout, routeX, currentY);
            currentY -= 30f;
        }
        
        font.getData().setScale(1.0f);
        currentY -= 10f;
        
        String infoLine = "Click to visit Earth";
        GlyphLayout infoLayout = new GlyphLayout(font, infoLine);
        float infoX = popupX + (popupWidth - infoLayout.width) / 2f;
        font.draw(batch, infoLayout, infoX, currentY);
        
        font.getData().setScale(1.0f);
        batch.end();
    }
    
    private void renderPlayerStats(com.badlogic.gdx.math.Matrix4 cameraCombined) {
        float screenHeight = Gdx.graphics.getHeight();
        
        float panelWidth = 300f;
        float panelHeight = 330f;
        float panelX = 20f;
        float panelY = screenHeight - panelHeight - 20f;
        float padding = 15f;
        
        shapeRenderer.setProjectionMatrix(cameraCombined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.7f);
        shapeRenderer.rect(panelX, panelY, panelWidth, panelHeight);
        shapeRenderer.end();
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1f, 1f, 1f, 0.8f);
        shapeRenderer.rect(panelX, panelY, panelWidth, panelHeight);
        shapeRenderer.end();
        
        batch.begin();
        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);
        
        float startY = panelY + panelHeight - padding;
        float lineHeight = 50f;
        float textX = panelX + padding;
        
        font.getData().setScale(1.8f);
        font.setColor(Color.YELLOW);
        GlyphLayout titleLayout = new GlyphLayout(font, "Player Stats");
        font.draw(batch, titleLayout, textX, startY);
        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);
        
        startY -= lineHeight + 10f;
        
        font.getData().setScale(1.3f);
        String fuelLabel = "Fuel:";
        GlyphLayout fuelLabelLayout = new GlyphLayout(font, fuelLabel);
        font.draw(batch, fuelLabelLayout, textX, startY);
        batch.end();
        
        float barWidth = panelWidth - (padding * 2) - fuelLabelLayout.width - 20f;
        float barHeight = 25f;
        float barX = textX + fuelLabelLayout.width + 10f;
        float barY = startY - barHeight;
        float maxFuel = 100f;
        float fuelPercent = Math.max(0f, Math.min(1f, player.getCurrentFuel() / maxFuel));
        float filledWidth = barWidth * fuelPercent;
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 0.8f);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        shapeRenderer.end();
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1f, 1f, 1f, 0.8f);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        shapeRenderer.end();
        
        if (filledWidth > 0) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            if (fuelPercent > 0.5f) {
                shapeRenderer.setColor(0f, 1f, 0f, 0.9f);
            } else if (fuelPercent > 0.25f) {
                shapeRenderer.setColor(1f, 1f, 0f, 0.9f);
            } else {
                shapeRenderer.setColor(1f, 0f, 0f, 0.9f);
            }
            shapeRenderer.rect(barX, barY, filledWidth, barHeight);
            shapeRenderer.end();
        }
        
        batch.begin();
        font.getData().setScale(1.1f);
        font.setColor(Color.WHITE);
        String fuelText = player.getCurrentFuel() + " L";
        GlyphLayout fuelTextLayout = new GlyphLayout(font, fuelText);
        float fuelTextX = barX + 5f;
        float fuelTextY = barY + 20f;
        font.draw(batch, fuelTextLayout, fuelTextX, fuelTextY);
        
        startY = fuelTextY - 45f;
        
        font.getData().setScale(1.5f);
        String goldText = "Gold: " + player.getGold();
        GlyphLayout goldLayout = new GlyphLayout(font, goldText);
        font.draw(batch, goldLayout, textX, startY);
        startY -= lineHeight;
        
        String planetText = "Location: " + (player.getCurrentPlanet() != null ? player.getCurrentPlanet() : "Space");
        GlyphLayout planetLayout = new GlyphLayout(font, planetText);
        font.draw(batch, planetLayout, textX, startY);
        startY -= lineHeight;
        
        String visitedText = "Visited: " + player.getVisitedPlanetCount() + " planets";
        GlyphLayout visitedLayout = new GlyphLayout(font, visitedText);
        font.draw(batch, visitedLayout, textX, startY);
        
        font.getData().setScale(1.0f);
        batch.end();
    }
    
    private void renderFuelMessage(com.badlogic.gdx.math.Matrix4 cameraCombined) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        
        float boxWidth = 500f;
        float boxHeight = 200f;
        float boxX = (screenWidth - boxWidth) / 2f;
        float boxY = (screenHeight - boxHeight) / 2f;
        
        shapeRenderer.setProjectionMatrix(cameraCombined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.5f);
        shapeRenderer.rect(0, 0, screenWidth, screenHeight);
        shapeRenderer.end();
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.2f, 0.2f, 0f, 0.95f);
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
        shapeRenderer.end();
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1f, 1f, 0f, 1f);
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
        shapeRenderer.end();
        
        batch.begin();
        font.setColor(Color.YELLOW);
        font.getData().setScale(2.5f);
        
        String message = "Not enough fuel!";
        GlyphLayout messageLayout = new GlyphLayout(font, message);
        float messageX = boxX + (boxWidth - messageLayout.width) / 2f;
        float messageY = boxY + boxHeight - 60f;
        font.draw(batch, messageLayout, messageX, messageY);
        
        font.getData().setScale(1.5f);
        String clickText = "Click to continue...";
        GlyphLayout clickLayout = new GlyphLayout(font, clickText);
        float clickX = boxX + (boxWidth - clickLayout.width) / 2f;
        float clickY = boxY + 50f;
        font.draw(batch, clickLayout, clickX, clickY);
        
        font.getData().setScale(1.0f);
        batch.end();
    }
    
    public ArrayList<PlanetOptionBounds> getPlanetOptionBounds() {
        return planetOptionBounds;
    }
    
    public static class PlanetOptionBounds {
        public float x, y, width, height;
        public String planetName;
    }
}

