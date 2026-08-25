package com.festeringportal.mixin;

import com.festeringportal.FesteringPortal;
import com.festeringportal.util.PortalScanner;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;

/**
 * Mixin to:
 * 1. Allow crying obsidian to trigger portal lighting (shouldLightPortalAt check)
 * 2. Detect when a portal is created to check for crying obsidian frame blocks
 */
@Mixin(BaseFireBlock.class)
public class AbstractFireBlockMixin {

    /**
     * Wrap the frame check in isPortal to also accept crying obsidian.
     *
     * <p>26.3-snapshot-10 replaced an inline {@code state.is(Blocks.OBSIDIAN)} here with a shared
     * {@link net.minecraft.world.level.portal.PortalShape#FRAME} predicate. The target is a string,
     * so the build stays green and the mixin simply stops applying: this only surfaces on a launch.
     */
    @WrapOperation(
        method = "isPortal",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z",
            ordinal = 0
        )
    )
    private static boolean wrapShouldLightObsidianCheck(Predicate<BlockState> frame, Object state,
            Operation<Boolean> original) {
        return original.call(frame, state) || ((BlockState) state).is(Blocks.CRYING_OBSIDIAN);
    }

    /**
     * Inject after a portal is created to check if it contains crying obsidian.
     * If it does, register it as a festering portal.
     */
    @Inject(
        method = "onPlace",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/portal/PortalShape;createPortalBlocks(Lnet/minecraft/world/level/LevelAccessor;)V",
            shift = At.Shift.AFTER
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onPortalCreated(
            BlockState state,
            Level world,
            BlockPos pos,
            BlockState oldState,
            boolean notify,
            CallbackInfo ci,
            Optional<PortalShape> optional) {

        if (!(world instanceof ServerLevel serverWorld)) {
            return;
        }

        if (serverWorld.dimension() != Level.OVERWORLD) {
            return;
        }

        if (optional == null || optional.isEmpty()) {
            return;
        }

        int cryingObsidianCount = PortalScanner.countCryingObsidianInFrame(serverWorld, pos);

        if (cryingObsidianCount > 0) {
            FesteringPortal.onFesteringPortalCreated(serverWorld, pos, cryingObsidianCount);
        }
    }
}
