package com.festeringportal.config;

import com.festeringportal.FesteringPortal;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FesteringConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Path.of("config", "festeringportal.json");

    public static int SPREAD_INTERVAL_TICKS = 20;
    public static int RADIUS_PER_CRYING_OBSIDIAN = 64;
    public static int MAX_DEPTH_BELOW_SURFACE = 4;
    public static boolean CORRUPT_MOBS = true;
    public static float MOB_CORRUPTION_CHANCE = 0.05f;
    public static boolean TRANSFORM_WATER_TO_LAVA = true;
    public static int MAX_PORTALS_PER_TICK = 10;

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                ConfigData data = GSON.fromJson(json, ConfigData.class);
                if (data != null) {
                    data.applyTo();
                }
            } catch (Exception e) {
                FesteringPortal.LOGGER.warn("Failed to read config, using defaults: {}", e.getMessage());
            }
        }
        save();
    }

    public static void save() {
        ConfigData data = new ConfigData();
        data.readFrom();
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(data));
        } catch (IOException e) {
            FesteringPortal.LOGGER.error("Failed to save config: {}", e.getMessage());
        }
    }

    private static class ConfigData {
        int spreadIntervalTicks = 20;
        int radiusPerCryingObsidian = 64;
        int maxDepthBelowSurface = 4;
        boolean corruptMobs = true;
        float mobCorruptionChance = 0.05f;
        boolean transformWaterToLava = true;
        int maxPortalsPerTick = 10;

        void applyTo() {
            SPREAD_INTERVAL_TICKS = spreadIntervalTicks;
            RADIUS_PER_CRYING_OBSIDIAN = radiusPerCryingObsidian;
            MAX_DEPTH_BELOW_SURFACE = maxDepthBelowSurface;
            CORRUPT_MOBS = corruptMobs;
            MOB_CORRUPTION_CHANCE = mobCorruptionChance;
            TRANSFORM_WATER_TO_LAVA = transformWaterToLava;
            MAX_PORTALS_PER_TICK = maxPortalsPerTick;
        }

        void readFrom() {
            spreadIntervalTicks = SPREAD_INTERVAL_TICKS;
            radiusPerCryingObsidian = RADIUS_PER_CRYING_OBSIDIAN;
            maxDepthBelowSurface = MAX_DEPTH_BELOW_SURFACE;
            corruptMobs = CORRUPT_MOBS;
            mobCorruptionChance = MOB_CORRUPTION_CHANCE;
            transformWaterToLava = TRANSFORM_WATER_TO_LAVA;
            maxPortalsPerTick = MAX_PORTALS_PER_TICK;
        }
    }
}
