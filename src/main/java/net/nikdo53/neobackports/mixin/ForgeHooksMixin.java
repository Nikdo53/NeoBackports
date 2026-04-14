package net.nikdo53.neobackports.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.nikdo53.neobackports.datamaps.NeoForgeDataMaps;
import net.nikdo53.neobackports.datamaps.builtin.FurnaceFuel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ForgeHooks.class, remap = false)
public class ForgeHooksMixin {

    @ModifyExpressionValue(method = "getBurnTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getBurnTime(Lnet/minecraft/world/item/crafting/RecipeType;)I"), remap = false)
    private static int getBurnTime(int original, @Local(argsOnly = true) ItemStack stack){
        if (original > -1){
            return original;
        }
        FurnaceFuel fuel = stack.getItemHolder().getData(NeoForgeDataMaps.FURNACE_FUELS);
        if (fuel != null){
            return fuel.burnTime();
        }
        return original;
    }


}
