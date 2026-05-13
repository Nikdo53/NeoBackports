package net.nikdo53.neobackports.utils.recipe.holder;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.nikdo53.neobackports.utils.recipe.CraftingRecipeNeo;
import net.nikdo53.neobackports.utils.recipe.input.CraftingInput;

public class CraftingRecipeHolder extends RecipeHolder<CraftingContainer, CraftingRecipeNeo> implements CraftingRecipeNeo{
    public CraftingRecipeHolder(Recipe<?> recipe, ResourceLocation id) {
        super(recipe, id);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return getRecipe().matches(input, level);
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return getRecipe().assemble(input, registries);
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return getRecipe().getResultItem(registries);
    }

    @Override
    public CraftingBookCategory category() {
        return getRecipe().category();
    }
}
