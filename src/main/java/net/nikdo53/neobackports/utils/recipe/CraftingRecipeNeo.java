package net.nikdo53.neobackports.utils.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.nikdo53.neobackports.utils.recipe.input.CraftingInput;

public interface CraftingRecipeNeo extends CraftingRecipe {

    @Override
    default boolean matches(CraftingContainer container, Level level) {
        return matches(CraftingInput.fromContainer(container), level);
    }

    @Override
    default ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        return assemble(CraftingInput.fromContainer(container), registryAccess);
    }


    @Override
    default ItemStack getResultItem(RegistryAccess registryAccess) {
        return getResultItem((HolderLookup.Provider) registryAccess);
    }

    boolean matches(CraftingInput input, Level level);

    ItemStack assemble(CraftingInput input, HolderLookup.Provider registries);

    ItemStack getResultItem(HolderLookup.Provider registries);

}
