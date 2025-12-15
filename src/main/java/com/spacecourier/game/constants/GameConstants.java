package com.spacecourier.game.constants;

public final class GameConstants {
    
    // Prevent instantiation
    private GameConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
    
    // Player Configuration
    public static final int INITIAL_FUEL = 100;
    public static final int INITIAL_GOLD = 100;
    public static final int MAX_FUEL = 100;
    
    // Fuel Purchase
    public static final int FUEL_PURCHASE_COST = 100;
    public static final int FUEL_PURCHASE_AMOUNT = 20;
    
    // Event Effects
    public static final float SPACE_STORM_FUEL_LOSS_PERCENT = 0.2f;
    public static final float FUEL_LEAK_LOSS_PERCENT = 0.3f;
    public static final float NAVIGATION_ERROR_FUEL_MULTIPLIER = 1.5f;
    
    // Roulette Wheel
    public static final float ROULETTE_RADIUS = 150f;
    public static final float ROULETTE_INITIAL_SPIN_SPEED_MIN = 500f;
    public static final float ROULETTE_INITIAL_SPIN_SPEED_MAX = 800f;
    public static final float ROULETTE_SPIN_DECAY = 0.98f;
    public static final float ROULETTE_MIN_SPEED_TO_STOP = 10f;
    
    // Mini Game
    public static final float MINI_GAME_DURATION = 5.0f;
    public static final float ROCK_SIZE = 140f;
    public static final float ROCK_COLLISION_SCALE = 0.5f;
    public static final float ROCK_FALL_SPEED = 250f;
    public static final float ROCK_SPEED_INCREASE = 10f;
    public static final float MIN_SPAWN_INTERVAL = 0.1f;
    public static final float MAX_SPAWN_INTERVAL = 0.5f;
    
    // UI Dimensions
    public static final float PLANET_SELECTION_BOX_WIDTH = 700f;
    public static final float PLANET_SELECTION_BOX_HEIGHT = 900f;
    public static final float DANGER_POPUP_WIDTH = 600f;
    public static final float DANGER_POPUP_HEIGHT = 400f;
    public static final float FUEL_MESSAGE_BOX_WIDTH = 600f;
    public static final float FUEL_MESSAGE_BOX_HEIGHT = 300f;
    public static final float BUTTON_WIDTH = 200f;
    public static final float BUTTON_HEIGHT = 60f;
    public static final float BUTTON_SPACING = 20f;
    
    // Planet Radii
    public static final float EARTH_RADIUS = 90f;
    public static final float MARS_RADIUS = 107f;
    
    // Stats Panel
    public static final float STATS_PANEL_WIDTH = 300f;
    public static final float STATS_PANEL_HEIGHT = 330f;
    public static final float STATS_PANEL_PADDING = 15f;
    public static final float STATS_LINE_HEIGHT = 50f;
    
    // Route Progress Panel
    public static final float ROUTE_PANEL_WIDTH = 380f;
    public static final float ROUTE_PANEL_BASE_HEIGHT = 250f;
    public static final float ROUTE_PANEL_HEIGHT_PER_ROUTE = 50f;
    
    // Font Scales
    public static final float FONT_SCALE_HUGE = 4.0f;
    public static final float FONT_SCALE_VERY_LARGE = 3.0f;
    public static final float FONT_SCALE_LARGE = 2.0f;
    public static final float FONT_SCALE_MEDIUM_LARGE = 1.8f;
    public static final float FONT_SCALE_MEDIUM = 1.5f;
    public static final float FONT_SCALE_SMALL_MEDIUM = 1.3f;
    public static final float FONT_SCALE_SMALL = 1.2f;
    public static final float FONT_SCALE_VERY_SMALL = 1.1f;
    public static final float FONT_SCALE_TINY = 0.9f;
    public static final float FONT_SCALE_NORMAL = 1.0f;
    
    // Colors (RGBA)
    public static final float[] COLOR_BLACK_TRANSPARENT = {0f, 0f, 0f, 0.5f};
    public static final float[] COLOR_BLACK_SEMI = {0f, 0f, 0f, 0.7f};
    public static final float[] COLOR_BLACK_OPAQUE = {0f, 0f, 0f, 0.95f};
    public static final float[] COLOR_DANGER_BG = {0.2f, 0f, 0f, 0.95f};
    public static final float[] COLOR_WARNING_BG = {0.2f, 0.2f, 0f, 0.95f};
    
    // Gold Rewards
    public static final int GOLD_REWARD_PER_DANGER_POINT = 10;
    
    // Hard Mode
    public static final int HARD_MODE_DANGER_BONUS = 2;
    
    // Window Configuration
    public static final int WINDOWED_WIDTH = 1920;
    public static final int WINDOWED_HEIGHT = 1080;
    public static final int TARGET_FPS = 60;
    public static final String GAME_TITLE = "Space Stranding";
    
    // Cursor
    public static final float CURSOR_SCALE = 0.20f;
    public static final int CURSOR_OFFSET_X = 75;
    public static final int CURSOR_OFFSET_Y_COLLISION = 60;
    public static final int CURSOR_OFFSET_Y_TOP = 80;
}