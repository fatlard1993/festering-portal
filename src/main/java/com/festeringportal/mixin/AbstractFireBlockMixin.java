package com.festeringportal.mixin;

import com.festeringportal.FesteringPortal;
import com.festeringportal.util.PortalScanner;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.NetherPortal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

/**
 * Mixin to:
 * 1. Allow crying obsidian to trigger portal lighting (shouldLightPortalAt check)
 * 2. Detect when a portal is created to check for crying obsidian frame blocks
 */
@Mixin(AbstractFireBlock.class)
public class AbstractFireBlockMixin {

    /**
     * Redirect the obsidian check in shouldLightPortalAt to also accept crying obsidian.
     * This is the pre-check that runs before portal validation.
     */
    @Redirect(
        method = "shouldLightPortalAt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z",
            ordinal = 0
        )
    )
    private static boolean redirectShouldLightObsidianCheck(BlockState state, Block block) {
        // Accept both regular obsidian and crying obsidian
        return state.isOf(Blocks.OBSIDIAN) || state.isOf(Blocks.CRYING_OBSIDIAN);
    }

    /**
     * Inject after a portal is created to check if it contains crying obsidian.
     * If it does, register it as a festering portal.
     */
    @Inject(
        method = "onBlockAdded",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/dimension/NetherPortal;createPortal(Lnet/minecraft/world/WorldAccess;)V",
            shift = At.Shift.AFTER
        ),
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void onPortalCreated(
            BlockState state,
            World world,
            BlockPos pos,
            BlockState oldState,
            boolean notify,
            CallbackInfo ci,
            Optional<NetherPortal> optional) {

        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }

        if (optional == null || optional.isEmpty()) {
            return;
        }

        // Scan the area around the fire position to find portal frame blocks
        int cryingObsidianCount = PortalScanner.countCryingObsidianInFrame(serverWorld, pos);

        if (cryingObsidianCount > 0) {
            // This is a festering portal! Register it.
            FesteringPortal.onFesteringPortalCreated(serverWorld, pos, cryingObsidianCount);
        }
    }
}
