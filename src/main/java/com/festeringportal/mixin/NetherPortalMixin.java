package com.festeringportal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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

/**
 * Mixin to allow Crying Obsidian as a valid portal frame block alongside regular Obsidian.
 * Wraps IS_VALID_FRAME_BLOCK predicate checks in all frame validation methods.
 */
@Mixin(NetherPortal.class)
public class NetherPortalMixin {

    @WrapOperation(
        method = "getWidth(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)I",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/AbstractBlock$ContextPredicate;test(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"
        )
    )
    private static boolean wrapFrameCheckInGetWidth(
            AbstractBlock.ContextPredicate predicate,
            BlockState state,
            BlockView world,
            BlockPos pos,
            Operation<Boolean> original) {
        return original.call(predicate, state, world, pos) || state.isOf(Blocks.CRYING_OBSIDIAN);
    }

    @WrapOperation(
        method = "isHorizontalFrameValid(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/util/math/BlockPos$Mutable;II)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/AbstractBlock$ContextPredicate;test(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"
        )
    )
    private static boolean wrapFrameCheckInHorizontalValid(
            AbstractBlock.ContextPredicate predicate,
            BlockState state,
            BlockView world,
            BlockPos pos,
            Operation<Boolean> original) {
        return original.call(predicate, state, world, pos) || state.isOf(Blocks.CRYING_OBSIDIAN);
    }

    @WrapOperation(
        method = "getPotentialHeight(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/util/math/BlockPos$Mutable;ILorg/apache/commons/lang3/mutable/MutableInt;)I",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/AbstractBlock$ContextPredicate;test(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"
        )
    )
    private static boolean wrapFrameCheckInGetHeight(
            AbstractBlock.ContextPredicate predicate,
            BlockState state,
            BlockView world,
            BlockPos pos,
            Operation<Boolean> original) {
        return original.call(predicate, state, world, pos) || state.isOf(Blocks.CRYING_OBSIDIAN);
    }
}
