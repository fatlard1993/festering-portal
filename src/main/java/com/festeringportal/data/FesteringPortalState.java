package com.festeringportal.data;

import com.festeringportal.FesteringPortal;
import com.festeringportal.config.FesteringConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.SavedDataStorage;
import net.minecraft.resources.Identifier;
import java.util.*;

/**
 * Persistent state storage for festering portals.
 * Saves portal locations and corruption frontier across world restarts.
 */
public class FesteringPortalState extends SavedData {

    private static final String STATE_ID = FesteringPortal.MOD_ID + "_portals";
    public static final int MAX_FRONTIER_SIZE = 5000;

    private Map<BlockPos, FesteringPortalData> festeringPortals;

    public FesteringPortalState() {
        super();
        this.festeringPortals = new HashMap<>();
    }

    public FesteringPortalState(Map<BlockPos, FesteringPortalData> portals) {
        super();
        this.festeringPortals = new HashMap<>(portals);
    }

    public static class FesteringPortalData {
        public final BlockPos center;
        public final int cryingObsidianCount;
        public final int maxRadius;
        public Set<BlockPos> corruptionFrontier;
        public long lastSpreadTick;
        public long lastBurstTick;

        public static final Codec<FesteringPortalData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                BlockPos.CODEC.fieldOf("center").forGetter(d -> d.center),
                Codec.INT.fieldOf("cryingCount").forGetter(d -> d.cryingObsidianCount),
                Codec.LONG.fieldOf("lastTick").forGetter(d -> d.lastSpreadTick),
                Codec.LONG.optionalFieldOf("lastBurstTick", 0L).forGetter(d -> d.lastBurstTick),
                BlockPos.CODEC.listOf().fieldOf("frontier").forGetter(d -> new ArrayList<>(d.corruptionFrontier))
            ).apply(instance, FesteringPortalData::fromCodec)
        );

        public FesteringPortalData(BlockPos center, int cryingObsidianCount) {
            this.center = center;
            this.cryingObsidianCount = cryingObsidianCount;
            this.maxRadius = cryingObsidianCount * FesteringConfig.RADIUS_PER_CRYING_OBSIDIAN;
            this.corruptionFrontier = new HashSet<>();
            this.lastSpreadTick = 0;
            this.lastBurstTick = 0;
            this.corruptionFrontier.add(center);
        }

        private FesteringPortalData(BlockPos center, int cryingObsidianCount, Set<BlockPos> frontier, long lastTick, long lastBurstTick) {
            this.center = center;
            this.cryingObsidianCount = cryingObsidianCount;
            this.maxRadius = cryingObsidianCount * FesteringConfig.RADIUS_PER_CRYING_OBSIDIAN;
            this.corruptionFrontier = frontier;
            this.lastSpreadTick = lastTick;
            this.lastBurstTick = lastBurstTick;
        }

        private static FesteringPortalData fromCodec(BlockPos center, int cryingCount, long lastTick, long lastBurstTick, List<BlockPos> frontier) {
            return new FesteringPortalData(center, cryingCount, new HashSet<>(frontier), lastTick, lastBurstTick);
        }

        public boolean isWithinMaxRadius(BlockPos pos) {
            double distSq = center.distSqr(pos);
            return distSq <= (double) maxRadius * maxRadius;
        }
    }

    private static final Codec<BlockPos> BLOCK_POS_STRING_CODEC = Codec.STRING.comapFlatMap(
        str -> {
            try {
                String[] parts = str.split(",");
                if (parts.length != 3) {
                    return DataResult.error(() -> "Invalid BlockPos format (expected 3 parts): " + str);
                }
                return DataResult.success(new BlockPos(
                    Integer.parseInt(parts[0].trim()),
                    Integer.parseInt(parts[1].trim()),
                    Integer.parseInt(parts[2].trim())
                ));
            } catch (NumberFormatException e) {
                return DataResult.error(() -> "Invalid BlockPos number format: " + str);
            }
        },
        pos -> pos.getX() + "," + pos.getY() + "," + pos.getZ()
    );

    public static final Codec<FesteringPortalState> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.unboundedMap(BLOCK_POS_STRING_CODEC, FesteringPortalData.CODEC)
                .fieldOf("portals").forGetter(state -> state.festeringPortals)
        ).apply(instance, FesteringPortalState::new)
    );

    private static final SavedDataType<FesteringPortalState> TYPE = new SavedDataType<>(
        Identifier.fromNamespaceAndPath(FesteringPortal.MOD_ID, "portals"),
        FesteringPortalState::new,
        CODEC,
        null
    );

    public void registerPortal(BlockPos center, int cryingObsidianCount) {
        festeringPortals.put(center, new FesteringPortalData(center, cryingObsidianCount));
        setDirty();
    }

    public void removePortal(BlockPos center) {
        if (festeringPortals.remove(center) != null) {
            setDirty();
        }
    }

    public Collection<FesteringPortalData> getPortals() {
        return Collections.unmodifiableCollection(festeringPortals.values());
    }

    public FesteringPortalData getPortal(BlockPos center) {
        return festeringPortals.get(center);
    }

    public boolean hasPortal(BlockPos center) {
        return festeringPortals.containsKey(center);
    }

    /**
     * Update the frontier for a portal. Enforces MAX_FRONTIER_SIZE cap.
     */
    public void updateFrontier(BlockPos center, Set<BlockPos> newFrontier, long tick) {
        FesteringPortalData data = festeringPortals.get(center);
        if (data != null) {
            if (data.corruptionFrontier != newFrontier) {
                data.corruptionFrontier.clear();
                data.corruptionFrontier.addAll(newFrontier);
            }
            // Enforce frontier size cap by evicting arbitrary entries
            if (data.corruptionFrontier.size() > MAX_FRONTIER_SIZE) {
                Iterator<BlockPos> it = data.corruptionFrontier.iterator();
                int toRemove = data.corruptionFrontier.size() - MAX_FRONTIER_SIZE;
                for (int i = 0; i < toRemove && it.hasNext(); i++) {
                    it.next();
                    it.remove();
                }
            }
            data.lastSpreadTick = tick;
            setDirty();
        }
    }

    public static FesteringPortalState getServerState(MinecraftServer server) {
        ServerLevel world = server.getLevel(Level.OVERWORLD);
        if (world == null) {
            throw new IllegalStateException("Overworld not found!");
        }
        SavedDataStorage manager = world.getDataStorage();
        return manager.computeIfAbsent(TYPE);
    }

    public static void initialize(MinecraftServer server) {
        FesteringPortalState state = getServerState(server);
        if (!state.festeringPortals.isEmpty()) {
            FesteringPortal.LOGGER.info("Loaded {} festering portal(s)", state.festeringPortals.size());
        }
    }
}
