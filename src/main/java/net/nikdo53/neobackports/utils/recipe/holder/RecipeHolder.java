package net.nikdo53.neobackports.utils.recipe.holder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RecipeHolder<C extends Container, R extends Recipe<C>> implements Recipe<C>{
    public final R recipe;
    public final ResourceLocation id;

    @SuppressWarnings("unchecked")
    public RecipeHolder(Recipe<?> recipe, ResourceLocation id) {
        this.recipe = (R) recipe;
        this.id = id;
    }

    public R getRecipe() {
        return recipe;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public boolean matches(C container, Level level){
        return getRecipe().matches(container, level);
    }

    @Override
    public ItemStack assemble(C container, RegistryAccess registryAccess){
        return getRecipe().assemble(container, registryAccess);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height){
        return getRecipe().canCraftInDimensions(width, height);
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess){
        return getRecipe().getResultItem(registryAccess);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(C container) {
        return getRecipe().getRemainingItems(container);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return getRecipe().getIngredients();
    }

    @Override
    public boolean isSpecial() {
        return getRecipe().isSpecial();
    }

    @Override
    public boolean showNotification() {
        return getRecipe().showNotification();
    }

    @Override
    public String getGroup() {
        return getRecipe().getGroup();
    }

    @Override
    public ItemStack getToastSymbol() {
        return getRecipe().getToastSymbol();
    }

    @Override
    public RecipeSerializer<?> getSerializer(){
        return getRecipe().getSerializer();
    }

    @Override
    public RecipeType<?> getType(){
        return getRecipe().getType();
    }

    @Override
    public boolean isIncomplete() {
        return getRecipe().isIncomplete();
    }
}
