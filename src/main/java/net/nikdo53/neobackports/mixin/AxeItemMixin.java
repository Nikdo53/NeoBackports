package net.nikdo53.neobackports.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.nikdo53.neobackports.datamaps.NeoForgeDataMaps;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @Nullable
    @WrapMethod(method = "getAxeStrippingState", remap = false)
    private static BlockState getAxeStrippingState(BlockState originalState, Operation<BlockState> original) {
        var strippable = originalState.getData(NeoForgeDataMaps.STRIPPABLES);
        if (strippable != null) return strippable.strippedBlock().withPropertiesOf(originalState);
        return original.call(originalState);
    }

}
