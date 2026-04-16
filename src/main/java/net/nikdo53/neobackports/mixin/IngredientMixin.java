package net.nikdo53.neobackports.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.nikdo53.neobackports.extensions.IngredientExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Ingredient.class)
public abstract class IngredientMixin implements IngredientExtension {
    @Shadow
    public abstract ItemStack[] getItems();

    @Override
    public boolean hasNoItems() {
        ItemStack[] items = getItems();
        if (items.length == 0)
            return true;
        if (items.length == 1) {
            // If we potentially added a barrier due to the ingredient being an empty tag, try and check if it is the stack we added
            ItemStack item = items[0];
            return item.getItem() == net.minecraft.world.item.Items.BARRIER && item.getHoverName() instanceof net.minecraft.network.chat.MutableComponent hoverName && hoverName.getString().startsWith("Empty Tag: ");
        }
        return false;
    }
}
