package com.spacecourier.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class SpaceTravel {
    private final SpriteBatch batch;
    private final Texture cursorTexture;
    private Texture rockTexture;
    private Texture spaceTravelBackground;
    private float scale = 0.20f;
    private int lastX = -1;
    private int lastY = -1;
    
    private ArrayList<Rock> rocks;
    private float gameTimer;
    private static final float GAME_DURATION = 5.0f;
    private boolean isMiniGameActive;
    private boolean hasFailed;
    private boolean hasCompleted;
    private Random random;
    
    private float rockSpawnTimer;
    private static final float MIN_SPAWN_INTERVAL = 0.1f;
    private static final float MAX_SPAWN_INTERVAL = 0.5f;
    private float nextSpawnInterval;
    
    private static final float ROCK_SIZE = 140f;
    private static final float ROCK_COLLISION_SCALE = 0.5f;
    private static final float ROCK_FALL_SPEED = 250f;
    private static final float ROCK_SPEED_INCREASE = 10f;
    
    private class Rock {
        float x;
        float y;
        float speed;
        
        Rock(float x, float speed, float screenHeight) {
            this.x = x;
            this.y = screenHeight + 200f;
            this.speed = speed;
        }
        
        void update(float deltaTime) {
            y -= speed * deltaTime;
        }
        
        boolean isOffScreen() {
            return y + ROCK_SIZE < 0;
        }
        
        boolean checkCollision(float cursorX, float cursorY, float cursorWidth, float cursorHeight) {
            float cursorLeft = cursorX;
            float cursorRight = cursorX + cursorWidth;
            float cursorBottom = cursorY - cursorHeight + 60f;
            float cursorTop = cursorY + 80f;
            
            float collisionSize = ROCK_SIZE * ROCK_COLLISION_SCALE;
            float rockCenterX = x + ROCK_SIZE / 2f;
            float rockCenterY = y - ROCK_SIZE / 2f;
            
            float rockLeft = rockCenterX - collisionSize / 2f;
            float rockRight = rockCenterX + collisionSize / 2f;
            float rockBottom = rockCenterY - collisionSize / 2f;
            float rockTop = rockCenterY + collisionSize / 2f;
            
            return !(cursorRight < rockLeft || cursorLeft > rockRight || 
                    cursorTop < rockBottom || cursorBottom > rockTop);
        }
    }

    public SpaceTravel(SpriteBatch batch, Texture cursorTexture) {
        this.batch = batch;
        this.cursorTexture = cursorTexture;
        this.rocks = new ArrayList<>();
        this.random = new Random();
        this.isMiniGameActive = false;
        this.hasFailed = false;
        this.hasCompleted = false;
    }
    
    public SpaceTravel(SpriteBatch batch, String cursorImagePath) {
        this.batch = batch;
        this.cursorTexture = new Texture(cursorImagePath);
        this.rocks = new ArrayList<>();
        this.random = new Random();
        this.isMiniGameActive = false;
        this.hasFailed = false;
        this.hasCompleted = false;
    }
    
    public void setMiniGameTextures(Texture rockTexture, Texture spaceTravelBackground) {
        this.rockTexture = rockTexture;
        this.spaceTravelBackground = spaceTravelBackground;
    }
    
    public void setScale(float scale) {
        this.scale = scale;
    }

    public void update() {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();

        if (x != lastX || y != lastY) {
            System.out.println("Cursor Position: X=" + x + ", Y=" + y);
            lastX = x;
            lastY = y;
        }
    }
    
    public void trackCursor() {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        System.out.println("Cursor Position: X=" + x + ", Y=" + y);
    }
    
    public void renderCursor(Matrix4 projectionMatrix) {
        int cursorX = getCursorX() - 75;
        int cursorY = Gdx.graphics.getHeight() - getCursorY();

        batch.setProjectionMatrix(projectionMatrix);
        batch.begin();
        float width = cursorTexture.getWidth() * scale;
        float height = cursorTexture.getHeight() * scale;
        batch.draw(cursorTexture, cursorX, cursorY, width, height);
        batch.end();
    }

    public int getCursorX() {
        return Gdx.input.getX();
    }
    
    public int getCursorY() {
        return Gdx.input.getY();
    }

    public void startMiniGame() {
        isMiniGameActive = true;
        hasFailed = false;
        hasCompleted = false;
        gameTimer = 0f;
        rockSpawnTimer = 0f;
        rocks.clear();
        nextSpawnInterval = MIN_SPAWN_INTERVAL + random.nextFloat() * (MAX_SPAWN_INTERVAL - MIN_SPAWN_INTERVAL);
    }
    
    public void updateMiniGame(float deltaTime) {
        if (!isMiniGameActive || hasFailed || hasCompleted) {
            return;
        }
        
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        
        gameTimer += deltaTime;
        
        if (gameTimer >= GAME_DURATION) {
            hasCompleted = true;
            isMiniGameActive = false;
            return;
        }
        
        rockSpawnTimer += deltaTime;
        if (rockSpawnTimer >= nextSpawnInterval) {
            float spawnX = random.nextFloat() * (screenWidth - ROCK_SIZE);
            float currentSpeed = ROCK_FALL_SPEED + (gameTimer / GAME_DURATION) * ROCK_SPEED_INCREASE * 20f;
            rocks.add(new Rock(spawnX, currentSpeed, screenHeight));
            rockSpawnTimer = 0f;
            float progress = gameTimer / GAME_DURATION;
            float minInterval = MIN_SPAWN_INTERVAL * (1f - progress * 0.5f);
            float maxInterval = MAX_SPAWN_INTERVAL * (1f - progress * 0.5f);
            nextSpawnInterval = minInterval + random.nextFloat() * (maxInterval - minInterval);
        }
        
        float cursorX = getCursorX() - 75;
        float cursorY = Gdx.graphics.getHeight() - getCursorY();
        float cursorWidth = cursorTexture.getWidth() * scale;
        float cursorHeight = cursorTexture.getHeight() * scale;
        
        Iterator<Rock> iterator = rocks.iterator();
        while (iterator.hasNext()) {
            Rock rock = iterator.next();
            rock.update(deltaTime);
            
            if (rock.checkCollision(cursorX, cursorY, cursorWidth, cursorHeight)) {
                hasFailed = true;
                isMiniGameActive = false;
                return;
            }
            
            if (rock.isOffScreen()) {
                iterator.remove();
            }
        }
    }
    
    public void renderMiniGame(Matrix4 projectionMatrix) {
        if (!isMiniGameActive && !hasFailed && !hasCompleted) {
            return;
        }
        
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        
        batch.setProjectionMatrix(projectionMatrix);
        batch.begin();
        
        if (spaceTravelBackground != null) {
            batch.draw(spaceTravelBackground, 0, 0, screenWidth, screenHeight);
        }
        
        if (rockTexture != null) {
            for (Rock rock : rocks) {
                batch.draw(rockTexture, rock.x, rock.y - ROCK_SIZE, ROCK_SIZE, ROCK_SIZE);
            }
        }
        
        batch.end();
    }
    
    public float getMiniGameTimeRemaining() {
        return Math.max(0, GAME_DURATION - gameTimer);
    }
    
    public void resetMiniGame() {
        isMiniGameActive = false;
        hasFailed = false;
        hasCompleted = false;
        gameTimer = 0f;
        rocks.clear();
    }
    
    public boolean isMiniGameActive() {
        return isMiniGameActive;
    }
    
    public boolean hasMiniGameFailed() {
        return hasFailed;
    }
    
    public boolean hasMiniGameCompleted() {
        return hasCompleted;
    }
    
    public void dispose() {
        if (cursorTexture != null) {
            cursorTexture.dispose();
        }
    }
}
