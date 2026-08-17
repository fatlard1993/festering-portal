package com.festeringportal;

import com.festeringportal.config.FesteringConfig;
import com.festeringportal.corruption.CorruptionManager;
import com.festeringportal.corruption.SpreadingAlgorithm;
import com.festeringportal.data.FesteringPortalState;
import com.festeringportal.util.PortalScanner;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class FesteringPortal implements ModInitializer {

    public static final String MOD_ID = "festeringportal";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Guarded class load: PortalQuestRegistration names village-quests types.
        if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("village-quests-justfatlard")) {
            com.festeringportal.integration.PortalQuestRegistration.register();

            // Half-built frames wait here for their chunk. Never during chunk
            // loading: placing blocks in a chunk that is still arriving waits on
            // the thread doing the arriving.
            net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.END_SERVER_TICK.register(server -> {
                if (server.getTickCount() % 40 != 0) return;
                for (net.minecraft.server.level.ServerLevel level : server.getAllLevels()) {
                    com.festeringportal.quest.PortalSites.tick(level);
                }
            });
        }

        FesteringConfig.load();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            FesteringPortalState.initialize(server);
        });

        ServerTickEvents.END_LEVEL_TICK.register(world -> {
            if (world instanceof ServerLevel serverWorld) {
                CorruptionManager.tick(serverWorld);
            }
        });

        LOGGER.info("Festering Portal mod initialized!");
    }

    /**
     * Called by the mixin when a festering portal (with crying obsidian) is created.
     */
    public static void onFesteringPortalCreated(ServerLevel world, BlockPos portalPos, int cryingObsidianCount) {
        BlockPos center = PortalScanner.calculatePortalCenter(world, portalPos);
        FesteringPortalState state = FesteringPortalState.getServerState(world.getServer());

        if (isNearExistingPortal(state, center, 5)) {
            LOGGER.debug("Portal at {} is near an existing festering portal, skipping registration", center);
            return;
        }

        state.registerPortal(center, cryingObsidianCount);

        FesteringPortalState.FesteringPortalData portal = state.getPortal(center);
        if (portal != null) {
            Set<BlockPos> frontier = SpreadingAlgorithm.initializeFrontier(world, center, portal.maxRadius);
            state.updateFrontier(center, frontier, world.getGameTime());
        }
        LOGGER.info("Festering portal activated at {}! Max radius: {} blocks",
            center, cryingObsidianCount * FesteringConfig.RADIUS_PER_CRYING_OBSIDIAN);
    }

    /**
     * Called by the mixin when an entity arrives through a portal near a festering portal.
     */
    public static void onEntityPortalArrival(ServerLevel world, BlockPos entityPos) {
        FesteringPortalState state = FesteringPortalState.getServerState(world.getServer());

        for (FesteringPortalState.FesteringPortalData portal : state.getPortals()) {
            double distSq = portal.center.distSqr(entityPos);

            if (distSq <= 100) {
                long currentTick = world.getGameTime();
                if (currentTick - portal.lastBurstTick < 100) {
                    break;
                }
                portal.lastBurstTick = currentTick;

                int burstSize = portal.cryingObsidianCount * 5;
                SpreadingAlgorithm.burstSpread(world, portal, state, burstSize);

                LOGGER.debug("Entity triggered corruption burst at {} (burst size: {})",
                    portal.center, burstSize);
                break;
            }
        }
    }

    private static boolean isNearExistingPortal(FesteringPortalState state, BlockPos center, int radius) {
        double radiusSq = (double) radius * radius;
        for (FesteringPortalState.FesteringPortalData portal : state.getPortals()) {
            if (portal.center.distSqr(center) <= radiusSq) {
                return true;
            }
        }
        return false;
    }
}
