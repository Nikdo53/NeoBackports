package net.nikdo53.neobackports.utils.recipe.holder;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.nikdo53.neobackports.utils.recipe.SmithingRecipeNeo;
import net.nikdo53.neobackports.utils.recipe.input.SmithingRecipeInput;

public class SmithingRecipeHolder extends RecipeHolder<Container, SmithingRecipeNeo> implements SmithingRecipeNeo {
    public SmithingRecipeHolder(Recipe<?> recipe, ResourceLocation id) {
        super(recipe, id);
    }

    @Override
    public boolean matches(SmithingRecipeInput input, Level level) {
        return getRecipe().matches(input, level);
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries) {
        return getRecipe().assemble(input, registries);
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return getRecipe().getResultItem(registries);
    }

    @Override
    public boolean isTemplateIngredient(ItemStack stack) {
        return getRecipe().isTemplateIngredient(stack);
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return getRecipe().isBaseIngredient(stack);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return getRecipe().isAdditionIngredient(stack);
    }
}
