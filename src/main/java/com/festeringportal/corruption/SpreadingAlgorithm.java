package com.festeringportal.corruption;

import com.festeringportal.FesteringPortal;
import com.festeringportal.config.FesteringConfig;
import com.festeringportal.data.FesteringPortalState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.world.World;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.VillagerData;

import java.util.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Grass-like organic spreading algorithm for nether corruption.
 * Spreads one block at a time from the corruption frontier, creating
 * natural, irregular edges similar to how grass spreads.
 */
public class SpreadingAlgorithm {

    // All 6 directions for neighbor checking
    private static final Direction[] DIRECTIONS = Direction.values();

    // Number of spread attempts per tick
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
            ServerWorld world,
            FesteringPortalState.FesteringPortalData portal,
            FesteringPortalState state,
            long currentTick) {

        Set<BlockPos> frontier = portal.corruptionFrontier;

        // If frontier is empty, reinitialize from portal
        if (frontier.isEmpty()) {
            Set<BlockPos> newFrontier = initializeFrontier(world, portal.center, portal.maxRadius);
            frontier.addAll(newFrontier);
        }

        // Remove any frontier blocks that are no longer on the edge
        cleanupFrontier(world, portal, frontier);

        if (frontier.isEmpty()) {
            return false;
        }

        boolean anySpread = false;
        Random random = world.getRandom();

        // Try multiple spread attempts per tick
        for (int attempt = 0; attempt < SPREADS_PER_TICK; attempt++) {
            if (frontier.isEmpty()) break;

            // Pick a random block from the frontier
            List<BlockPos> frontierList = new ArrayList<>(frontier);
            BlockPos spreadSource = frontierList.get(random.nextInt(frontierList.size()));

            // Try to spread to a random adjacent block
            List<Direction> shuffledDirections = new ArrayList<>(Arrays.asList(DIRECTIONS));
            Collections.shuffle(shuffledDirections, new java.util.Random(random.nextLong()));

            for (Direction direction : shuffledDirections) {
                BlockPos targetPos = spreadSource.offset(direction);

                // Check if within max radius
                if (!portal.isWithinMaxRadius(targetPos)) {
                    continue;
                }

                // Check if chunk is loaded
                if (!world.isChunkLoaded(targetPos)) {
                    continue;
                }

                BlockState targetState = world.getBlockState(targetPos);

                // Skip if immune
                if (BlockTransformations.isImmune(targetState)) {
                    continue;
                }

                // Skip if too deep below surface
                if (!isWithinDepthLimit(world, targetPos)) {
                    continue;
                }

                // Special handling for water - create supported lava pool
                if (targetState.isOf(Blocks.WATER)) {
                    if (transformWaterToLava(world, targetPos)) {
                        frontier.add(targetPos);
                        spawnCorruptionParticles(world, targetPos);
                        anySpread = true;
                        break;
                    }
                    continue;
                }

                // Get transformation
                BlockState transformedState = BlockTransformations.getTransformation(targetState);
                if (transformedState != null && !targetState.equals(transformedState)) {
                    // Perform the transformation
                    world.setBlockState(targetPos, transformedState, Block.NOTIFY_ALL);

                    // Add the newly corrupted block to the frontier
                    frontier.add(targetPos);

                    // Spawn particles for visual feedback
                    spawnCorruptionParticles(world, targetPos);

                    anySpread = true;
                    break;
                }
            }

            // Check if the source block should be removed from frontier
            if (shouldRemoveFromFrontier(world, portal, spreadSource)) {
                frontier.remove(spreadSource);
            }
        }

        // === MATURATION PASS ===
        // Also try to mature/evolve existing nether blocks based on neighbors
        // This creates diversity in the corrupted landscape
        matureNetherBlocks(world, portal, random);

        // Update the state
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
            ServerWorld world,
            FesteringPortalState.FesteringPortalData portal,
            FesteringPortalState state,
            int burstSize) {

        FesteringPortal.LOGGER.debug("Entity triggered corruption burst! Spreading {} blocks", burstSize);

        Set<BlockPos> frontier = portal.corruptionFrontier;

        // Ensure frontier is populated
        if (frontier.isEmpty()) {
            frontier.addAll(initializeFrontier(world, portal.center, portal.maxRadius));
        }

        int spread = 0;
        int attempts = 0;
        int maxAttempts = burstSize * 10;

        while (spread < burstSize && attempts < maxAttempts && !frontier.isEmpty()) {
            attempts++;

            List<BlockPos> frontierList = new ArrayList<>(frontier);
            Random random = world.getRandom();
            BlockPos spreadSource = frontierList.get(random.nextInt(frontierList.size()));

            for (Direction direction : DIRECTIONS) {
                BlockPos targetPos = spreadSource.offset(direction);

                if (!portal.isWithinMaxRadius(targetPos)) continue;
                if (!world.isChunkLoaded(targetPos)) continue;

                BlockState targetState = world.getBlockState(targetPos);
                if (BlockTransformations.isImmune(targetState)) continue;

                BlockState transformedState = BlockTransformations.getTransformation(targetState);
                if (transformedState != null && !targetState.equals(transformedState)) {
                    world.setBlockState(targetPos, transformedState, Block.NOTIFY_ALL);
                    frontier.add(targetPos);
                    spawnCorruptionParticles(world, targetPos);
                    spread++;
                    break;
                }
            }

            if (shouldRemoveFromFrontier(world, portal, spreadSource)) {
                frontier.remove(spreadSource);
            }
        }

        state.updateFrontier(portal.center, frontier, world.getTime());
        FesteringPortal.LOGGER.debug("Burst spread {} blocks", spread);
    }

    /**
     * Attempt to mature/evolve nether blocks within the corruption zone.
     * This creates diversity - netherrack becomes nylium, basalt becomes polished, etc.
     */
    private static void matureNetherBlocks(
            ServerWorld world,
            FesteringPortalState.FesteringPortalData portal,
            Random random) {

        // Pick random positions within the current corruption spread to mature
        int maturationAttempts = 2;
        int searchRadius = Math.min(20, portal.corruptionFrontier.size() > 0 ? 15 : 5);

        for (int i = 0; i < maturationAttempts; i++) {
            // Random position within search radius of portal center
            int dx = random.nextInt(searchRadius * 2 + 1) - searchRadius;
            int dy = random.nextInt(searchRadius * 2 + 1) - searchRadius;
            int dz = random.nextInt(searchRadius * 2 + 1) - searchRadius;

            BlockPos pos = portal.center.add(dx, dy, dz);

            if (!world.isChunkLoaded(pos)) continue;
            if (!portal.isWithinMaxRadius(pos)) continue;

            BlockState currentState = world.getBlockState(pos);

            // Only mature nether blocks (already corrupted)
            if (!isNetherCorruptedBlock(currentState)) continue;

            // Analyze neighbors and attempt maturation
            BlockTransformations.NeighborContext context =
                new BlockTransformations.NeighborContext(random);
            context.analyze(world, pos);

            BlockState maturedState = BlockTransformations.getMatureTransformation(currentState, context);

            if (maturedState != null && !currentState.equals(maturedState)) {
                world.setBlockState(pos, maturedState, Block.NOTIFY_ALL);

                // Subtle particles for maturation
                spawnMaturationParticles(world, pos);

                // If nylium formed, maybe spawn roots/fungus above
                if ((maturedState.isOf(Blocks.CRIMSON_NYLIUM) || maturedState.isOf(Blocks.WARPED_NYLIUM))
                        && context.hasAirAbove() && random.nextFloat() < 0.4f) {
                    BlockPos above = pos.up();
                    if (world.getBlockState(above).isAir()) {
                        BlockState vegetation = maturedState.isOf(Blocks.CRIMSON_NYLIUM)
                            ? (random.nextFloat() < 0.7f ? Blocks.CRIMSON_ROOTS.getDefaultState() : Blocks.CRIMSON_FUNGUS.getDefaultState())
                            : (random.nextFloat() < 0.7f ? Blocks.WARPED_ROOTS.getDefaultState() : Blocks.WARPED_FUNGUS.getDefaultState());
                        world.setBlockState(above, vegetation, Block.NOTIFY_ALL);
                    }
                }
            }
        }
    }

    /**
     * Spawn subtle particles for block maturation.
     */
    private static void spawnMaturationParticles(ServerWorld world, BlockPos pos) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        world.spawnParticles(
                ParticleTypes.CRIMSON_SPORE,
                x, y, z,
                2,
                0.3, 0.3, 0.3,
                0.01
        );
    }

    /**
     * Clean up the frontier by removing blocks that are no longer valid spread sources.
     */
    private static void cleanupFrontier(
            ServerWorld world,
            FesteringPortalState.FesteringPortalData portal,
            Set<BlockPos> frontier) {

        Iterator<BlockPos> iterator = frontier.iterator();
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();

            // Remove if outside max radius
            if (!portal.isWithinMaxRadius(pos)) {
                iterator.remove();
                continue;
            }

            // Remove if it can no longer spread (all neighbors done)
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
            ServerWorld world,
            FesteringPortalState.FesteringPortalData portal,
            BlockPos pos) {

        for (Direction direction : DIRECTIONS) {
            BlockPos neighbor = pos.offset(direction);

            // If outside radius, skip this neighbor
            if (!portal.isWithinMaxRadius(neighbor)) {
                continue;
            }

            // If chunk not loaded, assume it might be spreadable
            if (!world.isChunkLoaded(neighbor)) {
                return false;
            }

            BlockState neighborState = world.getBlockState(neighbor);

            // If neighbor is not immune and can be transformed, keep in frontier
            if (!BlockTransformations.isImmune(neighborState) &&
                    BlockTransformations.canTransform(neighborState)) {
                return false;
            }
        }

        // All neighbors are done
        return true;
    }

    /**
     * Transform water into a contained lava pool.
     * Creates a minimal lava pocket - just floor, 4 cardinal walls, and lava center.
     * Only works on surface water (water with air above).
     */
    private static boolean transformWaterToLava(ServerWorld world, BlockPos waterPos) {
        Random random = world.getRandom();

        // Only transform SURFACE water - must have air or non-water above
        BlockState above = world.getBlockState(waterPos.up());
        if (above.isOf(Blocks.WATER)) {
            return false;
        }

        // Create minimal lava pocket: floor below + 4 cardinal walls + lava center

        // Floor below lava
        BlockPos floorPos = waterPos.down();
        BlockState floorState = world.getBlockState(floorPos);
        if (floorState.isOf(Blocks.WATER) || !floorState.isSolidBlock(world, floorPos)) {
            world.setBlockState(floorPos, getRandomWallBlock(random).getDefaultState(), Block.NOTIFY_ALL);
        }

        // 4 cardinal walls (only if water/air)
        for (Direction dir : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
            BlockPos wallPos = waterPos.offset(dir);
            BlockState wallState = world.getBlockState(wallPos);
            if (wallState.isOf(Blocks.WATER) || wallState.isAir()) {
                world.setBlockState(wallPos, getRandomWallBlock(random).getDefaultState(), Block.NOTIFY_ALL);
            }
        }

        // Center becomes lava
        world.setBlockState(waterPos, Blocks.LAVA.getDefaultState(), Block.NOTIFY_ALL);
        spawnCorruptionParticles(world, waterPos);

        // Steam effect
        world.spawnParticles(
                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                waterPos.getX() + 0.5, waterPos.getY() + 1, waterPos.getZ() + 0.5,
                5, 0.3, 0.3, 0.3, 0.02
        );

        return true;
    }

    /**
     * Get a random wall block for lava pool containment.
     */
    private static Block getRandomWallBlock(Random random) {
        float roll = random.nextFloat();
        if (roll < 0.4f) {
            return Blocks.STONE;
        } else if (roll < 0.7f) {
            return Blocks.COBBLESTONE;
        } else {
            return Blocks.OBSIDIAN;
        }
    }

    /**
     * Spawn particles at the corruption site for visual feedback.
     */
    private static void spawnCorruptionParticles(ServerWorld world, BlockPos pos) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        // Spawn soul fire flame particles
        world.spawnParticles(
                ParticleTypes.SOUL_FIRE_FLAME,
                x, y, z,
                3, // count
                0.3, 0.3, 0.3, // spread
                0.02 // speed
        );

        // Spawn smoke particles
        world.spawnParticles(
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
    public static Set<BlockPos> initializeFrontier(ServerWorld world, BlockPos portalCenter, int maxRadius) {
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

            // Stop if outside the portal's max radius
            double dist = Math.sqrt(current.getSquaredDistance(portalCenter));
            if (dist > maxRadius) continue;

            if (!world.isChunkLoaded(current)) continue;

            BlockState state = world.getBlockState(current);

            // If this is a corrupted block or portal block
            if (isNetherCorruptedBlock(state) || state.isOf(Blocks.NETHER_PORTAL) || state.isOf(Blocks.CRYING_OBSIDIAN) || state.isOf(Blocks.OBSIDIAN)) {
                // Check if it's an edge block (has transformable neighbors WITHIN max radius)
                if (hasTransformableNeighborWithinRadius(world, current, portalCenter, maxRadius)) {
                    frontier.add(current);
                }

                // Continue exploring from corrupted blocks to find more corruption
                for (Direction dir : DIRECTIONS) {
                    BlockPos neighbor = current.offset(dir);
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        toExplore.add(neighbor);
                    }
                }
            }
        }

        // If still empty, seed with portal center itself
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
    private static boolean hasTransformableNeighborWithinRadius(ServerWorld world, BlockPos pos, BlockPos portalCenter, int maxRadius) {
        for (Direction dir : DIRECTIONS) {
            BlockPos neighbor = pos.offset(dir);

            // Skip if neighbor is outside max radius
            double dist = Math.sqrt(neighbor.getSquaredDistance(portalCenter));
            if (dist > maxRadius) continue;

            if (!world.isChunkLoaded(neighbor)) continue;
            BlockState state = world.getBlockState(neighbor);
            if (!BlockTransformations.isImmune(state) && BlockTransformations.canTransform(state)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a position has any transformable (uncorrupted) neighbors.
     */
    private static boolean hasTransformableNeighbor(ServerWorld world, BlockPos pos) {
        for (Direction dir : DIRECTIONS) {
            BlockPos neighbor = pos.offset(dir);
            if (!world.isChunkLoaded(neighbor)) continue;
            BlockState state = world.getBlockState(neighbor);
            if (!BlockTransformations.isImmune(state) && BlockTransformations.canTransform(state)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a position has any corrupted (nether) neighbors.
     */
    private static boolean hasCorruptedNeighbor(ServerWorld world, BlockPos pos) {
        for (Direction dir : DIRECTIONS) {
            BlockPos neighbor = pos.offset(dir);
            if (!world.isChunkLoaded(neighbor)) continue;
            BlockState state = world.getBlockState(neighbor);
            if (isNetherCorruptedBlock(state) || state.isOf(Blocks.NETHER_PORTAL)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a block is a nether-corrupted block that can act as a spread source.
     */
    private static boolean isNetherCorruptedBlock(BlockState state) {
        Block block = state.getBlock();
        return block == Blocks.NETHERRACK ||
                block == Blocks.SOUL_SOIL ||
                block == Blocks.SOUL_SAND ||
                block == Blocks.BASALT ||
                block == Blocks.BLACKSTONE ||
                block == Blocks.CRIMSON_STEM ||
                block == Blocks.WARPED_STEM ||
                block == Blocks.NETHER_WART_BLOCK ||
                block == Blocks.WARPED_WART_BLOCK ||
                block == Blocks.MAGMA_BLOCK;
    }

    /**
     * Check if a position is within the allowed depth from surface.
     * Returns true if the position is at surface or within MAX_DEPTH_BELOW_SURFACE blocks below.
     */
    public static boolean isWithinDepthLimit(ServerWorld world, BlockPos pos) {
        int maxDepth = FesteringConfig.MAX_DEPTH_BELOW_SURFACE;

        // Search upward to find the surface (first non-solid block above solid)
        BlockPos checkPos = pos.up();
        int depthBelow = 0;

        while (depthBelow <= maxDepth + 10 && checkPos.getY() < world.getHeight()) {
            BlockState state = world.getBlockState(checkPos);

            // If we find air/non-solid, we've found the surface
            if (!state.isOpaque() || state.isAir()) {
                // Current position is 'depthBelow' blocks below surface
                return depthBelow <= maxDepth;
            }

            checkPos = checkPos.up();
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
            ServerWorld world,
            FesteringPortalState.FesteringPortalData portal,
            Random random) {

        if (!FesteringConfig.CORRUPT_MOBS) return;

        // Only attempt corruption occasionally
        if (random.nextFloat() > FesteringConfig.MOB_CORRUPTION_CHANCE) return;

        // Search area around the corruption frontier
        int searchRadius = 20;
        Box searchBox = new Box(
                portal.center.getX() - searchRadius,
                portal.center.getY() - searchRadius,
                portal.center.getZ() - searchRadius,
                portal.center.getX() + searchRadius,
                portal.center.getY() + searchRadius,
                portal.center.getZ() + searchRadius
        );

        // Find pigs and convert to zombified piglins
        world.getEntitiesByClass(PigEntity.class, searchBox, pig -> {
            BlockPos pigPos = pig.getBlockPos();
            // Only corrupt if standing on corrupted ground
            BlockState groundState = world.getBlockState(pigPos.down());
            return isNetherCorruptedBlock(groundState) && portal.isWithinMaxRadius(pigPos);
        }).stream().findFirst().ifPresent(pig -> {
            BlockPos pos = pig.getBlockPos();
            pig.discard();

            // Spawn zombified piglin
            var zombifiedPiglin = EntityType.ZOMBIFIED_PIGLIN.create(world, SpawnReason.MOB_SUMMONED);
            if (zombifiedPiglin != null) {
                zombifiedPiglin.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, pig.getYaw(), pig.getPitch());
                world.spawnEntity(zombifiedPiglin);
                spawnCorruptionParticles(world, pos);
                FesteringPortal.LOGGER.debug("Pig corrupted into Zombified Piglin at {}", pos);
            }
        });

        // Find villagers and convert to zombie villagers
        world.getEntitiesByClass(VillagerEntity.class, searchBox, villager -> {
            BlockPos villagerPos = villager.getBlockPos();
            BlockState groundState = world.getBlockState(villagerPos.down());
            return isNetherCorruptedBlock(groundState) && portal.isWithinMaxRadius(villagerPos);
        }).stream().findFirst().ifPresent(villager -> {
            BlockPos pos = villager.getBlockPos();
            VillagerData villagerData = villager.getVillagerData();
            villager.discard();

            // Spawn zombie villager
            ZombieVillagerEntity zombieVillager = EntityType.ZOMBIE_VILLAGER.create(world, SpawnReason.MOB_SUMMONED);
            if (zombieVillager != null) {
                zombieVillager.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, villager.getYaw(), villager.getPitch());
                zombieVillager.setVillagerData(villagerData);
                world.spawnEntity(zombieVillager);
                spawnCorruptionParticles(world, pos);
                FesteringPortal.LOGGER.debug("Villager corrupted into Zombie Villager at {}", pos);
            }
        });

        // Find slimes and convert to magma cubes
        world.getEntitiesByClass(SlimeEntity.class, searchBox, slime -> {
            BlockPos slimePos = slime.getBlockPos();
            BlockState groundState = world.getBlockState(slimePos.down());
            return isNetherCorruptedBlock(groundState) && portal.isWithinMaxRadius(slimePos);
        }).stream().findFirst().ifPresent(slime -> {
            BlockPos pos = slime.getBlockPos();
            int size = slime.getSize();
            slime.discard();

            var magmaCube = EntityType.MAGMA_CUBE.create(world, SpawnReason.MOB_SUMMONED);
            if (magmaCube != null) {
                magmaCube.setSize(size, false);
                magmaCube.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, slime.getYaw(), slime.getPitch());
                world.spawnEntity(magmaCube);
                spawnCorruptionParticles(world, pos);
                FesteringPortal.LOGGER.debug("Slime corrupted into Magma Cube at {}", pos);
            }
        });

        // Find horses and convert to skeleton horses
        world.getEntitiesByClass(HorseEntity.class, searchBox, horse -> {
            BlockPos horsePos = horse.getBlockPos();
            BlockState groundState = world.getBlockState(horsePos.down());
            return isNetherCorruptedBlock(groundState) && portal.isWithinMaxRadius(horsePos);
        }).stream().findFirst().ifPresent(horse -> {
            BlockPos pos = horse.getBlockPos();
            horse.discard();

            var skeletonHorse = EntityType.SKELETON_HORSE.create(world, SpawnReason.MOB_SUMMONED);
            if (skeletonHorse != null) {
                skeletonHorse.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, horse.getYaw(), horse.getPitch());
                world.spawnEntity(skeletonHorse);
                spawnCorruptionParticles(world, pos);
                FesteringPortal.LOGGER.debug("Horse corrupted into Skeleton Horse at {}", pos);
            }
        });

    }
}
