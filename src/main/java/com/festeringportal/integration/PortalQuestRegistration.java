package com.festeringportal.integration;

import com.festeringportal.quest.FinishTheFrameQuest;
import java.util.Random;
import justfatlard.village_quests.api.QuestRegistry;
import justfatlard.village_quests.quest.VillagerQuest;
import net.minecraft.world.entity.npc.villager.Villager;

/**
 * Offers the frame from the cleric, and only from a cleric who knows you.
 *
 * <p>Rare and late on purpose. It ends with sixty-four blocks of overworld
 * turning to netherrack, which is not something to hand to somebody who
 * wandered into town this morning.
 *
 * <p>Names village-quests types directly, so it must only be loaded behind the
 * isModLoaded guard in the entry point.
 */
public final class PortalQuestRegistration {
	private PortalQuestRegistration() {}

	private static final float OFFER_CHANCE = 0.06F;
	private static final int MIN_REPUTATION = 35;

	public static void register() {
		QuestRegistry.registerProfessionQuest("cleric", PortalQuestRegistration::offer);
	}

	private static VillagerQuest offer(Villager villager, String villagerName, int reputation, Random random) {
		if (reputation < MIN_REPUTATION) return null;
		if (random.nextFloat() > OFFER_CHANCE) return null;

		return new FinishTheFrameQuest(villagerName, villager.getUUID());
	}
}
