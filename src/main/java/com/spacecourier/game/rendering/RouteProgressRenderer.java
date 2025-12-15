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
import com.spacecourier.game.models.RouteSequence;
import com.spacecourier.game.constants.GameConstants;
import com.spacecourier.game.interfaces.Renderable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RouteProgressRenderer implements Renderable {
    
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final Player player;
    
    public RouteProgressRenderer(ShapeRenderer shapeRenderer, BitmapFont font, Player player) {
        this.shapeRenderer = shapeRenderer;
        this.font = font;
        this.player = player;
    }
    
    @Override
    public void render(SpriteBatch batch, Matrix4 projectionMatrix) {
        List<RouteSequence> displayRoutes = getDisplayRoutes();
        
        if (displayRoutes.isEmpty()) {
            return;
        }
        
        float panelHeight = calculatePanelHeight(displayRoutes);
        float[] panelBounds = calculatePanelBounds(panelHeight);
        
        renderPanelBackground(projectionMatrix, panelBounds);
        renderPanelContent(batch, panelBounds, displayRoutes);
    }
    
    private List<RouteSequence> getDisplayRoutes() {
        List<RouteSequence> allRoutes = RouteSequence.getWinRoutes();
        List<RouteSequence> displayRoutes = new ArrayList<>();
        String selected = player.getSelectedWinPlanet();
        
        for (RouteSequence route : allRoutes) {
            if (selected == null) {
                if (route.getName().contains("Earth") || route.getName().contains("Mars")) {
                    displayRoutes.add(route);
                }
            } else {
                if (route.getName().contains(selected)) {
                    displayRoutes.add(route);
                }
            }
        }
        
        return displayRoutes;
    }
    
    private float calculatePanelHeight(List<RouteSequence> routes) {
        return GameConstants.ROUTE_PANEL_BASE_HEIGHT + 
               (routes.size() * GameConstants.ROUTE_PANEL_HEIGHT_PER_ROUTE);
    }
    
    private float[] calculatePanelBounds(float panelHeight) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        
        float panelX = screenWidth - GameConstants.ROUTE_PANEL_WIDTH - 20f;
        float panelY = screenHeight - panelHeight - 20f;
        
        return new float[] {panelX, panelY, GameConstants.ROUTE_PANEL_WIDTH, panelHeight};
    }
    
    private void renderPanelBackground(Matrix4 projectionMatrix, float[] bounds) {
        float panelX = bounds[0];
        float panelY = bounds[1];
        float panelWidth = bounds[2];
        float panelHeight = bounds[3];
        
        shapeRenderer.setProjectionMatrix(projectionMatrix);
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
    }
    
    private void renderPanelContent(SpriteBatch batch, float[] bounds, List<RouteSequence> routes) {
        float panelX = bounds[0];
        float panelY = bounds[1];
        float panelWidth = bounds[2];
        float panelHeight = bounds[3];
        
        batch.begin();
        font.setColor(Color.WHITE);
        font.getData().setScale(GameConstants.FONT_SCALE_MEDIUM);
        
        // Title
        String title = "Win Routes";
        GlyphLayout titleLayout = new GlyphLayout(font, title);
        float titleX = panelX + (panelWidth - titleLayout.width) / 2f;
        float titleY = panelY + panelHeight - 25f;
        font.draw(batch, titleLayout, titleX, titleY);
        
        float currentY = titleY - 40f;
        font.getData().setScale(GameConstants.FONT_SCALE_NORMAL);
        
        Map<String, Integer> routeProgress = player.getRouteProgress();
        String selected = player.getSelectedWinPlanet();
        
        renderRoutesByPlanet(batch, routes, routeProgress, selected, panelX, currentY, panelWidth);
        
        font.setColor(Color.WHITE);
        font.getData().setScale(GameConstants.FONT_SCALE_NORMAL);
        batch.end();
    }
    
    private void renderRoutesByPlanet(SpriteBatch batch, List<RouteSequence> routes, 
                                     Map<String, Integer> routeProgress, String selected,
                                     float panelX, float startY, float panelWidth) {
        float currentY = startY;
        
        if (selected == null || selected.equals("Earth")) {
            currentY = renderPlanetRoutes(batch, routes, routeProgress, "Earth", 
                                        panelX, currentY, panelWidth, Color.CYAN);
        }
        
        if (selected == null || selected.equals("Mars")) {
            renderPlanetRoutes(batch, routes, routeProgress, "Mars", 
                             panelX, currentY, panelWidth, Color.ORANGE);
        }
    }
    
    private float renderPlanetRoutes(SpriteBatch batch, List<RouteSequence> allRoutes,
                                    Map<String, Integer> routeProgress, String planetName,
                                    float panelX, float startY, float panelWidth, Color titleColor) {
        List<RouteSequence> planetRoutes = filterRoutesByPlanet(allRoutes, planetName);
        
        if (planetRoutes.isEmpty()) {
            return startY;
        }
        
        float currentY = startY;
        
        // Section title
        font.getData().setScale(GameConstants.FONT_SCALE_SMALL);
        font.setColor(titleColor);
        String sectionTitle = planetName + " Routes:";
        GlyphLayout titleLayout = new GlyphLayout(font, sectionTitle);
        font.draw(batch, titleLayout, panelX + 15f, currentY);
        currentY -= 30f;
        
        // Individual routes
        font.getData().setScale(GameConstants.FONT_SCALE_NORMAL);
        font.setColor(Color.WHITE);
        
        for (RouteSequence route : planetRoutes) {
            currentY = drawRouteProgress(batch, route, routeProgress, panelX, currentY, panelWidth);
        }
        
        return currentY - 15f;
    }
    
    private List<RouteSequence> filterRoutesByPlanet(List<RouteSequence> routes, String planetName) {
        List<RouteSequence> filtered = new ArrayList<>();
        for (RouteSequence route : routes) {
            if (route.getName().contains(planetName)) {
                filtered.add(route);
            }
        }
        return filtered;
    }
    
    private float drawRouteProgress(SpriteBatch batch, RouteSequence route, 
                                   Map<String, Integer> routeProgress,
                                   float panelX, float currentY, float panelWidth) {
        List<String> planets = route.getPlanets();
        int progress = routeProgress.getOrDefault(route.getName(), 0);
        int totalSteps = planets.size() - 1;
        boolean isCompleted = progress >= totalSteps;
        
        String routeText = buildRouteText(planets, progress);
        String statusText = buildStatusText(isCompleted, totalSteps, progress);
        
        if (isCompleted) {
            font.setColor(Color.GREEN);
        } else {
            font.setColor(Color.WHITE);
        }
        
        return renderRouteTextWrapped(batch, routeText, statusText, panelX, currentY, panelWidth);
    }
    
    private String buildRouteText(List<String> planets, int progress) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < planets.size(); i++) {
            if (i > 0) {
                sb.append(" → ");
            }
            if (i <= progress) {
                sb.append("[").append(planets.get(i)).append("]");
            } else {
                sb.append(planets.get(i));
            }
        }
        return sb.toString();
    }
    
    private String buildStatusText(boolean completed, int totalSteps, int progress) {
        if (completed) {
            return " ✓";
        } else {
            int remaining = totalSteps - progress;
            return " (" + remaining + " left)";
        }
    }
    
    private float renderRouteTextWrapped(SpriteBatch batch, String routeText, String statusText,
                                        float panelX, float currentY, float panelWidth) {
        float routeX = panelX + 15f;
        float maxWidth = panelWidth - 30f;
        
        String fullText = routeText + statusText;
        GlyphLayout fullLayout = new GlyphLayout(font, fullText);
        
        if (fullLayout.width <= maxWidth) {
            font.draw(batch, fullLayout, routeX, currentY);
            return currentY - 45f;
        } else {
            GlyphLayout routeLayout = new GlyphLayout(font, routeText);
            font.draw(batch, routeLayout, routeX, currentY);
            currentY -= 25f;
            
            GlyphLayout statusLayout = new GlyphLayout(font, statusText);
            font.draw(batch, statusLayout, routeX, currentY);
            return currentY - 25f;
        }
    }
}