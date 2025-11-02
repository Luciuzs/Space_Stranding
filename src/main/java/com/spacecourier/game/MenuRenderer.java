// Menu screen klase - Game pradzios ekrano menu
package com.spacecourier.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MenuRenderer {
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final Texture background;
    
    public float exitButtonX, exitButtonY, exitButtonWidth, exitButtonHeight;
    public float startButtonX, startButtonY, startButtonWidth, startButtonHeight;
    
    public MenuRenderer(SpriteBatch batch, ShapeRenderer shapeRenderer, BitmapFont font, Texture background) {
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.font = font;
        this.background = background;
    }
    
    public void render(float screenWidth, float screenHeight) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, screenWidth, screenHeight);
        batch.end();
        
        font.getData().setScale(2.0f);
        String title = "SPACE STRANDING";
        
        GlyphLayout layout = new GlyphLayout(font, title);
        
        float padding = 60f;
        float boxWidth = (layout.width + padding * 10);
        float boxHeight = (layout.height + padding * 12);
        float boxX = (screenWidth - boxWidth) / 2f;
        float boxY = (screenHeight - boxHeight) / 2f;
        
        float titleX = boxX + (boxWidth - layout.width) / 2f;
        float titleY = boxY + boxHeight - padding - layout.height / 2f;
        
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
        font.draw(batch, layout, titleX, titleY);
        
        font.getData().setScale(1.5f);
        String startText = "Start";
        GlyphLayout startLayout = new GlyphLayout(font, startText);
        float startX = boxX + (boxWidth - startLayout.width) / 2f;
        float startY = boxY + (boxHeight - startLayout.height) / 2f;
        font.draw(batch, startLayout, startX, startY);
        
        startButtonX = startX;
        startButtonY = startY - startLayout.height;
        startButtonWidth = startLayout.width;
        startButtonHeight = startLayout.height;
        
        font.getData().setScale(1.5f);
        
        String exitText = "Exit";
        GlyphLayout exitLayout = new GlyphLayout(font, exitText);
        float exitButtonPadding = 20f;
        float exitX = boxX + boxWidth - exitLayout.width - exitButtonPadding;
        float exitY = boxY + exitButtonPadding + exitLayout.height;
        
        font.draw(batch, exitLayout, exitX, exitY);
        
        exitButtonX = exitX;
        exitButtonY = exitY - exitLayout.height;
        exitButtonWidth = exitLayout.width;
        exitButtonHeight = exitLayout.height;
        
        font.getData().setScale(1.0f);
        
        batch.end();
    }
}

