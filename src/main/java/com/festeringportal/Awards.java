package com.festeringportal;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * The advancements this mod hands out.
 *
 * <p>Given to whoever is standing closest to a portal as it lights, which is a guess and worth
 * naming as one: the game hands portal creation to a block, not to a person, so the striker is not
 * in the room by the time this is called. Somebody within a few blocks of a frame at the moment it
 * catches is the person who lit it in every case that matters, and in the odd case where a friend
 * is nearer, they were both standing there watching it anyway.
 */
public final class Awards {
	private Awards() {}

	/** How far from the frame the one who lit it can plausibly be. */
	private static final double ARM_LENGTH = 8.0;

	public static void portalLit(ServerLevel world, BlockPos portalPos) {
		Player nearest = world.getNearestPlayer(portalPos.getX() + 0.5, portalPos.getY() + 0.5,
			portalPos.getZ() + 0.5, ARM_LENGTH, false);
		if (nearest instanceof ServerPlayer player) award(player, "festering");
	}

	private static void award(ServerPlayer player, String path) {
		if (player.level().getServer() == null) return;
		AdvancementHolder holder = player.level().getServer().getAdvancements()
			.get(Identifier.fromNamespaceAndPath(FesteringPortal.MOD_ID, path));
		if (holder == null) return;

		AdvancementProgress progress = player.getAdvancements().getOrStartProgress(holder);
		if (progress.isDone()) return;
		for (String criterion : progress.getRemainingCriteria()) {
			player.getAdvancements().award(holder, criterion);
		}
	}
}
