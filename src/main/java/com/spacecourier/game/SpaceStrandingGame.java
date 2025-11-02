package com.spacecourier.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import java.util.HashMap;
import java.util.Map;

public class SpaceStrandingGame extends ApplicationAdapter implements InputProcessor {
    
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Space Stranding");
        config.setWindowedMode(1920, 1080);
        config.useVsync(true);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new SpaceStrandingGame(), config);
    }
    
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;
    
    private Texture background;
    private Texture gameBackground;
    private Texture earthBackground;
    private Texture spaceTravelBackground;
    private Map<String, Texture> planetBackgrounds;
    
    private Player player;
    private MenuRenderer menuRenderer;
    private GameRenderer gameRenderer;
    private RouletteWheel rouletteWheel;
    private DangerPopup dangerPopup;
    private GameInputHandler inputHandler;
    private boolean showDangerPopup = false;
    private SpaceEvent currentEvent = null;
    private boolean isFullscreen = false;
    private static final int WINDOWED_WIDTH = 1920;
    private static final int WINDOWED_HEIGHT = 1080;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        
        background = new Texture("images/Space_Backround.jpeg");
        gameBackground = new Texture("images/Game_Backround.jpeg");
        earthBackground = new Texture("images/Earth_Backround.jpeg");
        spaceTravelBackground = new Texture("images/Space_Travel.jpeg");
        
        planetBackgrounds = new HashMap<>();
        planetBackgrounds.put("Earth", earthBackground);
        planetBackgrounds.put("Mars", new Texture("images/Mars_Backround.jpeg"));
        planetBackgrounds.put("Jupiter", new Texture("images/Jupiter_Backround.jpeg"));
        planetBackgrounds.put("Saturn", new Texture("images/Saturn_Backround.jpeg"));
        planetBackgrounds.put("Venus", new Texture("images/Venus_Backround.jpeg"));
        
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        camera.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
        camera.update();
        
        player = new Player(null);
        
        menuRenderer = new MenuRenderer(batch, shapeRenderer, font, background);
        gameRenderer = new GameRenderer(batch, shapeRenderer, font, gameBackground, earthBackground, spaceTravelBackground, player, planetBackgrounds);
        rouletteWheel = new RouletteWheel(shapeRenderer, font, batch);
        dangerPopup = new DangerPopup(shapeRenderer, font, batch);
        inputHandler = new GameInputHandler(GameState.MENU, "Earth", menuRenderer, gameRenderer, rouletteWheel, player);
        
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        rouletteWheel.update();
        
        if (rouletteWheel.justFinishedDanger()) {
            currentEvent = EventManager.generateRandomEvent();
            dangerPopup.setEvent(currentEvent);
            showDangerPopup = true;
            rouletteWheel.clearJustFinishedDanger();
        }
        
        if (inputHandler.getCurrentState() == GameState.MENU) {
            renderMenu();
        } else if (inputHandler.getCurrentState() == GameState.GAME) {
            renderGame();
        } else if (inputHandler.getCurrentState() == GameState.GAME_OVER) {
            renderGameOver();
        } else if (inputHandler.getCurrentState() == GameState.WIN) {
            renderWin();
        }
    }
    
    private void renderMenu() {
        menuRenderer.render(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    
    private void renderGameOver() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        font.getData().setScale(4.0f);
        font.setColor(com.badlogic.gdx.graphics.Color.RED);
        
        String gameOverText = "GAME OVER";
        com.badlogic.gdx.graphics.g2d.GlyphLayout layout = new com.badlogic.gdx.graphics.g2d.GlyphLayout(font, gameOverText);
        float x = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float y = Gdx.graphics.getHeight() / 2f + layout.height;
        font.draw(batch, layout, x, y);
        
        font.getData().setScale(2.0f);
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        String reasonText = "Pirates stole all your gold!";
        com.badlogic.gdx.graphics.g2d.GlyphLayout reasonLayout = new com.badlogic.gdx.graphics.g2d.GlyphLayout(font, reasonText);
        float reasonX = (Gdx.graphics.getWidth() - reasonLayout.width) / 2f;
        float reasonY = y - 80f;
        font.draw(batch, reasonLayout, reasonX, reasonY);
        
        font.getData().setScale(1.5f);
        String restartText = "Click to return to menu";
        com.badlogic.gdx.graphics.g2d.GlyphLayout restartLayout = new com.badlogic.gdx.graphics.g2d.GlyphLayout(font, restartText);
        float restartX = (Gdx.graphics.getWidth() - restartLayout.width) / 2f;
        float restartY = reasonY - 60f;
        font.draw(batch, restartLayout, restartX, restartY);
        
        font.getData().setScale(1.0f);
        batch.end();
    }
    
    private void renderWin() {
        Gdx.gl.glClearColor(0, 0.2f, 0, 1);
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        font.getData().setScale(4.0f);
        font.setColor(com.badlogic.gdx.graphics.Color.GOLD);
        
        String winText = "VICTORY!";
        com.badlogic.gdx.graphics.g2d.GlyphLayout layout = new com.badlogic.gdx.graphics.g2d.GlyphLayout(font, winText);
        float x = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float y = Gdx.graphics.getHeight() / 2f + layout.height;
        font.draw(batch, layout, x, y);
        
        font.getData().setScale(2.0f);
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        String reasonText = "You completed all required routes!";
        com.badlogic.gdx.graphics.g2d.GlyphLayout reasonLayout = new com.badlogic.gdx.graphics.g2d.GlyphLayout(font, reasonText);
        float reasonX = (Gdx.graphics.getWidth() - reasonLayout.width) / 2f;
        float reasonY = y - 80f;
        font.draw(batch, reasonLayout, reasonX, reasonY);
        
        font.getData().setScale(1.5f);
        String restartText = "Click to return to menu";
        com.badlogic.gdx.graphics.g2d.GlyphLayout restartLayout = new com.badlogic.gdx.graphics.g2d.GlyphLayout(font, restartText);
        float restartX = (Gdx.graphics.getWidth() - restartLayout.width) / 2f;
        float restartY = reasonY - 60f;
        font.draw(batch, restartLayout, restartX, restartY);
        
        font.getData().setScale(1.0f);
        batch.end();
    }
    
    private void renderGame() {
        gameRenderer.render(inputHandler.isShowEarthBackground(), 
                           inputHandler.isShowTravelBackground(),
                           inputHandler.isShowFuelMessage(),
                           inputHandler.getFuelCostMultiplier(),
                           inputHandler.isShowEarthHoverPopup(),
                           camera.combined);
        
        if (inputHandler.isShowTravelBackground()) {
            rouletteWheel.render(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera.combined);
            
            if (showDangerPopup) {
                dangerPopup.render(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera.combined);
            }
        }
    }
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(width / 2f, height / 2f, 0);
        camera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        background.dispose();
        gameBackground.dispose();
        earthBackground.dispose();
        spaceTravelBackground.dispose();
        for (Texture texture : planetBackgrounds.values()) {
            if (texture != earthBackground) {
                texture.dispose();
            }
        }
    }
    
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.F11) {
            toggleFullscreen();
            return true;
        }
        return false;
    }
    
    private void toggleFullscreen() {
        if (!isFullscreen) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            isFullscreen = true;
        } else {
            Gdx.graphics.setWindowedMode(WINDOWED_WIDTH, WINDOWED_HEIGHT);
            isFullscreen = false;
        }
    }
    
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    
    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (inputHandler.getCurrentState() == GameState.GAME_OVER) {
            inputHandler.setCurrentState(GameState.MENU);
            player.reset(null);
            return true;
        }
        
        if (inputHandler.getCurrentState() == GameState.WIN) {
            inputHandler.setCurrentState(GameState.MENU);
            player.reset(null);
            return true;
        }
        
        if (showDangerPopup) {
            if (currentEvent != null) {
                boolean travelCompleted = handleEvent(currentEvent);
                if (inputHandler.getCurrentState() != GameState.GAME_OVER) {
                    if (travelCompleted && inputHandler.getTravelOrigin() != null && player.getCurrentPlanet() != null) {
                        player.updateRouteProgress(inputHandler.getTravelOrigin(), player.getCurrentPlanet());
                        
                        if (player.hasWon()) {
                            inputHandler.setCurrentState(GameState.WIN);
                        }
                        
                        inputHandler.clearTravelOrigin();
                    }
                    
                    inputHandler.setShowTravelBackground(false);
                }
            }
            showDangerPopup = false;
            currentEvent = null;
            dangerPopup.setEvent(null);
            return true;
        }
        
        return inputHandler.handleTouchDown(screenX, screenY);
    }
    
    private boolean handleEvent(SpaceEvent event) {
        switch (event.type) {
            case PIRATE_ATTACK:
                player.setGold(0);
                inputHandler.setCurrentState(GameState.GAME_OVER);
                inputHandler.setShowTravelBackground(false);
                return false;
                
            case SPACE_STORM:
                int fuelLost20 = (int)(player.getCurrentFuel() * 0.2f);
                player.consumeFuel(fuelLost20);
                return true;
                
            case FUEL_LEAK:
                int fuelLost30 = (int)(player.getCurrentFuel() * 0.3f);
                player.consumeFuel(fuelLost30);
                return true;
                
            case NAVIGATION_ERROR:
                String previousPlanet = player.getPreviousPlanet();
                if (previousPlanet != null) {
                    int refundFuel = inputHandler.getLastTravelFuelCost();
                    if (refundFuel > 0) {
                        player.addFuel(refundFuel);
                        inputHandler.clearLastTravelFuelCost();
                    }
                    inputHandler.clearTravelOrigin();
                    player.setCurrentPlanetWithoutTracking(previousPlanet);
                    inputHandler.setCurrentPlanet(previousPlanet);
                    inputHandler.setFuelCostMultiplier(1.5f);
                    if (previousPlanet.equals("Earth")) {
                        inputHandler.setShowEarthBackground(true);
                    }
                }
                return false;
        }
        return false;
    }
    
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    
    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (inputHandler.getCurrentState() == GameState.GAME) {
            inputHandler.updateMouseHover(screenX, screenY);
        }
        return false;
    }
    
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
