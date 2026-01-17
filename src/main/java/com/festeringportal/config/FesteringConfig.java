package com.festeringportal.config;

/**
 * Configuration values for the Festering Portal mod.
 * These can be modified to tune the corruption behavior.
 */
public class FesteringConfig {

    /**
     * Number of ticks between spread attempts.
     * 20 ticks = 1 second (20 ticks per second)
     * Lower = faster spread, higher = slower spread
     */
    public static int SPREAD_INTERVAL_TICKS = 20;

    /**
     * Blocks of max spread radius per crying obsidian in the portal frame.
     * With 1 crying obsidian = 64 block max radius
     * With full frame (14 crying obsidian) = 896 block max radius
     */
    public static int RADIUS_PER_CRYING_OBSIDIAN = 64;

    /**
     * Whether to spread corruption vertically (up and down).
     * If false, corruption only spreads horizontally.
     */
    public static boolean SPREAD_VERTICALLY = true;

    /**
     * Maximum depth below surface to corrupt.
     * Blocks deeper than this below the surface will not be corrupted.
     */
    public static int MAX_DEPTH_BELOW_SURFACE = 4;

    /**
     * Whether to corrupt mobs in the corrupted area.
     */
    public static boolean CORRUPT_MOBS = true;

    /**
     * Chance (0-1) for a mob to be corrupted each tick cycle when in the zone.
     */
    public static float MOB_CORRUPTION_CHANCE = 0.05f;

    /**
     * Whether to transform water blocks into lava.
     * WARNING: This can cause fires and be dangerous!
     */
    public static boolean TRANSFORM_WATER_TO_LAVA = true;

    /**
     * Maximum number of portals to process per tick cycle.
     * Helps with performance on servers with many portals.
     */
    public static int MAX_PORTALS_PER_TICK = 10;

    /**
     * Load configuration from file.
     * Currently uses hardcoded values, but this method can be extended
     * to load from a config file (e.g., with Cloth Config API).
     */
    public static void load() {
        // TODO: Implement config file loading
        // For now, using default values
    }

    /**
     * Save configuration to file.
     */
    public static void save() {
        // TODO: Implement config file saving
    }
}
