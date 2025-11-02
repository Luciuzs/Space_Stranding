// Danger box klase:  pop up langas
package com.spacecourier.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DangerPopup {
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final SpriteBatch batch;
    
    public DangerPopup(ShapeRenderer shapeRenderer, BitmapFont font, SpriteBatch batch) {
        this.shapeRenderer = shapeRenderer;
        this.font = font;
        this.batch = batch;
    }
    
    public void render(float screenWidth, float screenHeight, com.badlogic.gdx.math.Matrix4 cameraCombined) {
        float boxWidth = 600f;
        float boxHeight = 400f;
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
        shapeRenderer.setColor(0.2f, 0f, 0f, 0.95f);
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
        shapeRenderer.end();
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1f, 0f, 0f, 1f);
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
        shapeRenderer.end();
        batch.begin();
        font.setColor(Color.RED);
        font.getData().setScale(3.0f);
        
        String title = "DANGER!";
        GlyphLayout titleLayout = new GlyphLayout(font, title);
        float titleX = boxX + (boxWidth - titleLayout.width) / 2f;
        float titleY = boxY + boxHeight - 80f;
        font.draw(batch, titleLayout, titleX, titleY);
        
        font.getData().setScale(2.0f);
        String message = "You encountered danger!";
        GlyphLayout messageLayout = new GlyphLayout(font, message);
        float messageX = boxX + (boxWidth - messageLayout.width) / 2f;
        float messageY = titleY - 80f;
        font.draw(batch, messageLayout, messageX, messageY);
        
        font.getData().setScale(1.5f);
        String clickText = "Click to continue...";
        GlyphLayout clickLayout = new GlyphLayout(font, clickText);
        float clickX = boxX + (boxWidth - clickLayout.width) / 2f;
        float clickY = boxY + 80f;
        font.draw(batch, clickLayout, clickX, clickY);
        
        font.getData().setScale(1.0f);
        batch.end();
    }
}

