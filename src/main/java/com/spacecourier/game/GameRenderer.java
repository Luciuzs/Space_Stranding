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

public class GameRenderer {
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final Texture gameBackground;
    private final Texture earthBackground;
    private final Texture spaceTravelBackground;
    private final Player player;
    
    public float earthCenterX, earthCenterY, earthRadius;
    
    private ArrayList<PlanetOptionBounds> planetOptionBounds = new ArrayList<>();
    
    public GameRenderer(SpriteBatch batch, ShapeRenderer shapeRenderer, BitmapFont font,
                       Texture gameBackground, Texture earthBackground, Texture spaceTravelBackground,
                       Player player) {
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.font = font;
        this.gameBackground = gameBackground;
        this.earthBackground = earthBackground;
        this.spaceTravelBackground = spaceTravelBackground;
        this.player = player;
    }
    
    public void render(boolean showEarthBackground, boolean showTravelBackground, com.badlogic.gdx.math.Matrix4 cameraCombined) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        if (showTravelBackground) {
            batch.draw(spaceTravelBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else if (showEarthBackground) {
            batch.draw(earthBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else {
            batch.draw(gameBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        batch.end();
        
        renderPlayerStats(cameraCombined);
        
        if (!showEarthBackground && !showTravelBackground) {
            float earthX = 1225f;
            float earthY = 892f;
            earthRadius = 90f;
            
            earthCenterX = earthX;
            earthCenterY = earthY;
            
            shapeRenderer.setProjectionMatrix(cameraCombined);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1f, 1f, 1f, 1f);
            shapeRenderer.circle(earthX, earthY, earthRadius);
            shapeRenderer.end();
        } else if (showEarthBackground && !showTravelBackground) {
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
            String currentPlanetText = "Current: " + player.getCurrentPlanet();
            GlyphLayout currentLayout = new GlyphLayout(font, currentPlanetText);
            float currentX = boxX + (boxWidth - currentLayout.width) / 2f;
            float currentY = titleY - 50f;
            font.draw(batch, currentLayout, currentX, currentY);
            
            font.getData().setScale(1.3f);
            float startY = currentY - 80f;
            float optionSpacing = 100f;
            planetOptionBounds.clear();
            
            String[] availablePlanets = PlanetManager.getPlanetNames();
            for (int i = 0; i < availablePlanets.length; i++) {
                String planetName = availablePlanets[i];
                int fuelCost = PlanetManager.getFuelCost(planetName);
                int dangerRating = PlanetManager.getDangerRating(planetName);
                String planetInfo = planetName + " - Fuel: " + fuelCost + " | Danger: " + dangerRating + "/10";
                
                GlyphLayout planetLayout = new GlyphLayout(font, planetInfo);
                float planetX = boxX + (boxWidth - planetLayout.width) / 2f;
                float planetY = startY - (i * optionSpacing);
                
                font.draw(batch, planetLayout, planetX, planetY);
                
                PlanetOptionBounds bounds = new PlanetOptionBounds();
                bounds.x = planetX;
                bounds.y = planetY - planetLayout.height;
                bounds.width = planetLayout.width;
                bounds.height = planetLayout.height + 20f;
                bounds.planetName = planetName;
                planetOptionBounds.add(bounds);
            }
            
            font.getData().setScale(1.0f);
            batch.end();
        }
    }
    
    private void renderPlayerStats(com.badlogic.gdx.math.Matrix4 cameraCombined) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        
        float panelWidth = 300f;
        float panelHeight = 300f;
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
        
        String fuelText = "Fuel: " + player.getCurrentFuel();
        GlyphLayout fuelLayout = new GlyphLayout(font, fuelText);
        font.draw(batch, fuelLayout, textX, startY);
        startY -= lineHeight;
        
        String cargoText = "Cargo: " + (player.hasCargo() ? "Yes" : "No");
        GlyphLayout cargoLayout = new GlyphLayout(font, cargoText);
        font.draw(batch, cargoLayout, textX, startY);
        startY -= lineHeight;
        
        String planetText = "Location: " + player.getCurrentPlanet();
        GlyphLayout planetLayout = new GlyphLayout(font, planetText);
        font.draw(batch, planetLayout, textX, startY);
        startY -= lineHeight;
        
        String visitedText = "Visited: " + player.getVisitedPlanetCount() + " planets";
        GlyphLayout visitedLayout = new GlyphLayout(font, visitedText);
        font.draw(batch, visitedLayout, textX, startY);
        
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

