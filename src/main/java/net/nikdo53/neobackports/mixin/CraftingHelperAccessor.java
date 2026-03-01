package net.nikdo53.neobackports.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(CraftingHelper.class)
public interface CraftingHelperAccessor {

    @Accessor(value = "conditions", remap = false)
    static Map<ResourceLocation, IConditionSerializer<?>> getConditions() {
        throw new AssertionError();
    }

}
