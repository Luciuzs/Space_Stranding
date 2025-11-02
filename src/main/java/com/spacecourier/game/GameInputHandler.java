// Input klase - migtuku ir paspaudimo atvejai
package com.spacecourier.game;

import com.badlogic.gdx.Gdx;
import java.util.ArrayList;

public class GameInputHandler {
    private GameState currentState;
    private boolean showEarthBackground;
    private boolean showTravelBackground;
    private String currentPlanet;
    private boolean showDangerPopup;
    
    private MenuRenderer menuRenderer;
    private GameRenderer gameRenderer;
    private RouletteWheel rouletteWheel;
    private Player player;
    
    public GameInputHandler(GameState initialState, String initialPlanet, 
                           MenuRenderer menuRenderer, GameRenderer gameRenderer, 
                           RouletteWheel rouletteWheel, Player player) {
        this.currentState = initialState;
        this.currentPlanet = initialPlanet;
        this.menuRenderer = menuRenderer;
        this.gameRenderer = gameRenderer;
        this.rouletteWheel = rouletteWheel;
        this.player = player;
    }
    
    public boolean handleTouchDown(int screenX, int screenY) {
        float worldY = Gdx.graphics.getHeight() - screenY;
        
        if (currentState == GameState.MENU) {
            return handleMenuClick(screenX, worldY);
        } else if (currentState == GameState.GAME) {
            return handleGameClick(screenX, worldY);
        }
        
        return false;
    }
    
    private boolean handleMenuClick(int screenX, float worldY) {
        if (screenX >= menuRenderer.exitButtonX && screenX <= menuRenderer.exitButtonX + menuRenderer.exitButtonWidth &&
            worldY >= menuRenderer.exitButtonY && worldY <= menuRenderer.exitButtonY + menuRenderer.exitButtonHeight) {
            Gdx.app.exit();
            return true;
        }

        if (screenX >= menuRenderer.startButtonX && screenX <= menuRenderer.startButtonX + menuRenderer.startButtonWidth &&
            worldY >= menuRenderer.startButtonY && worldY <= menuRenderer.startButtonY + menuRenderer.startButtonHeight) {
            currentState = GameState.GAME;
            showEarthBackground = false;
            showTravelBackground = false;
            currentPlanet = "Earth";
            player.reset("Earth");
            rouletteWheel.clearResult();
            return true;
        }
        
        return false;
    }
    
    private boolean handleGameClick(int screenX, float worldY) {
        if (showTravelBackground) {
            if (showDangerPopup) {
                showDangerPopup = false;
                return true;
            }
            if (!rouletteWheel.isSpinning()) {
                showTravelBackground = false;
                rouletteWheel.clearResult();
                return true;
            }
        } else if (showEarthBackground) {
            ArrayList<GameRenderer.PlanetOptionBounds> bounds = gameRenderer.getPlanetOptionBounds();
            for (GameRenderer.PlanetOptionBounds bound : bounds) {
                if (screenX >= bound.x && screenX <= bound.x + bound.width &&
                    worldY >= bound.y && worldY <= bound.y + bound.height) {
                    if (PlanetManager.planetExists(bound.planetName)) {
                        int fuelCost = PlanetManager.getFuelCost(bound.planetName);
                        if (player.canTravel(fuelCost)) {
                            player.consumeFuel(fuelCost);
                            currentPlanet = bound.planetName;
                            player.setCurrentPlanet(bound.planetName);
                            showEarthBackground = false;
                            showTravelBackground = true;
                            int dangerRating = PlanetManager.getDangerRating(currentPlanet);
                            rouletteWheel.startSpin(dangerRating);
                            return true;
                        }
                    }
                    return true;
                }
            }
        } else {
            float dx = screenX - gameRenderer.earthCenterX;
            float dy = worldY - gameRenderer.earthCenterY;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            
            if (distance <= gameRenderer.earthRadius) {
                showEarthBackground = true;
                return true;
            }
        }
        
        return false;
    }
    
    public GameState getCurrentState() {
        return currentState;
    }
    
    public boolean isShowEarthBackground() {
        return showEarthBackground;
    }
    
    public void setShowEarthBackground(boolean showEarthBackground) {
        this.showEarthBackground = showEarthBackground;
    }
    
    public boolean isShowTravelBackground() {
        return showTravelBackground;
    }
    
    public void setShowTravelBackground(boolean showTravelBackground) {
        this.showTravelBackground = showTravelBackground;
    }
    
    public String getCurrentPlanet() {
        return currentPlanet;
    }
    
    public boolean isShowDangerPopup() {
        return showDangerPopup;
    }
    
    public void setShowDangerPopup(boolean showDangerPopup) {
        this.showDangerPopup = showDangerPopup;
    }
    
    public RouletteWheel getRouletteWheel() {
        return rouletteWheel;
    }
}

