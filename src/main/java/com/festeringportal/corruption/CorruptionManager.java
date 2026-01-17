package com.festeringportal.corruption;

import com.festeringportal.FesteringPortal;
import com.festeringportal.config.FesteringConfig;
import com.festeringportal.data.FesteringPortalState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Manages the tick-based corruption spreading from festering portals.
 * Runs on the server world tick and processes registered portals.
 */
public class CorruptionManager {

    private static long tickCounter = 0;

    /**
     * Called every world tick to process corruption spreading.
     *
     * @param world The server world
     */
    public static void tick(ServerWorld world) {
        // Only process in the Overworld
        if (world.getRegistryKey() != World.OVERWORLD) {
            return;
        }

        tickCounter++;

        // Only process every N ticks (default: 200 = 10 seconds)
        if (tickCounter % FesteringConfig.SPREAD_INTERVAL_TICKS != 0) {
            return;
        }

        FesteringPortalState state = FesteringPortalState.getServerState(world.getServer());
        Collection<FesteringPortalState.FesteringPortalData> portals = state.getPortals();

        if (portals.isEmpty()) {
            return;
        }

        // Process portals
        List<BlockPos> portalsToRemove = new ArrayList<>();
        long currentTick = world.getTime();

        for (FesteringPortalState.FesteringPortalData portal : portals) {
            // Check if portal is still valid (has portal blocks nearby)
            if (!isPortalStillValid(world, portal.center)) {
                portalsToRemove.add(portal.center);
                FesteringPortal.LOGGER.debug("Portal at {} no longer valid", portal.center);
                continue;
            }

            // Check if chunk is loaded
            if (!world.isChunkLoaded(portal.center)) {
                FesteringPortal.LOGGER.debug("Portal at {} chunk not loaded", portal.center);
                continue;
            }

            // Attempt to spread corruption
            SpreadingAlgorithm.spreadFromPortal(world, portal, state, currentTick);

            // Attempt to corrupt mobs in the area
            SpreadingAlgorithm.corruptMobs(world, portal, world.getRandom());
        }

        // Remove invalid portals
        for (BlockPos center : portalsToRemove) {
            state.removePortal(center);
            FesteringPortal.LOGGER.debug("Removed invalid festering portal at {}", center);
        }
    }

    /**
     * Check if a portal is still valid (has portal blocks).
     */
    private static boolean isPortalStillValid(ServerWorld world, BlockPos center) {
        // Check a 5x5x5 area around the center for portal blocks
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -2; dz <= 2; dz++) {
                    BlockPos checkPos = center.add(dx, dy, dz);
                    if (world.getBlockState(checkPos).isOf(Blocks.NETHER_PORTAL)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Reset the tick counter (used for testing).
     */
    public static void resetTickCounter() {
        tickCounter = 0;
    }
}
