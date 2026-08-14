package com.festeringportal.corruption;

import com.festeringportal.FesteringPortal;
import com.festeringportal.config.FesteringConfig;
import com.festeringportal.data.FesteringPortalState;
import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;

/**
 * Grass-like organic spreading algorithm for nether corruption.
 * Spreads one block at a time from the corruption frontier, creating
 * natural, irregular edges similar to how grass spreads.
 */
public class SpreadingAlgorithm {

    private static final Direction[] DIRECTIONS = Direction.values();

    private static final int SPREADS_PER_TICK = 3;

    /**
     * Attempt to spread corruption from one portal.
     * Tries multiple spread attempts per call for faster corruption.
     *
     * @param world The server world
     * @param portal The festering portal data
     * @param state The persistent state (for saving changes)
     * @param currentTick The current world tick
     * @return true if corruption spread occurred
     */
    public static boolean spreadFromPortal(
            ServerLevel world,
            FesteringPortalState.FesteringPortalData portal,
            FesteringPortalState state,
            long currentTick) {

        Set<BlockPos> frontier = portal.corruptionFrontier;

        if (frontier.isEmpty()) {
            Set<BlockPos> newFrontier = initializeFrontier(world, portal.center, portal.maxRadius);
            frontier.addAll(newFrontier);
        }

        cleanupFrontier(world, portal, frontier);

        if (frontier.isEmpty()) {
            return false;
        }

        boolean anySpread = false;
        RandomSource random = world.getRandom();

        // Snapshot frontier once for random access
        List<BlockPos> frontierList = new ArrayList<>(frontier);

        for (int attempt = 0; attempt < SPREADS_PER_TICK; attempt++) {
            if (frontierList.isEmpty()) break;

            BlockPos spreadSource = frontierList.get(random.nextInt(frontierList.size()));

            // Spread to a random adjacent block: start from a random direction offset
            // rather than allocating and shuffling a list each iteration
            int startDir = random.nextInt(DIRECTIONS.length);

            for (int d = 0; d < DIRECTIONS.length; d++) {
                Direction direction = DIRECTIONS[(startDir + d) % DIRECTIONS.length];
                BlockPos targetPos = spreadSource.relative(direction);

                if (!portal.isWithinMaxRadius(targetPos)) {
                    continue;
                }

                if (!world.hasChunkAt(targetPos)) {
                    continue;
                }

                BlockState targetState = world.getBlockState(targetPos);

                if (BlockTransformations.isImmune(targetState)) {
                    continue;
                }

                if (!isWithinDepthLimit(world, targetPos)) {
                    continue;
                }

                if (targetState.is(Blocks.WATER)) {
                    if (transformWaterToLava(world, targetPos)) {
                        frontier.add(targetPos);
                        spawnCorruptionParticles(world, targetPos);
                        anySpread = true;
                        break;
                    }
                    continue;
                }

                // Skip upper halves of double-tall blocks; the bottom drives both
                if (targetState.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)
                        && targetState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
                    continue;
                }

                BlockState transformedState = BlockTransformations.getTransformation(targetState, world.getRandom());
                if (transformedState != null && !targetState.equals(transformedState)) {
                    transformBlock(world, targetPos, targetState, transformedState);

                    frontier.add(targetPos);

                    spawnCorruptionParticles(world, targetPos);

                    anySpread = true;
                    break;
                }
            }

            if (shouldRemoveFromFrontier(world, portal, spreadSource)) {
                frontier.remove(spreadSource);
            }
        }

        matureNetherBlocks(world, portal, random);

        if (anySpread) {
            state.updateFrontier(portal.center, frontier, currentTick);
        }

