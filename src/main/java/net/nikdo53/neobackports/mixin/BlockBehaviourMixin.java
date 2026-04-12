package net.nikdo53.neobackports.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.nikdo53.neobackports.extensions.IBlockBehaviourExtension;
import net.nikdo53.neobackports.utils.ItemInteractionResult;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin implements IBlockBehaviourExtension {
    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return IBlockBehaviourExtension.super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return IBlockBehaviourExtension.super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

}
