package net.nikdo53.neobackports.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;

public class DeferredItem<T extends Item> extends DeferredHolder<Item, T> implements ItemLike {
    public DeferredItem(RegistryObject<T> registryObject) {
        super(registryObject);
    }

    /**
     * Creates a new {@link ItemStack} with a default size of 1 from this {@link Item}
     */
    public ItemStack toStack() {
        return toStack(1);
    }

    /**
     * Creates a new {@link ItemStack} with the given size from this {@link Item}
     *
     * @param count The size of the stack to create
     */
    public ItemStack toStack(int count) {
        ItemStack stack = asItem().getDefaultInstance();
        if (stack.isEmpty()) throw new IllegalStateException("Obtained empty item stack; incorrect getDefaultInstance() call?");
        stack.setCount(count);
        return stack;
    }

    @Override
    public Item asItem() {
        return get();
    }
}
