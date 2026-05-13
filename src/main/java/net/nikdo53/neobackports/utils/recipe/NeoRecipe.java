package net.nikdo53.neobackports.utils.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.crafting.Recipe;

public interface NeoRecipe {
    static ResourceLocation onInvalidId(Recipe<?> recipe){
        throw new IllegalStateException("Tried getting recipe id for a NeoRecipe: " + recipe + " which wasn't wrapped by RecipeHolder");
    }
}
