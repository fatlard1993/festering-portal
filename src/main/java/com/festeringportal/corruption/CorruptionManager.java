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
    private static int portalRotationIndex = 0;

    /**
     * Called every world tick to process corruption spreading.
     */
    public static void tick(ServerWorld world) {
        if (world.getRegistryKey() != World.OVERWORLD) {
            return;
        }

        tickCounter++;

        if (tickCounter % FesteringConfig.SPREAD_INTERVAL_TICKS != 0) {
            return;
        }

        FesteringPortalState state = FesteringPortalState.getServerState(world.getServer());
        List<FesteringPortalState.FesteringPortalData> portals = new ArrayList<>(state.getPortals());

        if (portals.isEmpty()) {
            return;
        }

        List<BlockPos> portalsToRemove = new ArrayList<>();
        long currentTick = world.getTime();

        // Enforce MAX_PORTALS_PER_TICK with rotation for fairness
        int maxToProcess = Math.min(portals.size(), FesteringConfig.MAX_PORTALS_PER_TICK);
        if (portalRotationIndex >= portals.size()) {
            portalRotationIndex = 0;
        }

        int processed = 0;
        for (int i = 0; i < portals.size() && processed < maxToProcess; i++) {
            int idx = (portalRotationIndex + i) % portals.size();
            FesteringPortalState.FesteringPortalData portal = portals.get(idx);

            // Check if portal is still valid
            if (!isPortalStillValid(world, portal.center)) {
                portalsToRemove.add(portal.center);
                FesteringPortal.LOGGER.debug("Portal at {} no longer valid", portal.center);
                continue;
            }

            if (!world.isChunkLoaded(portal.center)) {
                continue;
            }

            SpreadingAlgorithm.spreadFromPortal(world, portal, state, currentTick);
            SpreadingAlgorithm.corruptMobs(world, portal, world.getRandom());
            processed++;
        }

        portalRotationIndex = (portalRotationIndex + maxToProcess) % Math.max(1, portals.size());

        for (BlockPos center : portalsToRemove) {
            state.removePortal(center);
            FesteringPortal.LOGGER.debug("Removed invalid festering portal at {}", center);
        }
    }

    /**
     * Check if a portal is still valid (has portal blocks).
     */
    private static boolean isPortalStillValid(ServerWorld world, BlockPos center) {
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
}
