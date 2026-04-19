package net.nikdo53.neobackports.utils.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;
import net.nikdo53.neobackports.utils.recipe.input.CraftingInput;
import net.nikdo53.neobackports.utils.recipe.input.SmithingRecipeInput;

import java.util.Objects;

public interface SmithingRecipeNeo extends SmithingRecipe {
    @Override
    default ResourceLocation getId(){
        return RecipeIdHolder.getId(this);
    }

    @Override
    default boolean matches(Container container, Level level) {
        return matches(SmithingRecipeInput.fromContainer(container), level);
    }

    @Override
    default ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return assemble(SmithingRecipeInput.fromContainer(container), registryAccess);
    }


    @Override
    default ItemStack getResultItem(RegistryAccess registryAccess) {
        return getResultItem((HolderLookup.Provider) registryAccess);
    }

    boolean matches(SmithingRecipeInput input, Level level);

    ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries);

    ItemStack getResultItem(HolderLookup.Provider registries);

}
