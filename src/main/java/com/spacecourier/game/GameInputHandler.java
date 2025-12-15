// Input klase - migtuku ir paspaudimo atvejai
package com.spacecourier.game;

import com.spacecourier.game.models.Player;
import com.spacecourier.game.models.Planet;
import com.spacecourier.game.models.Route;
import com.spacecourier.game.managers.PlanetManager;

import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import java.util.List;

public class GameInputHandler {
    private GameState currentState;
    private boolean showEarthBackground;
    private boolean showTravelBackground;
    private String currentPlanet;
    private boolean showDangerPopup;
    private boolean showFuelMessage;
    private int lastTravelFuelCost = 0;
    private float fuelCostMultiplier = 1.0f;
    private String travelOrigin = null;
    private int pendingGoldReward = 0;
    private boolean showEarthHoverPopup = false;
	private boolean showMarsHoverPopup = false;
	private boolean hardMode = false;
    private boolean shouldStartMiniGame = false;
    private int miniGameDangerRating = 0;
    
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
			currentPlanet = null;
			player.reset(null);
			player.setSelectedWinPlanet(null);
			rouletteWheel.clearResult();
			hardMode = false;
			return true;
		}
        
        return false;
    }
    
    private boolean handleGameClick(int screenX, float worldY) {
        if (showFuelMessage) {
            if (screenX >= gameRenderer.fuelBuyButtonX && 
                screenX <= gameRenderer.fuelBuyButtonX + gameRenderer.fuelBuyButtonWidth &&
                worldY >= gameRenderer.fuelBuyButtonY && 
                worldY <= gameRenderer.fuelBuyButtonY + gameRenderer.fuelBuyButtonHeight) {
                if (player.getGold() >= 100) {
                    player.removeGold(100);
                    player.addFuel(20);
                    showFuelMessage = false;
                }
                return true;
            }
            
            if (screenX >= gameRenderer.fuelCancelButtonX && 
                screenX <= gameRenderer.fuelCancelButtonX + gameRenderer.fuelCancelButtonWidth &&
                worldY >= gameRenderer.fuelCancelButtonY && 
                worldY <= gameRenderer.fuelCancelButtonY + gameRenderer.fuelCancelButtonHeight) {
                showFuelMessage = false;
                return true;
            }
            
            return true;
        }
        
        if (showTravelBackground) {
            if (showDangerPopup) {
                showDangerPopup = false;
                return true;
            }
            if (!rouletteWheel.isSpinning()) {
                if (travelOrigin != null && player.getCurrentPlanet() != null) {
                    player.updateRouteProgress(travelOrigin, player.getCurrentPlanet());
                    if (pendingGoldReward > 0) {
                        player.addGold(pendingGoldReward);
                        pendingGoldReward = 0;
                    }
                    
                    if (player.hasWon()) {
                        currentState = GameState.WIN;
                        travelOrigin = null;
                        showTravelBackground = false;
                        rouletteWheel.clearResult();
                        return true;
                    }
                    
                    travelOrigin = null;
                }
                
                showTravelBackground = false;
                if (player.getCurrentPlanet() != null) {
                    if (player.getCurrentPlanet().equals("Earth")) {
                        showEarthBackground = true;
                    }
                }
                rouletteWheel.clearResult();
                return true;
            }
        } else if (showEarthBackground || (player.getCurrentPlanet() != null && !player.getCurrentPlanet().equals("Earth"))) {
            String fromPlanet = showEarthBackground ? "Earth" : player.getCurrentPlanet();
            List<Route> availableRoutes = PlanetManager.getAvailableRoutes(fromPlanet);
            
            ArrayList<GameRenderer.PlanetOptionBounds> bounds = gameRenderer.getPlanetOptionBounds();
            for (GameRenderer.PlanetOptionBounds bound : bounds) {
                if (screenX >= bound.x && screenX <= bound.x + bound.width &&
                    worldY >= bound.y && worldY <= bound.y + bound.height) {
                    Route selectedRoute = null;
                    for (Route route : availableRoutes) {
                        if (route.to.name.equals(bound.planetName)) {
                            selectedRoute = route;
                            break;
                        }
                    }
                    
                    if (selectedRoute != null) {
                        int baseFuelCost = selectedRoute.fuelCost;
                        int fuelCost = (int)(baseFuelCost * fuelCostMultiplier);
                        if (player.canTravel(fuelCost)) {
                            travelOrigin = fromPlanet;
                            player.consumeFuel(fuelCost);
                            lastTravelFuelCost = fuelCost;
                            currentPlanet = selectedRoute.to.name;
                            player.setCurrentPlanet(selectedRoute.to.name);
                            showEarthBackground = false;
                            showTravelBackground = true;
                            fuelCostMultiplier = 1.0f;
					int dangerRating = (int)(selectedRoute.riskLevel * 10f);
						if (hardMode) {
							dangerRating += 2;
						}
                            if (dangerRating < 1) dangerRating = 1;
                            if (dangerRating > 10) dangerRating = 10;
                            
                            pendingGoldReward = dangerRating * 10;
                            shouldStartMiniGame = true;
                            miniGameDangerRating = dangerRating;
                            return true;
                        } else {
                            showFuelMessage = true;
                            return true;
                        }
                    }
                    return true;
                }
            }
        } else {
            float dxEarth = screenX - gameRenderer.earthCenterX;
            float dyEarth = worldY - gameRenderer.earthCenterY;
            float distanceEarth = (float) Math.sqrt(dxEarth * dxEarth + dyEarth * dyEarth);
            
			if (distanceEarth <= gameRenderer.earthRadius) {
                if (player.getCurrentPlanet() == null) {
					player.setCurrentPlanet("Earth");
                    currentPlanet = "Earth";
					hardMode = false;
					player.setSelectedWinPlanet("Earth");
                }
                showEarthBackground = true;
                return true;
            }
            
            float dxMars = screenX - gameRenderer.marsCenterX;
            float dyMars = worldY - gameRenderer.marsCenterY;
            float distanceMars = (float) Math.sqrt(dxMars * dxMars + dyMars * dyMars);
            
			if (distanceMars <= gameRenderer.marsRadius) {
				player.setCurrentPlanet("Mars");
                currentPlanet = "Mars";
				hardMode = true;
				player.setSelectedWinPlanet("Mars");
                showEarthBackground = false;
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
    
    public boolean isShowFuelMessage() {
        return showFuelMessage;
    }
    
    public void setCurrentState(GameState state) {
        this.currentState = state;
    }
    
    public void setCurrentPlanet(String planet) {
        this.currentPlanet = planet;
    }
    
    public int getLastTravelFuelCost() {
        return lastTravelFuelCost;
    }
    
    public void clearLastTravelFuelCost() {
        lastTravelFuelCost = 0;
    }
    
    public float getFuelCostMultiplier() {
        return fuelCostMultiplier;
    }
    
    public void setFuelCostMultiplier(float multiplier) {
        this.fuelCostMultiplier = multiplier;
    }
    
    public void clearTravelOrigin() {
        travelOrigin = null;
    }
    
    public String getTravelOrigin() {
        return travelOrigin;
    }

    public int consumePendingGoldReward() {
        int reward = pendingGoldReward;
        pendingGoldReward = 0;
        return reward;
    }
    
    public void updateMouseHover(int screenX, int screenY) {
		if (currentState == GameState.GAME && 
			!showEarthBackground && !showTravelBackground && 
			player.getCurrentPlanet() == null) {
            
            float worldY = Gdx.graphics.getHeight() - screenY;
            
            float dxEarth = screenX - gameRenderer.earthCenterX;
            float dyEarth = worldY - gameRenderer.earthCenterY;
            float distanceEarth = (float) Math.sqrt(dxEarth * dxEarth + dyEarth * dyEarth);
            
			showEarthHoverPopup = distanceEarth <= gameRenderer.earthRadius;
			
			float dxMars = screenX - gameRenderer.marsCenterX;
			float dyMars = worldY - gameRenderer.marsCenterY;
			float distanceMars = (float) Math.sqrt(dxMars * dxMars + dyMars * dyMars);
			showMarsHoverPopup = distanceMars <= gameRenderer.marsRadius;
		} else if (currentState == GameState.MENU) {
			float worldY = Gdx.graphics.getHeight() - screenY;
			boolean overNewGame = screenX >= menuRenderer.startButtonX && screenX <= menuRenderer.startButtonX + menuRenderer.startButtonWidth &&
							   worldY >= menuRenderer.startButtonY && worldY <= menuRenderer.startButtonY + menuRenderer.startButtonHeight;
			menuRenderer.setHoverNewGame(overNewGame);
		} else {
			showEarthHoverPopup = false;
			showMarsHoverPopup = false;
        }
    }
    
    public boolean isShowEarthHoverPopup() {
        return showEarthHoverPopup;
    }
	
	public boolean isShowMarsHoverPopup() {
		return showMarsHoverPopup;
	}
	
	public boolean shouldStartMiniGame() {
		return shouldStartMiniGame;
	}
	
	public int getMiniGameDangerRating() {
		return miniGameDangerRating;
	}
	
	public void clearMiniGameFlag() {
		shouldStartMiniGame = false;
		miniGameDangerRating = 0;
	}
}

