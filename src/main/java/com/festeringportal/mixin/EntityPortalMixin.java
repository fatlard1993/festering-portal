package com.festeringportal.mixin;

import com.festeringportal.FesteringPortal;
import com.festeringportal.corruption.SpreadingAlgorithm;
import com.festeringportal.data.FesteringPortalState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to detect when entities exit a nether portal and trigger corruption burst.
 * When a piglin, zombified piglin, or any entity comes through a festering portal,
 * it triggers an immediate burst of corruption spread.
 */
@Mixin(Entity.class)
public abstract class EntityPortalMixin {

    @Shadow
    public abstract World getWorld();

    /**
     * Inject after an entity teleports through a portal.
     * Check if they arrived near a festering portal and trigger corruption burst.
     */
    @Inject(
        method = "teleportTo",
        at = @At("RETURN")
    )
    private void onEntityTeleport(TeleportTarget target, CallbackInfoReturnable<Entity> cir) {
        Entity result = cir.getReturnValue();
        if (result == null) return;

        World world = result.getWorld();

        // Only trigger in the Overworld (entities coming FROM the nether)
        if (!(world instanceof ServerWorld serverWorld)) return;
        if (world.getRegistryKey() != World.OVERWORLD) return;

        BlockPos entityPos = result.getBlockPos();

        // Check if there's a festering portal nearby
        FesteringPortalState state = FesteringPortalState.getServerState(serverWorld.getServer());

        for (FesteringPortalState.FesteringPortalData portal : state.getPortals()) {
            double distance = Math.sqrt(portal.center.getSquaredDistance(entityPos));

            // If entity arrived within 10 blocks of a festering portal
            if (distance <= 10) {
                // Trigger a burst of corruption!
                // More crying obsidian = bigger burst
                int burstSize = portal.cryingObsidianCount * 5;
                SpreadingAlgorithm.burstSpread(serverWorld, portal, state, burstSize);

                FesteringPortal.LOGGER.info(
                    "Entity {} triggered corruption burst at {} (burst size: {})",
                    result.getType().getName().getString(),
                    portal.center,
                    burstSize
                );
                break;
            }
        }
    }
}
