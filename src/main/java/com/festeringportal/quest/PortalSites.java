package com.festeringportal.quest;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Half-finished frames waiting for their chunk to exist.
 *
 * <p>The cleric's frame stands a long way from the village, which is the whole
 * point of it, and a long way away means the chunk is not loaded when the quest
 * is accepted. Placing blocks there anyway would drag the chunk in on the server
 * thread and hang it, which this suite has already done once tonight.
 *
 * <p>So the site is written down and built later, on an ordinary tick, once the
 * world has that chunk for its own reasons. The player walks toward it and finds
 * it standing, which is also the fiction: somebody else built this a while ago.
 */
public final class PortalSites {
	private PortalSites() {}

	private record Pending(int x, int z, BlockPos[] built) {}

	private static final List<Pending> PENDING = new ArrayList<>();

	/** Frame footprint: a 4x5 portal with the corners left out, ten blocks in all. */
	private static final int[][] FRAME = {
		{1, 0}, {2, 0},
		{0, 1}, {0, 2}, {0, 3},
		{3, 1}, {3, 2}, {3, 3},
		{1, 4}, {2, 4}
	};

	/** The half the cleric managed: one upright, the sill, and a start on the lintel. */
	private static final int PLACED_BY_CLERIC = 6;

	public static synchronized void expect(int x, int z) {
		PENDING.add(new Pending(x, z, null));
	}

	/** Called from the mod's own tick. Builds any site whose chunk has turned up. */
	public static synchronized void tick(ServerLevel world) {
		if (PENDING.isEmpty()) return;

		PENDING.removeIf(site -> {
			BlockPos probe = new BlockPos(site.x(), world.getSeaLevel(), site.z());
			if (!world.isLoaded(probe)) return false;

			build(world, site.x(), site.z());
			return true;
		});
	}

	private static void build(ServerLevel world, int x, int z) {
		int groundY = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
		BlockPos base = new BlockPos(x, groundY, z);

		for (int i = 0; i < PLACED_BY_CLERIC; i++) {
			BlockPos at = base.offset(FRAME[i][0], FRAME[i][1], 0);
			world.setBlockAndUpdate(at, Blocks.OBSIDIAN.defaultBlockState());
		}
	}

	/** Where the portal will stand if somebody finishes it: the middle of the frame. */
	public static BlockPos interior(ServerLevel world, int x, int z) {
		int groundY = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
		return new BlockPos(x + 1, groundY + 2, z);
	}
}
