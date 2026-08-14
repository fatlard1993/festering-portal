package com.festeringportal.util;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Utility class for scanning portal frames to detect crying obsidian blocks.
 */
public class PortalScanner {

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
    public static int countCryingObsidianInFrame(ServerLevel world, BlockPos firePos) {
        Set<BlockPos> frameBlocks = findFrameBlocks(world, firePos);
        int count = 0;
        for (BlockPos pos : frameBlocks) {
            BlockState state = world.getBlockState(pos);
            if (state.is(Blocks.CRYING_OBSIDIAN)) {
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
    public static Set<BlockPos> findFrameBlocks(ServerLevel world, BlockPos portalPos) {
        Set<BlockPos> frameBlocks = new HashSet<>();

        for (Direction.Axis axis : new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}) {
            Set<BlockPos> found = findFrameBlocksForAxis(world, portalPos, axis);
            if (!found.isEmpty()) {
                frameBlocks.addAll(found);
            }
        }

        return frameBlocks;
    }

    private static Set<BlockPos> findFrameBlocksForAxis(ServerLevel world, BlockPos startPos, Direction.Axis axis) {
        Set<BlockPos> frameBlocks = new HashSet<>();

        Direction widthDir = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;

        BlockPos portalInterior = findPortalInterior(world, startPos);
        if (portalInterior == null) {
            return frameBlocks;
        }

        BlockPos lowerCorner = findLowerCorner(world, portalInterior, widthDir);
        if (lowerCorner == null) {
            return frameBlocks;
        }

        int width = measureWidth(world, lowerCorner, widthDir);
        int height = measureHeight(world, lowerCorner);

        if (width <= 0 || height <= 0) {
            return frameBlocks;
        }

        // Bottom frame (below portal)
        for (int i = -1; i <= width; i++) {
            frameBlocks.add(lowerCorner.relative(widthDir, i).below());
        }

        // Top frame (above portal)
        for (int i = -1; i <= width; i++) {
            frameBlocks.add(lowerCorner.relative(widthDir, i).above(height));
        }

        // Left frame
        for (int j = 0; j < height; j++) {
            frameBlocks.add(lowerCorner.relative(widthDir, -1).above(j));
        }

        // Right frame
        for (int j = 0; j < height; j++) {
            frameBlocks.add(lowerCorner.relative(widthDir, width).above(j));
        }

        return frameBlocks;
    }

    private static BlockPos findPortalInterior(ServerLevel world, BlockPos startPos) {
        if (world.getBlockState(startPos).is(Blocks.NETHER_PORTAL)) {
            return startPos;
        }

        for (int dx = -SEARCH_RADIUS; dx <= SEARCH_RADIUS; dx++) {
            for (int dy = -SEARCH_RADIUS; dy <= SEARCH_RADIUS; dy++) {
                for (int dz = -SEARCH_RADIUS; dz <= SEARCH_RADIUS; dz++) {
                    BlockPos checkPos = startPos.offset(dx, dy, dz);
                    if (world.getBlockState(checkPos).is(Blocks.NETHER_PORTAL)) {
                        return checkPos;
                    }
                }
            }
        }
        return null;
    }

    private static BlockPos findLowerCorner(ServerLevel world, BlockPos portalPos, Direction widthDir) {
        BlockPos corner = portalPos;

        while (world.getBlockState(corner.below()).is(Blocks.NETHER_PORTAL)) {
            corner = corner.below();
        }

        Direction negativeDir = widthDir.getOpposite();
        while (world.getBlockState(corner.relative(negativeDir)).is(Blocks.NETHER_PORTAL)) {
            corner = corner.relative(negativeDir);
        }

        return corner;
    }

    private static int measureWidth(ServerLevel world, BlockPos lowerCorner, Direction widthDir) {
        int width = 0;
        BlockPos checkPos = lowerCorner;

        while (width < MAX_PORTAL_WIDTH && world.getBlockState(checkPos).is(Blocks.NETHER_PORTAL)) {
            width++;
            checkPos = checkPos.relative(widthDir);
        }

        return width;
    }

    private static int measureHeight(ServerLevel world, BlockPos lowerCorner) {
        int height = 0;
        BlockPos checkPos = lowerCorner;

        while (height < MAX_PORTAL_HEIGHT && world.getBlockState(checkPos).is(Blocks.NETHER_PORTAL)) {
            height++;
            checkPos = checkPos.above();
        }

        return height;
    }

    /**
     * Calculate the center position of a portal.
     * Measures both axes and picks the wider one to correctly detect portal orientation.
     */
    public static BlockPos calculatePortalCenter(ServerLevel world, BlockPos portalPos) {
        BlockPos interior = findPortalInterior(world, portalPos);
        if (interior == null) {
            return portalPos;
        }

        BlockPos eastCorner = findLowerCorner(world, interior, Direction.EAST);
        int eastWidth = eastCorner != null ? measureWidth(world, eastCorner, Direction.EAST) : 0;

        BlockPos southCorner = findLowerCorner(world, interior, Direction.SOUTH);
        int southWidth = southCorner != null ? measureWidth(world, southCorner, Direction.SOUTH) : 0;

        Direction widthDir;
        BlockPos lowerCorner;
        int width;

        if (southWidth > eastWidth) {
            widthDir = Direction.SOUTH;
            lowerCorner = southCorner;
            width = southWidth;
        } else {
            widthDir = Direction.EAST;
            lowerCorner = eastCorner;
            width = eastWidth;
        }

        if (lowerCorner == null || width == 0) {
            return portalPos;
        }

        int height = measureHeight(world, lowerCorner);

        if (widthDir == Direction.SOUTH) {
            return lowerCorner.offset(0, height / 2, width / 2);
        }
        return lowerCorner.offset(width / 2, height / 2, 0);
    }
}
