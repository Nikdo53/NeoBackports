package net.nikdo53.neobackports.utils.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;
import net.nikdo53.neobackports.utils.recipe.input.SingleRecipeInput;

import java.util.Objects;

public abstract class SingleItemRecipeNeo extends SingleItemRecipe {
    public SingleItemRecipeNeo(RecipeType<?> type, RecipeSerializer<?> serializer, ResourceLocation id, String group, Ingredient ingredient, ItemStack result) {
        super(type, serializer, id, group, ingredient, result);
    }

    @Override
    public boolean matches(Container container, Level level) {
        return matches(SingleRecipeInput.fromContainer(container), level);
    }

    abstract boolean matches(SingleRecipeInput input, Level level);

    @Override
    public ResourceLocation getId(){
        return Objects.requireNonNull(RecipeIdHolder.getReversed().get(this));
    }

}
