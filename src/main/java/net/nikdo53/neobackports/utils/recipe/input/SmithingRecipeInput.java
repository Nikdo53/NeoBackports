package net.nikdo53.neobackports.utils.recipe.input;


import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public record SmithingRecipeInput(ItemStack template, ItemStack base, ItemStack addition) implements RecipeInput {
    public static SmithingRecipeInput fromContainer(Container container){
        return new SmithingRecipeInput(container.getItem(0), container.getItem(1), container.getItem(2));
    }

    @Override
    public ItemStack getItem(int p_346205_) {
        return switch (p_346205_) {
            case 0 -> this.template;
            case 1 -> this.base;
            case 2 -> this.addition;
            default -> throw new IllegalArgumentException("Recipe does not contain slot " + p_346205_);
        };
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public boolean isEmpty() {
        return this.template.isEmpty() && this.base.isEmpty() && this.addition.isEmpty();
    }
}
