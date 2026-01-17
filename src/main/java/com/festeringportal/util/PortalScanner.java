package com.festeringportal.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for scanning portal frames to detect crying obsidian blocks.
 */
public class PortalScanner {

    // Maximum portal dimensions to search
    private static final int MAX_PORTAL_WIDTH = 21;
    private static final int MAX_PORTAL_HEIGHT = 21;
    private static final int SEARCH_RADIUS = 3;

    /**
     * Count the number of crying obsidian blocks in the portal frame near the given position.
     * Searches outward from the fire position to find and analyze the portal frame.
     *
     * @param world The server world
     * @param firePos The position where fire was placed (inside the portal)
     * @return The number of crying obsidian blocks in the frame
     */
    public static int countCryingObsidianInFrame(ServerWorld world, BlockPos firePos) {
        Set<BlockPos> frameBlocks = findFrameBlocks(world, firePos);
        int count = 0;
        for (BlockPos pos : frameBlocks) {
            BlockState state = world.getBlockState(pos);
            if (state.isOf(Blocks.CRYING_OBSIDIAN)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Find all frame blocks of the portal containing the given position.
     *
     * @param world The server world
     * @param portalPos A position inside or near the portal
     * @return Set of all frame block positions
     */
    public static Set<BlockPos> findFrameBlocks(ServerWorld world, BlockPos portalPos) {
        Set<BlockPos> frameBlocks = new HashSet<>();

        // Try both portal orientations (X and Z axis)
        for (Direction.Axis axis : new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}) {
            Set<BlockPos> found = findFrameBlocksForAxis(world, portalPos, axis);
            if (!found.isEmpty()) {
                frameBlocks.addAll(found);
            }
        }

        return frameBlocks;
    }

    /**
     * Find frame blocks for a specific portal axis orientation.
     */
    private static Set<BlockPos> findFrameBlocksForAxis(ServerWorld world, BlockPos startPos, Direction.Axis axis) {
        Set<BlockPos> frameBlocks = new HashSet<>();

        // Determine the horizontal directions based on axis
        Direction widthDir = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        Direction depthDir = axis == Direction.Axis.X ? Direction.SOUTH : Direction.EAST;

        // Search for the portal interior by finding adjacent portal blocks
        BlockPos portalInterior = findPortalInterior(world, startPos, widthDir);
        if (portalInterior == null) {
            return frameBlocks;
        }

        // Find the lower-left corner of the portal interior
        BlockPos lowerCorner = findLowerCorner(world, portalInterior, widthDir);
        if (lowerCorner == null) {
            return frameBlocks;
        }

        // Measure portal dimensions
        int width = measureWidth(world, lowerCorner, widthDir);
        int height = measureHeight(world, lowerCorner);

        if (width <= 0 || height <= 0) {
            return frameBlocks;
        }

        // Collect frame blocks
        // Bottom frame (below portal)
        for (int i = -1; i <= width; i++) {
            frameBlocks.add(lowerCorner.offset(widthDir, i).down());
        }

        // Top frame (above portal)
        for (int i = -1; i <= width; i++) {
            frameBlocks.add(lowerCorner.offset(widthDir, i).up(height));
        }

        // Left frame
        for (int j = 0; j < height; j++) {
            frameBlocks.add(lowerCorner.offset(widthDir, -1).up(j));
        }

        // Right frame
        for (int j = 0; j < height; j++) {
            frameBlocks.add(lowerCorner.offset(widthDir, width).up(j));
        }

        return frameBlocks;
    }

    /**
     * Find a portal block near the start position.
     */
    private static BlockPos findPortalInterior(ServerWorld world, BlockPos startPos, Direction widthDir) {
        // Check if start pos is already a portal block
        if (world.getBlockState(startPos).isOf(Blocks.NETHER_PORTAL)) {
            return startPos;
        }

        // Search nearby for portal blocks
        for (int dx = -SEARCH_RADIUS; dx <= SEARCH_RADIUS; dx++) {
            for (int dy = -SEARCH_RADIUS; dy <= SEARCH_RADIUS; dy++) {
                for (int dz = -SEARCH_RADIUS; dz <= SEARCH_RADIUS; dz++) {
                    BlockPos checkPos = startPos.add(dx, dy, dz);
                    if (world.getBlockState(checkPos).isOf(Blocks.NETHER_PORTAL)) {
                        return checkPos;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Find the lower-left corner of the portal interior.
     */
    private static BlockPos findLowerCorner(ServerWorld world, BlockPos portalPos, Direction widthDir) {
        BlockPos corner = portalPos;

        // Move down to find the bottom
        while (world.getBlockState(corner.down()).isOf(Blocks.NETHER_PORTAL)) {
            corner = corner.down();
        }

        // Move in negative width direction to find left edge
        Direction negativeDir = widthDir.getOpposite();
        while (world.getBlockState(corner.offset(negativeDir)).isOf(Blocks.NETHER_PORTAL)) {
            corner = corner.offset(negativeDir);
        }

        return corner;
    }

    /**
     * Measure the width of the portal (number of portal blocks horizontally).
     */
    private static int measureWidth(ServerWorld world, BlockPos lowerCorner, Direction widthDir) {
        int width = 0;
        BlockPos checkPos = lowerCorner;

        while (width < MAX_PORTAL_WIDTH && world.getBlockState(checkPos).isOf(Blocks.NETHER_PORTAL)) {
            width++;
            checkPos = checkPos.offset(widthDir);
        }

        return width;
    }

    /**
     * Measure the height of the portal (number of portal blocks vertically).
     */
    private static int measureHeight(ServerWorld world, BlockPos lowerCorner) {
        int height = 0;
        BlockPos checkPos = lowerCorner;

        while (height < MAX_PORTAL_HEIGHT && world.getBlockState(checkPos).isOf(Blocks.NETHER_PORTAL)) {
            height++;
            checkPos = checkPos.up();
        }

        return height;
    }

    /**
     * Calculate the center position of a portal.
     */
    public static BlockPos calculatePortalCenter(ServerWorld world, BlockPos portalPos) {
        BlockPos interior = findPortalInterior(world, portalPos, Direction.EAST);
        if (interior == null) {
            return portalPos;
        }

        // Find lower corner and dimensions
        BlockPos lowerCorner = findLowerCorner(world, interior, Direction.EAST);
        if (lowerCorner == null) {
            lowerCorner = findLowerCorner(world, interior, Direction.SOUTH);
        }
        if (lowerCorner == null) {
            return portalPos;
        }

        int width = measureWidth(world, lowerCorner, Direction.EAST);
        if (width == 0) {
            width = measureWidth(world, lowerCorner, Direction.SOUTH);
        }
        int height = measureHeight(world, lowerCorner);

        return lowerCorner.add(width / 2, height / 2, 0);
    }

    /**
     * Check if a block is a valid portal frame block (obsidian or crying obsidian).
     */
    public static boolean isFrameBlock(BlockState state) {
        return state.isOf(Blocks.OBSIDIAN) || state.isOf(Blocks.CRYING_OBSIDIAN);
    }
}