        return anySpread;
    }

    /**
     * Force an immediate burst of corruption spread (triggered by entity portal exit).
     * Spreads multiple blocks at once for dramatic effect.
     */
    public static void burstSpread(
            ServerLevel world,
            FesteringPortalState.FesteringPortalData portal,
            FesteringPortalState state,
            int burstSize) {

        FesteringPortal.LOGGER.debug("Entity triggered corruption burst! Spreading {} blocks", burstSize);

        Set<BlockPos> frontier = portal.corruptionFrontier;

        if (frontier.isEmpty()) {
            frontier.addAll(initializeFrontier(world, portal.center, portal.maxRadius));
        }

        int spread = 0;
        int attempts = 0;
        int maxAttempts = burstSize * 10;
        RandomSource random = world.getRandom();
        List<BlockPos> frontierList = new ArrayList<>(frontier);
        Set<BlockPos> toRemove = new HashSet<>();

        while (spread < burstSize && attempts < maxAttempts && !frontierList.isEmpty()) {
            attempts++;

            // Pick random source; swap-remove if flagged dead to avoid O(n) scans
            int idx = random.nextInt(frontierList.size());
            BlockPos spreadSource = frontierList.get(idx);

            for (Direction direction : DIRECTIONS) {
                BlockPos targetPos = spreadSource.relative(direction);

                if (!portal.isWithinMaxRadius(targetPos)) continue;
                if (!world.hasChunkAt(targetPos)) continue;
                if (!isWithinDepthLimit(world, targetPos)) continue;

                BlockState targetState = world.getBlockState(targetPos);
                if (BlockTransformations.isImmune(targetState)) continue;

                // Water-to-lava handling (same as spreadFromPortal)
                if (targetState.is(Blocks.WATER)) {
                    if (transformWaterToLava(world, targetPos)) {
                        frontier.add(targetPos);
                        frontierList.add(targetPos);
                        spawnCorruptionParticles(world, targetPos);
                        spread++;
                        break;
                    }
                    continue;
                }

                // Skip upper halves of double-tall blocks; the bottom drives both
                if (targetState.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)
                        && targetState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
                    continue;
                }

                BlockState transformedState = BlockTransformations.getTransformation(targetState, random);
                if (transformedState != null && !targetState.equals(transformedState)) {
                    transformBlock(world, targetPos, targetState, transformedState);
                    frontier.add(targetPos);
                    frontierList.add(targetPos);
                    spawnCorruptionParticles(world, targetPos);
                    spread++;
                    break;
                }
            }

            if (shouldRemoveFromFrontier(world, portal, spreadSource)) {
                toRemove.add(spreadSource);
                // Swap-remove from list: O(1) instead of O(n)
                int last = frontierList.size() - 1;
                frontierList.set(idx, frontierList.get(last));
                frontierList.remove(last);
            }
        }

        frontier.removeAll(toRemove);

        state.updateFrontier(portal.center, frontier, world.getGameTime());
        FesteringPortal.LOGGER.debug("Burst spread {} blocks", spread);
    }

    /**
     * Transform a block, handling double-tall blocks (doors, tall plants) properly.
     * Bottom halves drive the transform for both halves; upper halves are skipped at the call site.
     */
    private static void transformBlock(ServerLevel world, BlockPos targetPos, BlockState targetState, BlockState transformedState) {
        if (targetState.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
            DoubleBlockHalf half = targetState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);
            if (half == DoubleBlockHalf.UPPER) {
                return;
            }

            world.setBlock(targetPos, transformedState, Block.UPDATE_ALL);

            BlockPos topPos = targetPos.above();
            BlockState topState = world.getBlockState(topPos);

            if (topState.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)
                    && topState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
                if (transformedState.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
                    // Door→door: set top half of new door
                    world.setBlock(topPos,
                            transformedState.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER),
                            Block.UPDATE_ALL);
                } else {
                    // Tall plant→single block: clear the orphaned top
                    world.setBlock(topPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                }
            }
            return;
        }

        world.setBlock(targetPos, transformedState, Block.UPDATE_ALL);
    }

    /**
     * Attempt to mature/evolve nether blocks within the corruption zone.
     * This creates diversity - netherrack becomes nylium, basalt becomes polished, etc.
     */
    private static void matureNetherBlocks(
            ServerLevel world,
            FesteringPortalState.FesteringPortalData portal,
            RandomSource random) {

        int maturationAttempts = 2;
        int searchRadius = Math.min(20, portal.corruptionFrontier.size() > 0 ? 15 : 5);

        for (int i = 0; i < maturationAttempts; i++) {
            int dx = random.nextInt(searchRadius * 2 + 1) - searchRadius;
            int dy = random.nextInt(searchRadius * 2 + 1) - searchRadius;
            int dz = random.nextInt(searchRadius * 2 + 1) - searchRadius;

            BlockPos pos = portal.center.offset(dx, dy, dz);

            if (!world.hasChunkAt(pos)) continue;
            if (!portal.isWithinMaxRadius(pos)) continue;

            BlockState currentState = world.getBlockState(pos);

            if (!BlockTransformations.isNetherBlock(currentState.getBlock())) continue;

            BlockTransformations.NeighborContext context =
                new BlockTransformations.NeighborContext(random);
            context.analyze(world, pos);

            BlockState maturedState = BlockTransformations.getMatureTransformation(currentState, context);

            if (maturedState != null && !currentState.equals(maturedState)) {
                world.setBlock(pos, maturedState, Block.UPDATE_ALL);

                spawnMaturationParticles(world, pos);

                // If nylium formed, maybe spawn roots/fungus above
                if ((maturedState.is(Blocks.CRIMSON_NYLIUM) || maturedState.is(Blocks.WARPED_NYLIUM))
                        && context.hasAirAbove() && random.nextFloat() < 0.4f) {
                    BlockPos above = pos.above();
                    if (world.getBlockState(above).isAir()) {
                        BlockState vegetation = maturedState.is(Blocks.CRIMSON_NYLIUM)
                            ? (random.nextFloat() < 0.7f ? Blocks.CRIMSON_ROOTS.defaultBlockState() : Blocks.CRIMSON_FUNGUS.defaultBlockState())
                            : (random.nextFloat() < 0.7f ? Blocks.WARPED_ROOTS.defaultBlockState() : Blocks.WARPED_FUNGUS.defaultBlockState());
                        world.setBlock(above, vegetation, Block.UPDATE_ALL);
                    }
                }
            }
        }
    }

    /**
     * Spawn subtle particles for block maturation.
     */
    private static void spawnMaturationParticles(ServerLevel world, BlockPos pos) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        world.sendParticles(
                ParticleTypes.CRIMSON_SPORE,
                x, y, z,
                2,
                0.3, 0.3, 0.3,
                0.01
        );
    }

    /** Max frontier entries to check per cleanup pass. */
    private static final int CLEANUP_BATCH_SIZE = 50;

    /**
     * Clean up the frontier by removing blocks that are no longer valid spread sources.
     * Amortized: processes up to CLEANUP_BATCH_SIZE entries per call to avoid
     * scanning the entire frontier every tick.
     */
    private static void cleanupFrontier(
            ServerLevel world,
            FesteringPortalState.FesteringPortalData portal,
            Set<BlockPos> frontier) {

        int checked = 0;
        Iterator<BlockPos> iterator = frontier.iterator();
        while (iterator.hasNext() && checked < CLEANUP_BATCH_SIZE) {
            BlockPos pos = iterator.next();
            checked++;

            if (!portal.isWithinMaxRadius(pos)) {
                iterator.remove();
                continue;
            }

            if (shouldRemoveFromFrontier(world, portal, pos)) {
                iterator.remove();
            }
        }
    }

    /**
     * Check if a position should be removed from the frontier.
     * Returns true if all adjacent blocks are either:
     * - Already corrupted (nether blocks)
     * - Immune to corruption
     * - Outside the max radius
     */
    private static boolean shouldRemoveFromFrontier(
            ServerLevel world,
            FesteringPortalState.FesteringPortalData portal,
            BlockPos pos) {

        for (Direction direction : DIRECTIONS) {
            BlockPos neighbor = pos.relative(direction);

            if (!portal.isWithinMaxRadius(neighbor)) {
                continue;
            }

            // If chunk not loaded, assume it might be spreadable
            if (!world.hasChunkAt(neighbor)) {
                return false;
            }

            BlockState neighborState = world.getBlockState(neighbor);

            if (!BlockTransformations.isImmune(neighborState) &&
                    BlockTransformations.canTransform(neighborState)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Transform water into a contained lava pool.
     * Creates a minimal lava pocket - just floor, 4 cardinal walls, and lava center.
     * Only works on surface water (water with air above).
     */
    private static boolean transformWaterToLava(ServerLevel world, BlockPos waterPos) {
        if (!FesteringConfig.TRANSFORM_WATER_TO_LAVA) return false;

        RandomSource random = world.getRandom();

        BlockState above = world.getBlockState(waterPos.above());
        if (above.is(Blocks.WATER)) {
            return false;
        }

        BlockPos floorPos = waterPos.below();
        BlockState floorState = world.getBlockState(floorPos);
        if (floorState.is(Blocks.WATER) || !floorState.isRedstoneConductor(world, floorPos)) {
            world.setBlock(floorPos, getRandomWallBlock(random).defaultBlockState(), Block.UPDATE_ALL);
        }

        for (Direction dir : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
            BlockPos wallPos = waterPos.relative(dir);
            BlockState wallState = world.getBlockState(wallPos);
            if (wallState.is(Blocks.WATER) || wallState.isAir()) {
                world.setBlock(wallPos, getRandomWallBlock(random).defaultBlockState(), Block.UPDATE_ALL);
            }
        }

        world.setBlock(waterPos, Blocks.LAVA.defaultBlockState(), Block.UPDATE_ALL);
        spawnCorruptionParticles(world, waterPos);

        // Steam effect
        world.sendParticles(
                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                waterPos.getX() + 0.5, waterPos.getY() + 1, waterPos.getZ() + 0.5,
                5, 0.3, 0.3, 0.3, 0.02
        );

        return true;
    }

    /**
     * Get a random wall block for lava pool containment.
     */
    private static Block getRandomWallBlock(RandomSource random) {
        float roll = random.nextFloat();
        if (roll < 0.4f) {
            return Blocks.BLACKSTONE;
        } else if (roll < 0.7f) {
            return Blocks.BASALT;
        } else {
            return Blocks.OBSIDIAN;
        }
    }

    /**
     * Spawn particles at the corruption site for visual feedback.
     */
    private static void spawnCorruptionParticles(ServerLevel world, BlockPos pos) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        world.sendParticles(
                ParticleTypes.SOUL_FIRE_FLAME,
                x, y, z,
                3, // count
                0.3, 0.3, 0.3, // spread
                0.02 // speed
        );

        world.sendParticles(
                ParticleTypes.SMOKE,
                x, y, z,
                2,
                0.2, 0.2, 0.2,
                0.01
        );
    }

    /**
     * Initialize the frontier by finding the actual corruption edge.
     * Uses BFS to search outward from portal center and find corrupted blocks with uncorrupted neighbors.
     */
    public static Set<BlockPos> initializeFrontier(ServerLevel world, BlockPos portalCenter, int maxRadius) {
        Set<BlockPos> frontier = new HashSet<>();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> toExplore = new LinkedList<>();

        toExplore.add(portalCenter);
        visited.add(portalCenter);

        int blocksExplored = 0;
        int maxBlocksToExplore = 50000;

        while (!toExplore.isEmpty() && blocksExplored < maxBlocksToExplore) {
            BlockPos current = toExplore.poll();
            blocksExplored++;

            double distSq = current.distSqr(portalCenter);
            if (distSq > (double) maxRadius * maxRadius) continue;

            if (!world.hasChunkAt(current)) continue;

            BlockState state = world.getBlockState(current);

            if (BlockTransformations.isNetherBlock(state.getBlock()) || state.is(Blocks.NETHER_PORTAL) || state.is(Blocks.CRYING_OBSIDIAN) || state.is(Blocks.OBSIDIAN)) {
                if (hasTransformableNeighborWithinRadius(world, current, portalCenter, maxRadius)) {
                    frontier.add(current);
                }

                // Continue exploring from corrupted blocks to find more corruption
                for (Direction dir : DIRECTIONS) {
                    BlockPos neighbor = current.relative(dir);
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        toExplore.add(neighbor);
                    }
                }
            }
        }

        if (frontier.isEmpty()) {
            frontier.add(portalCenter);
        }

        FesteringPortal.LOGGER.debug("Found corruption edge with {} frontier blocks at {} (explored {} blocks, max radius {})",
            frontier.size(), portalCenter, blocksExplored, maxRadius);
        return frontier;
    }

    /**
     * Check if a position has any transformable (uncorrupted) neighbors within max radius.
     */
    private static boolean hasTransformableNeighborWithinRadius(ServerLevel world, BlockPos pos, BlockPos portalCenter, int maxRadius) {
        for (Direction dir : DIRECTIONS) {
            BlockPos neighbor = pos.relative(dir);

            double distSq = neighbor.distSqr(portalCenter);
            if (distSq > (double) maxRadius * maxRadius) continue;

            if (!world.hasChunkAt(neighbor)) continue;
            BlockState state = world.getBlockState(neighbor);
            if (!BlockTransformations.isImmune(state) && BlockTransformations.canTransform(state)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Check if a position is within the allowed depth from surface.
     * Returns true if the position is at surface or within MAX_DEPTH_BELOW_SURFACE blocks below.
     */
    public static boolean isWithinDepthLimit(ServerLevel world, BlockPos pos) {
        int maxDepth = FesteringConfig.MAX_DEPTH_BELOW_SURFACE;

        // Search upward to find the surface (first non-solid block above solid)
        BlockPos checkPos = pos.above();
        int depthBelow = 0;

        while (depthBelow <= maxDepth + 10 && checkPos.getY() < world.getHeight()) {
            BlockState state = world.getBlockState(checkPos);

            if (!state.canOcclude() || state.isAir()) {
                return depthBelow <= maxDepth;
            }

            checkPos = checkPos.above();
            depthBelow++;
        }

        // If we searched far and found no surface, we're deep underground
        return false;
    }

    /**
     * Corrupt mobs within the corruption zone.
     * Called periodically to transform overworld mobs into nether variants.
     */
    public static void corruptMobs(
            ServerLevel world,
            FesteringPortalState.FesteringPortalData portal,
            RandomSource random) {

        if (!FesteringConfig.CORRUPT_MOBS) return;

        if (random.nextFloat() > FesteringConfig.MOB_CORRUPTION_CHANCE) return;

        int searchRadius = 20;
        AABB searchBox = new AABB(
                portal.center.getX() - searchRadius,
                portal.center.getY() - searchRadius,
                portal.center.getZ() - searchRadius,
                portal.center.getX() + searchRadius,
                portal.center.getY() + searchRadius,
                portal.center.getZ() + searchRadius
        );

        world.getEntitiesOfClass(Pig.class, searchBox, pig -> {
            BlockPos pigPos = pig.blockPosition();
            // Only corrupt if standing on corrupted ground
            BlockState groundState = world.getBlockState(pigPos.below());
            return BlockTransformations.isNetherBlock(groundState.getBlock()) && portal.isWithinMaxRadius(pigPos);
        }).stream().findFirst().ifPresent(pig -> {
            BlockPos pos = pig.blockPosition();
            pig.discard();

            var zombifiedPiglin = EntityTypes.ZOMBIFIED_PIGLIN.create(world, EntitySpawnReason.MOB_SUMMONED);
            if (zombifiedPiglin != null) {
                zombifiedPiglin.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, pig.getYRot(), pig.getXRot());
                world.addFreshEntity(zombifiedPiglin);
                spawnCorruptionParticles(world, pos);
                FesteringPortal.LOGGER.debug("Pig corrupted into Zombified Piglin at {}", pos);
            }
        });

        world.getEntitiesOfClass(Villager.class, searchBox, villager -> {
            BlockPos villagerPos = villager.blockPosition();
            BlockState groundState = world.getBlockState(villagerPos.below());
            return BlockTransformations.isNetherBlock(groundState.getBlock()) && portal.isWithinMaxRadius(villagerPos);
        }).stream().findFirst().ifPresent(villager -> {
            BlockPos pos = villager.blockPosition();
            VillagerData villagerData = villager.getVillagerData();
            villager.discard();

            ZombieVillager zombieVillager = EntityTypes.ZOMBIE_VILLAGER.create(world, EntitySpawnReason.MOB_SUMMONED);
            if (zombieVillager != null) {
                zombieVillager.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, villager.getYRot(), villager.getXRot());
                zombieVillager.setVillagerData(villagerData);
                world.addFreshEntity(zombieVillager);
                spawnCorruptionParticles(world, pos);
                FesteringPortal.LOGGER.debug("Villager corrupted into Zombie Villager at {}", pos);
            }
        });

        world.getEntitiesOfClass(Slime.class, searchBox, slime -> {
            BlockPos slimePos = slime.blockPosition();
            BlockState groundState = world.getBlockState(slimePos.below());
            return BlockTransformations.isNetherBlock(groundState.getBlock()) && portal.isWithinMaxRadius(slimePos);
        }).stream().findFirst().ifPresent(slime -> {
            BlockPos pos = slime.blockPosition();
            int size = slime.getSize();
            slime.discard();

            var magmaCube = EntityTypes.MAGMA_CUBE.create(world, EntitySpawnReason.MOB_SUMMONED);
            if (magmaCube != null) {
                magmaCube.setSize(size, false);
                magmaCube.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, slime.getYRot(), slime.getXRot());
                world.addFreshEntity(magmaCube);
                spawnCorruptionParticles(world, pos);
                FesteringPortal.LOGGER.debug("Slime corrupted into Magma Cube at {}", pos);
            }
        });

        world.getEntitiesOfClass(Horse.class, searchBox, horse -> {
            BlockPos horsePos = horse.blockPosition();
            BlockState groundState = world.getBlockState(horsePos.below());
            return BlockTransformations.isNetherBlock(groundState.getBlock()) && portal.isWithinMaxRadius(horsePos);
        }).stream().findFirst().ifPresent(horse -> {
            BlockPos pos = horse.blockPosition();
            horse.discard();

            var skeletonHorse = EntityTypes.SKELETON_HORSE.create(world, EntitySpawnReason.MOB_SUMMONED);
            if (skeletonHorse != null) {
                skeletonHorse.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, horse.getYRot(), horse.getXRot());
                world.addFreshEntity(skeletonHorse);
                spawnCorruptionParticles(world, pos);
                FesteringPortal.LOGGER.debug("Horse corrupted into Skeleton Horse at {}", pos);
            }
        });

    }
}
