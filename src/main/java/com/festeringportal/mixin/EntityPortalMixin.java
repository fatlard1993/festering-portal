package com.festeringportal.mixin;

import com.festeringportal.FesteringPortal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to detect when entities exit a nether portal and trigger corruption burst.
 */
@Mixin(Entity.class)
public abstract class EntityPortalMixin {

    @Inject(
        method = "teleport",
        at = @At("RETURN")
    )
    private void onEntityTeleport(TeleportTransition target, CallbackInfoReturnable<Entity> cir) {
        Entity result = cir.getReturnValue();
        if (result == null) return;

        // Only trigger for portal teleportation
        if (target.postTeleportTransition() != TeleportTransition.PLAY_PORTAL_SOUND) return;

        Level world = result.level();
        if (!(world instanceof ServerLevel serverWorld)) return;
        if (world.dimension() != Level.OVERWORLD) return;

        FesteringPortal.onEntityPortalArrival(serverWorld, result.blockPosition());
    }
}
