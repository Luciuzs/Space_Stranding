package com.spacecourier.game.models;

import com.spacecourier.game.constants.GameConstants;

public class Rock {
    private float x;
    private float y;
    private float speed;
    
    private static final float ROCK_SPAWN_OFFSET_Y = 200f;
    
    public Rock(float x, float speed, float screenHeight) {
        this.x = x;
        this.y = screenHeight + ROCK_SPAWN_OFFSET_Y;
        this.speed = speed;
    }
    
    public void update(float deltaTime) {
        y -= speed * deltaTime;
    }
    
    public boolean isOffScreen() {
        return y + GameConstants.ROCK_SIZE < 0;
    }
    
    public boolean checkCollision(float cursorX, float cursorY, float cursorWidth, float cursorHeight) {
        float cursorLeft = cursorX;
        float cursorRight = cursorX + cursorWidth;
        float cursorBottom = cursorY - cursorHeight + GameConstants.CURSOR_OFFSET_Y_COLLISION;
        float cursorTop = cursorY + GameConstants.CURSOR_OFFSET_Y_TOP;
        
        float collisionSize = GameConstants.ROCK_SIZE * GameConstants.ROCK_COLLISION_SCALE;
        float rockCenterX = x + GameConstants.ROCK_SIZE / 2f;
        float rockCenterY = y - GameConstants.ROCK_SIZE / 2f;
        
        float rockLeft = rockCenterX - collisionSize / 2f;
        float rockRight = rockCenterX + collisionSize / 2f;
        float rockBottom = rockCenterY - collisionSize / 2f;
        float rockTop = rockCenterY + collisionSize / 2f;
        
        return !(cursorRight < rockLeft || cursorLeft > rockRight || 
                cursorTop < rockBottom || cursorBottom > rockTop);
    }
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
}