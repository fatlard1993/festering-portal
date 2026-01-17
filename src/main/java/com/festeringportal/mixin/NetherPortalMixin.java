package com.festeringportal.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.dimension.NetherPortal;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin to allow Crying Obsidian as a valid portal frame block alongside regular Obsidian.
 * Redirects IS_VALID_FRAME_BLOCK predicate checks in all frame validation methods.
 */
@Mixin(NetherPortal.class)
public class NetherPortalMixin {

    /**
     * Redirect the frame check in getWidth method to accept crying obsidian.
     */
    @Redirect(
        method = "getWidth(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)I",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/AbstractBlock$ContextPredicate;test(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"
        )
    )
    private static boolean redirectFrameCheckInGetWidth(
            AbstractBlock.ContextPredicate predicate,
            BlockState state,
            BlockView world,
            BlockPos pos) {
        return isValidFrameBlock(state);
    }

    /**
     * Redirect the frame check in isHorizontalFrameValid method (top frame check).
     */
    @Redirect(
        method = "isHorizontalFrameValid(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/util/math/BlockPos$Mutable;II)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/AbstractBlock$ContextPredicate;test(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"
        )
    )
    private static boolean redirectFrameCheckInHorizontalValid(
            AbstractBlock.ContextPredicate predicate,
            BlockState state,
            BlockView world,
            BlockPos pos) {
        return isValidFrameBlock(state);
    }

    /**
     * Redirect the frame check in getPotentialHeight method (side frames).
     */
    @Redirect(
        method = "getPotentialHeight(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/util/math/BlockPos$Mutable;ILorg/apache/commons/lang3/mutable/MutableInt;)I",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/AbstractBlock$ContextPredicate;test(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"
        )
    )
    private static boolean redirectFrameCheckInGetHeight(
            AbstractBlock.ContextPredicate predicate,
            BlockState state,
            BlockView world,
            BlockPos pos) {
        return isValidFrameBlock(state);
    }

    /**
     * Check if a block is valid for a portal frame.
     * Accepts both regular Obsidian and Crying Obsidian.
     */
    private static boolean isValidFrameBlock(BlockState state) {
        return state.isOf(Blocks.OBSIDIAN) || state.isOf(Blocks.CRYING_OBSIDIAN);
    }
}
