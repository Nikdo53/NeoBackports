package net.nikdo53.neobackports.utils.recipe.input;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public record SingleRecipeInput(ItemStack item) implements RecipeInput {
    public static SingleRecipeInput fromContainer(Container container){
        return new SingleRecipeInput(container.getItem(0));
    }
    @Override
    public ItemStack getItem(int p_345528_) {
        if (p_345528_ != 0) {
            throw new IllegalArgumentException("No item for index " + p_345528_);
        } else {
            return this.item;
        }
    }

    @Override
    public int size() {
        return 1;
    }
}
