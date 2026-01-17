package com.festeringportal.data;

import com.festeringportal.FesteringPortal;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Persistent state storage for festering portals.
 * Saves portal locations and corruption frontier across world restarts.
 */
public class FesteringPortalState extends PersistentState {

    private static final String STATE_ID = FesteringPortal.MOD_ID + "_portals";

    // Map of portal center position -> portal data
    private Map<BlockPos, FesteringPortalData> festeringPortals;

    public FesteringPortalState() {
        super();
        this.festeringPortals = new HashMap<>();
    }

    public FesteringPortalState(Map<BlockPos, FesteringPortalData> portals) {
        super();
        this.festeringPortals = new HashMap<>(portals);
    }

    /**
     * Data class for a single festering portal.
     */
    public static class FesteringPortalData {
        public final BlockPos center;
        public final int cryingObsidianCount;
        public final int maxRadius;
        public Set<BlockPos> corruptionFrontier;
        public long lastSpreadTick;

        // Codec for FesteringPortalData
        public static final Codec<FesteringPortalData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                BlockPos.CODEC.fieldOf("center").forGetter(d -> d.center),
                Codec.INT.fieldOf("cryingCount").forGetter(d -> d.cryingObsidianCount),
                Codec.LONG.fieldOf("lastTick").forGetter(d -> d.lastSpreadTick),
                BlockPos.CODEC.listOf().fieldOf("frontier").forGetter(d -> new ArrayList<>(d.corruptionFrontier))
            ).apply(instance, FesteringPortalData::fromCodec)
        );

        public FesteringPortalData(BlockPos center, int cryingObsidianCount) {
            this.center = center;
            this.cryingObsidianCount = cryingObsidianCount;
            this.maxRadius = cryingObsidianCount * 64; // 64 blocks per crying obsidian
            this.corruptionFrontier = new HashSet<>();
            this.lastSpreadTick = 0;

            // Initialize frontier with the portal center
            this.corruptionFrontier.add(center);
        }

        private FesteringPortalData(BlockPos center, int cryingObsidianCount, Set<BlockPos> frontier, long lastTick) {
            this.center = center;
            this.cryingObsidianCount = cryingObsidianCount;
            this.maxRadius = cryingObsidianCount * 64;
            this.corruptionFrontier = frontier;
            this.lastSpreadTick = lastTick;
        }

        // Factory method for Codec
        private static FesteringPortalData fromCodec(BlockPos center, int cryingCount, long lastTick, List<BlockPos> frontier) {
            return new FesteringPortalData(center, cryingCount, new HashSet<>(frontier), lastTick);
        }

        /**
         * Check if a position is within the max spread radius.
         */
        public boolean isWithinMaxRadius(BlockPos pos) {
            double distance = Math.sqrt(center.getSquaredDistance(pos));
            return distance <= maxRadius;
        }
    }

    // Codec for the entire state
    public static final Codec<FesteringPortalState> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.unboundedMap(
                Codec.STRING.xmap(
                    str -> {
                        String[] parts = str.split(",");
                        return new BlockPos(
                            Integer.parseInt(parts[0]),
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2])
                        );
                    },
                    pos -> pos.getX() + "," + pos.getY() + "," + pos.getZ()
                ),
                FesteringPortalData.CODEC
            ).fieldOf("portals").forGetter(state -> state.festeringPortals)
        ).apply(instance, FesteringPortalState::new)
    );

    // PersistentStateType for 1.21.11+
    private static final PersistentStateType<FesteringPortalState> TYPE = new PersistentStateType<>(
        STATE_ID,
        FesteringPortalState::new,
        CODEC,
        null // No DataFixTypes needed for mod data
    );

    /**
     * Register a new festering portal.
     */
    public void registerPortal(BlockPos center, int cryingObsidianCount) {
        festeringPortals.put(center, new FesteringPortalData(center, cryingObsidianCount));
        markDirty();
    }

    /**
     * Remove a festering portal.
     */
    public void removePortal(BlockPos center) {
        if (festeringPortals.remove(center) != null) {
            markDirty();
        }
    }

    /**
     * Get all festering portals.
     */
    public Collection<FesteringPortalData> getPortals() {
        return Collections.unmodifiableCollection(festeringPortals.values());
    }

    /**
     * Get a specific portal by center position.
     */
    public FesteringPortalData getPortal(BlockPos center) {
        return festeringPortals.get(center);
    }

    /**
     * Check if a portal exists at the given center.
     */
    public boolean hasPortal(BlockPos center) {
        return festeringPortals.containsKey(center);
    }

    /**
     * Update the frontier for a portal.
     */
    public void updateFrontier(BlockPos center, Set<BlockPos> newFrontier, long tick) {
        FesteringPortalData data = festeringPortals.get(center);
        if (data != null) {
            data.corruptionFrontier.clear();
            data.corruptionFrontier.addAll(newFrontier);
            data.lastSpreadTick = tick;
            markDirty();
        }
    }

    /**
     * Get or create the state for the server's overworld.
     */
    public static FesteringPortalState getServerState(MinecraftServer server) {
        ServerWorld world = server.getWorld(World.OVERWORLD);
        if (world == null) {
            throw new IllegalStateException("Overworld not found!");
        }
        PersistentStateManager manager = world.getPersistentStateManager();
        FesteringPortalState state = manager.getOrCreate(TYPE);

        if (!state.festeringPortals.isEmpty()) {
            FesteringPortal.LOGGER.debug("Loaded {} festering portal(s)", state.festeringPortals.size());
        }

        return state;
    }

    /**
     * Initialize the state system.
     */
    public static void initialize(MinecraftServer server) {
        getServerState(server);
    }
}
