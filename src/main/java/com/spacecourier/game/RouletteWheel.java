// Danger rulete: logika ir rezultatai ruletes
package com.spacecourier.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class RouletteWheel {
    private float wheelRotation = 0f;
    private boolean isWheelSpinning = false;
    private float spinSpeed = 0f;
    private String wheelResult = "";
    private final float wheelRadius = 150f;
    private float currentSafeSegmentSize = 108f;
    private boolean justFinishedDanger = false;
    
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final SpriteBatch batch;
    
    public RouletteWheel(ShapeRenderer shapeRenderer, BitmapFont font, SpriteBatch batch) {
        this.shapeRenderer = shapeRenderer;
        this.font = font;
        this.batch = batch;
    }
    
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        
        if (isWheelSpinning) {
            wheelRotation -= spinSpeed * deltaTime;
            spinSpeed *= 0.98f;
            
            if (spinSpeed < 10f) {
                isWheelSpinning = false;
                wheelResult = calculateWheelResult();
                justFinishedDanger = wheelResult.equals("Danger");
            }
        }
    }
    
    public void render(float screenWidth, float screenHeight, com.badlogic.gdx.math.Matrix4 cameraCombined) {
        float wheelX = screenWidth / 2f;
        float wheelY = screenHeight / 2f;
        
        shapeRenderer.setProjectionMatrix(cameraCombined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1f, 1f, 1f, 0.9f);
        shapeRenderer.circle(wheelX, wheelY, wheelRadius);
        shapeRenderer.end();
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0f, 0f, 0f, 1f);
        shapeRenderer.circle(wheelX, wheelY, wheelRadius);
        shapeRenderer.end();
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        shapeRenderer.setColor(0f, 0.8f, 0f, 0.8f);
        float safeSegments = currentSafeSegmentSize;
        for (int i = 0; i < safeSegments; i++) {
            float angle1 = (wheelRotation + 90f + i) * (float) Math.PI / 180f;
            float angle2 = (wheelRotation + 90f + i + 1) * (float) Math.PI / 180f;
            
            float x1 = wheelX + (float) Math.cos(angle1) * wheelRadius;
            float y1 = wheelY + (float) Math.sin(angle1) * wheelRadius;
            float x2 = wheelX + (float) Math.cos(angle2) * wheelRadius;
            float y2 = wheelY + (float) Math.sin(angle2) * wheelRadius;
            
            shapeRenderer.triangle(wheelX, wheelY, x1, y1, x2, y2);
        }
        
        shapeRenderer.setColor(0.8f, 0f, 0f, 0.8f);
        float dangerSegments = 360f - safeSegments;
        for (int i = 0; i < dangerSegments; i++) {
            float angle1 = (wheelRotation + 90f + safeSegments + i) * (float) Math.PI / 180f;
            float angle2 = (wheelRotation + 90f + safeSegments + i + 1) * (float) Math.PI / 180f;
            
            float x1 = wheelX + (float) Math.cos(angle1) * wheelRadius;
            float y1 = wheelY + (float) Math.sin(angle1) * wheelRadius;
            float x2 = wheelX + (float) Math.cos(angle2) * wheelRadius;
            float y2 = wheelY + (float) Math.sin(angle2) * wheelRadius;
            
            shapeRenderer.triangle(wheelX, wheelY, x1, y1, x2, y2);
        }
        shapeRenderer.end();
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1f, 1f, 0f, 1f);
        float arrowX = wheelX;
        float arrowY = wheelY + wheelRadius + 30f;
        
        float arrowBodyWidth = 8f;
        float arrowBodyHeight = 20f;
        shapeRenderer.rect(arrowX - arrowBodyWidth / 2f, arrowY, arrowBodyWidth, arrowBodyHeight);
        
        float arrowHeadSize = 15f;
        shapeRenderer.triangle(
            arrowX, arrowY,
            arrowX - arrowHeadSize / 2f, arrowY + arrowHeadSize,
            arrowX + arrowHeadSize / 2f, arrowY + arrowHeadSize
        );
        shapeRenderer.end();
        
        batch.begin();
        if (!isWheelSpinning && !wheelResult.isEmpty()) {
            font.getData().setScale(2.0f);
            font.setColor(wheelResult.equals("Safe") ? Color.GREEN : Color.RED);
            GlyphLayout resultLayout = new GlyphLayout(font, wheelResult);
            float resultX = wheelX - resultLayout.width / 2f;
            float resultY = wheelY + resultLayout.height / 2f;
            font.draw(batch, resultLayout, resultX, resultY);
        }
        
        font.getData().setScale(1.2f);
        font.setColor(Color.BLACK);
        
        float safeAngle = (wheelRotation + 90f + currentSafeSegmentSize / 2f) * (float) Math.PI / 180f;
        GlyphLayout safeLayout = new GlyphLayout(font, "Safe");
        float safeX = wheelX + (float) Math.cos(safeAngle) * (wheelRadius * 0.6f) - safeLayout.width / 2f;
        float safeY = wheelY + (float) Math.sin(safeAngle) * (wheelRadius * 0.6f) + safeLayout.height / 2f;
        font.draw(batch, safeLayout, safeX, safeY);
        
        float dangerAngle = (wheelRotation + 90f + currentSafeSegmentSize + (360f - currentSafeSegmentSize) / 2f) * (float) Math.PI / 180f;
        GlyphLayout dangerLayout = new GlyphLayout(font, "Danger");
        float dangerX = wheelX + (float) Math.cos(dangerAngle) * (wheelRadius * 0.6f) - dangerLayout.width / 2f;
        float dangerY = wheelY + (float) Math.sin(dangerAngle) * (wheelRadius * 0.6f) + dangerLayout.height / 2f;
        font.draw(batch, dangerLayout, dangerX, dangerY);
        
        font.getData().setScale(1.0f);
        batch.end();
    }
    
    private String calculateWheelResult() {
        float normalizedRotation = (wheelRotation % 360f + 360f) % 360f;
        
        float topAngle = 90f;
        float safeStart = (normalizedRotation + 90f) % 360f;
        float safeEnd = (normalizedRotation + 90f + currentSafeSegmentSize) % 360f;
        
        boolean isSafe = false;
        if (safeEnd < safeStart) {
            isSafe = (topAngle >= safeStart || topAngle < safeEnd);
        } else {
            isSafe = (topAngle >= safeStart && topAngle < safeEnd);
        }
        
        return isSafe ? "Safe" : "Danger";
    }
    
    public void startSpin(int dangerRating) {
        isWheelSpinning = true;
        spinSpeed = 500f + (float) Math.random() * 300f;
        wheelResult = "";
        
        float safePercentage = (10f - dangerRating) / 10f;
        currentSafeSegmentSize = safePercentage * 360f;
    }
    
    public boolean isSpinning() {
        return isWheelSpinning;
    }
    
    public String getResult() {
        return wheelResult;
    }
    
    public boolean isDanger() {
        return wheelResult.equals("Danger");
    }
    
    public void clearResult() {
        wheelResult = "";
        justFinishedDanger = false;
    }
    
    public boolean justFinishedDanger() {
        return justFinishedDanger;
    }
    
    public void clearJustFinishedDanger() {
        justFinishedDanger = false;
    }
}

