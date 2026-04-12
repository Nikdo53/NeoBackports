package net.nikdo53.neobackports.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.nikdo53.neobackports.extensions.IBlockBehaviourExtension;
import net.nikdo53.neobackports.extensions.IBlockStateExtension;
import net.nikdo53.neobackports.utils.ItemInteractionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin implements IBlockStateExtension {

    @Shadow
    public abstract Block getBlock();

    @Shadow
    protected abstract BlockState asState();

    @Shadow
    public abstract boolean is(Block block);

    @WrapMethod(method = "use")
    public InteractionResult use(Level level, Player player, InteractionHand hand, BlockHitResult result, Operation<InteractionResult> original) {
        IBlockBehaviourExtension extension = (IBlockBehaviourExtension) getBlock();

        ItemInteractionResult itemInteractionResult = extension.useItemOn(player.getItemInHand(hand), asState(), level, result.getBlockPos(), player, hand, result);

        if (itemInteractionResult.consumesAction()) {
            return itemInteractionResult.result();
        }

        if (itemInteractionResult == ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION && hand == InteractionHand.MAIN_HAND) {
            InteractionResult interactionresult = extension.useWithoutItem(asState(), level, result.getBlockPos(), player, result);
            if (interactionresult == InteractionResult.PASS) {
                return original.call(level, player, hand, result);
            } else {
                return interactionresult;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean is(Supplier<Block> supplier) {
        return is(supplier.get());
    }

}

