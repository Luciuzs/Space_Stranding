package com.spacecourier.game.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public interface Renderable {
    void render(SpriteBatch batch, Matrix4 projectionMatrix);
}