package com.spacecourier.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
    
    private Player player;
    private MenuRenderer menuRenderer;
    private GameRenderer gameRenderer;
    private RouletteWheel rouletteWheel;
    private DangerPopup dangerPopup;
    private GameInputHandler inputHandler;
    private boolean showDangerPopup = false;

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
        
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        camera.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
        camera.update();
        
        player = new Player("Earth");
        
        menuRenderer = new MenuRenderer(batch, shapeRenderer, font, background);
        gameRenderer = new GameRenderer(batch, shapeRenderer, font, gameBackground, earthBackground, spaceTravelBackground, player);
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
            showDangerPopup = true;
            rouletteWheel.clearJustFinishedDanger();
        }
        
        if (inputHandler.getCurrentState() == GameState.MENU) {
            renderMenu();
        } else if (inputHandler.getCurrentState() == GameState.GAME) {
            renderGame();
        }
    }
    
    private void renderMenu() {
        menuRenderer.render(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    
    private void renderGame() {
        gameRenderer.render(inputHandler.isShowEarthBackground(), 
                           inputHandler.isShowTravelBackground(), 
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
    }
    
    @Override
    public boolean keyDown(int keycode) {
        return false;
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
        if (showDangerPopup) {
            showDangerPopup = false;
            return true;
        }
        
        return inputHandler.handleTouchDown(screenX, screenY);
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
        return false;
    }
    
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
