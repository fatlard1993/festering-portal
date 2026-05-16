package com.festeringportal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Mixin to allow Crying Obsidian as a valid portal frame block alongside regular Obsidian.
 * Wraps IS_VALID_FRAME_BLOCK predicate checks in all frame validation methods.
 */
@Mixin(PortalShape.class)
public class NetherPortalMixin {

    @WrapOperation(
        method = "getDistanceUntilEdgeAboveFrame(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)I",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$StatePredicate;test(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z"
        )
    )
    private static boolean wrapFrameCheckInGetWidth(
            BlockBehaviour.StatePredicate predicate,
            BlockState state,
            BlockGetter world,
            BlockPos pos,
            Operation<Boolean> original) {
        return original.call(predicate, state, world, pos) || state.is(Blocks.CRYING_OBSIDIAN);
    }

    @WrapOperation(
        method = "hasTopFrame(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/core/BlockPos$MutableBlockPos;II)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$StatePredicate;test(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z"
        )
    )
    private static boolean wrapFrameCheckInHorizontalValid(
            BlockBehaviour.StatePredicate predicate,
            BlockState state,
            BlockGetter world,
            BlockPos pos,
            Operation<Boolean> original) {
        return original.call(predicate, state, world, pos) || state.is(Blocks.CRYING_OBSIDIAN);
    }

    @WrapOperation(
        method = "getDistanceUntilTop(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/core/BlockPos$MutableBlockPos;ILorg/apache/commons/lang3/mutable/MutableInt;)I",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$StatePredicate;test(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z"
        )
    )
    private static boolean wrapFrameCheckInGetHeight(
            BlockBehaviour.StatePredicate predicate,
            BlockState state,
            BlockGetter world,
            BlockPos pos,
            Operation<Boolean> original) {
        return original.call(predicate, state, world, pos) || state.is(Blocks.CRYING_OBSIDIAN);
    }
}
