package com.festeringportal.quest;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import justfatlard.village_quests.quest.VillagerQuest;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

/**
 * A cleric who built half a portal, thought better of it, and would like
 * somebody else to find out what happens.
 *
 * <p>Crying obsidian in a lit frame corrupts sixty-four blocks of overworld per
 * stone. Nobody discovers that by accident and nobody should discover it beside
 * their own house, so the frame stands a long way out, in a direction rather
 * than at a coordinate. Finding it is part of the errand.
 *
 * <p>The materials come from the cleric because the amount matters: four stones
 * to finish it and exactly one of them crying, which is the smallest festering
 * portal there is. They are not being generous, they are being careful, and they
 * are still not going themselves.
 */
public class FinishTheFrameQuest extends VillagerQuest {
	/** Far enough that sixty-four blocks of corruption never reaches the village. */
	private static final int DISTANCE = 150;

	private int siteX;
	private int siteZ;
	private String heading = "out past the fields";
	private boolean sited = false;

	public FinishTheFrameQuest(String requesterName, UUID villagerUuid) {
		super(VillagerQuest.QuestType.MYSTERY, requesterName, villagerUuid, 12);
	}

	@Override
	public String getDescription() {
		ThreadLocalRandom rng = ThreadLocalRandom.current();
		String[] lines = {
			this.requesterName + ": \"I built something " + this.heading + " and I have not been back to it. "
				+ "It is a frame, and it is not finished, and I have decided I do not want to be the one who finishes it.\"",
			this.requesterName + ": \"There is a doorway standing half-built " + this.heading + ". I carried every stone of it myself "
				+ "and then I stopped. Here is what is left. I would like to know what it does, and I would like not to be there.\"",
			this.requesterName + ": \"Something " + this.heading + " that I started and could not make myself end. "
				+ "Take these. Finish it, light it, and come back and tell me exactly what you saw.\""
		};
		return lines[rng.nextInt(lines.length)];
	}

	@Override
	public String getObjective() {
		return "finish the cleric's frame " + this.heading + ", light it, and come back - "
			+ "the crying stone is the part they are frightened of";
	}

	@Override
	public void onAccept(ServerPlayer player) {
		if (this.sited) return;

		// A direction and a distance, not a waypoint: the cleric knows roughly
		// where they left it, the way a person knows.
		ThreadLocalRandom rng = ThreadLocalRandom.current();
		String[] names = {"to the north", "to the northeast", "to the east", "to the southeast",
			"to the south", "to the southwest", "to the west", "to the northwest"};
		int octant = rng.nextInt(8);
		double angle = octant * Math.PI / 4.0;

		this.siteX = player.blockPosition().getX() + (int) Math.round(Math.sin(angle) * DISTANCE);
		this.siteZ = player.blockPosition().getZ() - (int) Math.round(Math.cos(angle) * DISTANCE);
		this.heading = names[octant];
		this.sited = true;

		PortalSites.expect(this.siteX, this.siteZ);

		// Exactly enough, and exactly one crying stone. The smallest version of
		// the thing they are afraid of.
		give(player, new ItemStack(Blocks.OBSIDIAN, 3));
		give(player, new ItemStack(Blocks.CRYING_OBSIDIAN, 1));
		give(player, new ItemStack(Items.FLINT_AND_STEEL, 1));
	}

	private static void give(ServerPlayer player, ItemStack stack) {
		if (!player.getInventory().add(stack)) {
			player.drop(stack, false, net.minecraft.util.Prediction.SERVER_ONLY);
		}
	}

	@Override
	public boolean checkCompletion(ServerPlayer player) {
		if (!this.sited) return false;
		if (!(player.level() instanceof ServerLevel world)) return false;

		BlockPos interior = PortalSites.interior(world, this.siteX, this.siteZ);
		if (!world.isLoaded(interior)) return false;

		// Lit is the whole question. A finished frame that nobody dared strike is
		// the cleric's own position, and they already have one of those.
		for (int dy = -1; dy <= 3; dy++) {
			if (world.getBlockState(interior.above(dy)).is(Blocks.NETHER_PORTAL)) return true;
		}
		return false;
	}

	@Override
	public void onComplete(ServerPlayer player) {
		// Nothing is taken and nothing is undone. It is burning out there now, and
		// that is the finding they asked for.
	}
}
