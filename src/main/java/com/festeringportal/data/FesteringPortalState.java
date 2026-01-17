package com.festeringportal.data;

import com.festeringportal.FesteringPortal;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.*;

/**
 * Persistent state storage for festering portals.
 * Saves portal locations and corruption frontier across world restarts.
 */
public class FesteringPortalState extends PersistentState {

    private static final String STATE_ID = FesteringPortal.MOD_ID + "_portals";

    // Map of portal center position -> portal data
    private final Map<BlockPos, FesteringPortalData> festeringPortals = new HashMap<>();

    public FesteringPortalState() {
        super();
    }

    /**
     * Data class for a single festering portal.
     */
    public static class FesteringPortalData {
        public final BlockPos center;
        public final int cryingObsidianCount;
        public final int maxRadius;
        public final Set<BlockPos> corruptionFrontier;
        public long lastSpreadTick;

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

        /**
         * Check if a position is within the max spread radius.
         */
        public boolean isWithinMaxRadius(BlockPos pos) {
            double distance = Math.sqrt(center.getSquaredDistance(pos));
            return distance <= maxRadius;
        }

        /**
         * Serialize to NBT.
         */
        public NbtCompound toNbt() {
            NbtCompound nbt = new NbtCompound();
            nbt.putInt("centerX", center.getX());
            nbt.putInt("centerY", center.getY());
            nbt.putInt("centerZ", center.getZ());
            nbt.putInt("cryingCount", cryingObsidianCount);
            nbt.putLong("lastTick", lastSpreadTick);

            // Save frontier positions
            NbtList frontierList = new NbtList();
            for (BlockPos pos : corruptionFrontier) {
                NbtCompound posNbt = new NbtCompound();
                posNbt.putInt("x", pos.getX());
                posNbt.putInt("y", pos.getY());
                posNbt.putInt("z", pos.getZ());
                frontierList.add(posNbt);
            }
            nbt.put("frontier", frontierList);

            return nbt;
        }

        /**
         * Deserialize from NBT.
         */
        public static FesteringPortalData fromNbt(NbtCompound nbt) {
            BlockPos center = new BlockPos(
                    nbt.getInt("centerX"),
                    nbt.getInt("centerY"),
                    nbt.getInt("centerZ")
            );
            int cryingCount = nbt.getInt("cryingCount");
            long lastTick = nbt.getLong("lastTick");

            Set<BlockPos> frontier = new HashSet<>();
            NbtList frontierList = nbt.getList("frontier", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < frontierList.size(); i++) {
                NbtCompound posNbt = frontierList.getCompound(i);
                frontier.add(new BlockPos(
                        posNbt.getInt("x"),
                        posNbt.getInt("y"),
                        posNbt.getInt("z")
                ));
            }

            return new FesteringPortalData(center, cryingCount, frontier, lastTick);
        }
    }

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

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtList portalsList = new NbtList();
        for (FesteringPortalData portal : festeringPortals.values()) {
            portalsList.add(portal.toNbt());
        }
        nbt.put("portals", portalsList);
        return nbt;
    }

    /**
     * Create state from NBT.
     */
    public static FesteringPortalState createFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        FesteringPortalState state = new FesteringPortalState();
        NbtList portalsList = nbt.getList("portals", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < portalsList.size(); i++) {
            FesteringPortalData data = FesteringPortalData.fromNbt(portalsList.getCompound(i));
            state.festeringPortals.put(data.center, data);
        }
        if (!state.festeringPortals.isEmpty()) {
            FesteringPortal.LOGGER.info("Loaded {} festering portal(s)", state.festeringPortals.size());
        }
        return state;
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

        FesteringPortalState state = manager.getOrCreate(
                new Type<>(
                        FesteringPortalState::new,
                        FesteringPortalState::createFromNbt,
                        DataFixTypes.LEVEL
                ),
                STATE_ID
        );

        return state;
    }

    /**
     * Initialize the state system.
     */
    public static void initialize(MinecraftServer server) {
        getServerState(server);
    }
}
