package com.festeringportal;

import com.festeringportal.config.FesteringConfig;
import com.festeringportal.corruption.CorruptionManager;
import com.festeringportal.corruption.SpreadingAlgorithm;
import com.festeringportal.data.FesteringPortalState;
import com.festeringportal.util.PortalScanner;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Festering Portal - A Fabric mod that makes nether portals with crying obsidian
 * spread nether corruption to the surrounding overworld.
 *
 * Features:
 * - Portals can be built with crying obsidian in the frame
 * - Each crying obsidian adds 64 blocks to the max corruption radius
 * - Corruption spreads organically, one block at a time (like grass spreading)
 * - Natural blocks transform into their nether equivalents
 * - Water transforms into lava for dramatic effect
 */
public class FesteringPortal implements ModInitializer {

    public static final String MOD_ID = "festeringportal";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Load configuration
        FesteringConfig.load();

        // Register server lifecycle events
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            FesteringPortalState.initialize(server);
        });

        // Register world tick event for corruption spreading
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world instanceof ServerWorld serverWorld) {
                CorruptionManager.tick(serverWorld);
            }
        });

        LOGGER.info("Festering Portal mod initialized!");
    }

    /**
     * Called by the mixin when a festering portal (with crying obsidian) is created.
     *
     * @param world The server world
     * @param portalPos A position inside or near the portal
     * @param cryingObsidianCount Number of crying obsidian blocks in the frame
     */
    public static void onFesteringPortalCreated(ServerWorld world, BlockPos portalPos, int cryingObsidianCount) {

        // Calculate the portal center
        BlockPos center = PortalScanner.calculatePortalCenter(world, portalPos);

        // Get or create the state
        FesteringPortalState state = FesteringPortalState.getServerState(world.getServer());

        // Check if this portal is already registered (nearby existing portal)
        if (isNearExistingPortal(state, center, 5)) {
            LOGGER.debug("Portal at {} is near an existing festering portal, skipping registration", center);
            return;
        }

        // Register the new festering portal
        state.registerPortal(center, cryingObsidianCount);

        int maxRadius = cryingObsidianCount * FesteringConfig.RADIUS_PER_CRYING_OBSIDIAN;

        // Initialize the corruption frontier
        FesteringPortalState.FesteringPortalData portal = state.getPortal(center);
        if (portal != null) {
            Set<BlockPos> frontier = SpreadingAlgorithm.initializeFrontier(world, center, maxRadius);
            state.updateFrontier(center, frontier, world.getTime());
        }
        LOGGER.info("Festering portal activated! Max radius: {} blocks", maxRadius);
    }

    /**
     * Check if a position is near an existing festering portal.
     */
    private static boolean isNearExistingPortal(FesteringPortalState state, BlockPos center, int radius) {
        for (FesteringPortalState.FesteringPortalData portal : state.getPortals()) {
            double distance = Math.sqrt(portal.center.getSquaredDistance(center));
            if (distance <= radius) {
                return true;
            }
        }
        return false;
    }
}
