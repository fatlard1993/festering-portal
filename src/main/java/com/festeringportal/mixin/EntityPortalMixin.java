package com.festeringportal.mixin;

import com.festeringportal.FesteringPortal;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
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
        method = "teleportTo",
        at = @At("RETURN")
    )
    private void onEntityTeleport(TeleportTarget target, CallbackInfoReturnable<Entity> cir) {
        Entity result = cir.getReturnValue();
        if (result == null) return;

        // Only trigger for portal teleportation
        if (target.postTeleportTransition() != TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET) return;

        World world = result.getEntityWorld();
        if (!(world instanceof ServerWorld serverWorld)) return;
        if (world.getRegistryKey() != World.OVERWORLD) return;

        FesteringPortal.onEntityPortalArrival(serverWorld, result.getBlockPos());
    }
}
