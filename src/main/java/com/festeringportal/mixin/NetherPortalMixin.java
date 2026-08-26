package com.festeringportal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.function.Predicate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Crying obsidian counts as portal frame alongside obsidian.
 *
 * <p>PortalShape.FRAME is a {@code Predicate<BlockState>}, tested at every frame
 * position; each wrap below covers every test call in one method, so a method
 * that checks twice needs no second injection.
 */
@Mixin(PortalShape.class)
public class NetherPortalMixin {

    @WrapOperation(
        method = "getDistanceUntilEdgeAboveFrame(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)I",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"
        )
    )
    private static boolean wrapFrameCheckInGetWidth(
            Predicate<BlockState> predicate,
            Object state,
            Operation<Boolean> original) {
        return isFrame(predicate, state, original);
    }

    @WrapOperation(
        method = "hasTopFrame(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/core/BlockPos$MutableBlockPos;II)Z",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"
        )
    )
    private static boolean wrapFrameCheckInHorizontalValid(
            Predicate<BlockState> predicate,
            Object state,
            Operation<Boolean> original) {
        return isFrame(predicate, state, original);
    }

    @WrapOperation(
        method = "getDistanceUntilTop(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/core/BlockPos$MutableBlockPos;ILorg/apache/commons/lang3/mutable/MutableInt;)I",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"
        )
    )
    private static boolean wrapFrameCheckInGetHeight(
            Predicate<BlockState> predicate,
            Object state,
            Operation<Boolean> original) {
        return isFrame(predicate, state, original);
    }

    private static boolean isFrame(
            Predicate<BlockState> predicate, Object state, Operation<Boolean> original) {
        if (original.call(predicate, state)) return true;
        return state instanceof BlockState blockState && blockState.is(Blocks.CRYING_OBSIDIAN);
    }
}
