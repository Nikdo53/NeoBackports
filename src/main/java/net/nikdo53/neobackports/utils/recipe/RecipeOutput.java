package net.nikdo53.neobackports.utils.recipe;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

@FunctionalInterface
public interface RecipeOutput extends Consumer<FinishedRecipe> {
}
