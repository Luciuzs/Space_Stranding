package com.spacecourier.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.spacecourier.game.models.Player;
import com.spacecourier.game.constants.GameConstants;
import com.spacecourier.game.interfaces.Renderable;


public class PlayerStatsRenderer implements Renderable {
    
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final Player player;
    
    public PlayerStatsRenderer(ShapeRenderer shapeRenderer, BitmapFont font, Player player) {
        this.shapeRenderer = shapeRenderer;
        this.font = font;
        this.player = player;
    }
    
    @Override
    public void render(SpriteBatch batch, Matrix4 projectionMatrix) {
        float screenHeight = Gdx.graphics.getHeight();
        
        float panelX = 20f;
        float panelY = screenHeight - GameConstants.STATS_PANEL_HEIGHT - 20f;
        
        renderPanelBackground(projectionMatrix, panelX, panelY);
        renderStatsContent(batch, panelX, panelY);
    }
    
    private void renderPanelBackground(Matrix4 projectionMatrix, float panelX, float panelY) {
        shapeRenderer.setProjectionMatrix(projectionMatrix);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(GameConstants.COLOR_BLACK_SEMI[0], 
                              GameConstants.COLOR_BLACK_SEMI[1], 
                              GameConstants.COLOR_BLACK_SEMI[2], 
                              GameConstants.COLOR_BLACK_SEMI[3]);
        shapeRenderer.rect(panelX, panelY, GameConstants.STATS_PANEL_WIDTH, GameConstants.STATS_PANEL_HEIGHT);
        shapeRenderer.end();
        
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1f, 1f, 1f, 0.8f);
        shapeRenderer.rect(panelX, panelY, GameConstants.STATS_PANEL_WIDTH, GameConstants.STATS_PANEL_HEIGHT);
        shapeRenderer.end();
    }
    
    private void renderStatsContent(SpriteBatch batch, float panelX, float panelY) {
        batch.begin();
        
        float textX = panelX + GameConstants.STATS_PANEL_PADDING;
        float currentY = panelY + GameConstants.STATS_PANEL_HEIGHT - GameConstants.STATS_PANEL_PADDING;
        
        
        renderTitle(batch, textX, currentY, panelX);
        currentY -= GameConstants.STATS_LINE_HEIGHT + 10f;
        
        
        currentY = renderFuelBar(batch, textX, currentY, panelX);
        currentY -= 45f;
        
        
        renderGoldInfo(batch, textX, currentY);
        currentY -= GameConstants.STATS_LINE_HEIGHT;
        
        renderLocationInfo(batch, textX, currentY);
        currentY -= GameConstants.STATS_LINE_HEIGHT;
        
        renderVisitedInfo(batch, textX, currentY);
        
        font.getData().setScale(GameConstants.FONT_SCALE_NORMAL);
        batch.end();
    }
    
    private void renderTitle(SpriteBatch batch, float textX, float y, float panelX) {
        font.getData().setScale(GameConstants.FONT_SCALE_MEDIUM_LARGE);
        font.setColor(Color.YELLOW);
        GlyphLayout titleLayout = new GlyphLayout(font, "Player Stats");
        font.draw(batch, titleLayout, textX, y);
        font.getData().setScale(GameConstants.FONT_SCALE_MEDIUM);
        font.setColor(Color.WHITE);
    }
    
    private float renderFuelBar(SpriteBatch batch, float textX, float currentY, float panelX) {
        font.getData().setScale(GameConstants.FONT_SCALE_SMALL_MEDIUM);
        String fuelLabel = "Fuel:";
        GlyphLayout fuelLabelLayout = new GlyphLayout(font, fuelLabel);
        font.draw(batch, fuelLabelLayout, textX, currentY);
        batch.end();
        
        
        float barWidth = GameConstants.STATS_PANEL_WIDTH - (GameConstants.STATS_PANEL_PADDING * 2) - fuelLabelLayout.width - 20f;
        float barHeight = 25f;
        float barX = textX + fuelLabelLayout.width + 10f;
        float barY = currentY - barHeight;
        
        renderFuelBarGraphics(barX, barY, barWidth, barHeight);
        
        batch.begin();
        renderFuelText(batch, barX, barY);
        
        return currentY - barHeight;
    }
    
    private void renderFuelBarGraphics(float barX, float barY, float barWidth, float barHeight) {
        float fuelPercent = Math.max(0f, Math.min(1f, player.getCurrentFuel() / (float)GameConstants.MAX_FUEL));
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
    }
    
    private void renderFuelText(SpriteBatch batch, float barX, float barY) {
        font.getData().setScale(GameConstants.FONT_SCALE_VERY_SMALL);
        font.setColor(Color.WHITE);
        String fuelText = player.getCurrentFuel() + " L";
        GlyphLayout fuelTextLayout = new GlyphLayout(font, fuelText);
        float fuelTextX = barX + 5f;
        float fuelTextY = barY + 20f;
        font.draw(batch, fuelTextLayout, fuelTextX, fuelTextY);
    }
    
    private void renderGoldInfo(SpriteBatch batch, float textX, float y) {
        font.getData().setScale(GameConstants.FONT_SCALE_MEDIUM);
        String goldText = "Gold: " + player.getGold();
        GlyphLayout goldLayout = new GlyphLayout(font, goldText);
        font.draw(batch, goldLayout, textX, y);
    }
    
    private void renderLocationInfo(SpriteBatch batch, float textX, float y) {
        String location = player.getCurrentPlanet() != null ? player.getCurrentPlanet() : "Space";
        String planetText = "Location: " + location;
        GlyphLayout planetLayout = new GlyphLayout(font, planetText);
        font.draw(batch, planetLayout, textX, y);
    }
    
    private void renderVisitedInfo(SpriteBatch batch, float textX, float y) {
        String visitedText = "Visited: " + player.getVisitedPlanetCount() + " planets";
        GlyphLayout visitedLayout = new GlyphLayout(font, visitedText);
        font.draw(batch, visitedLayout, textX, y);
    }
}