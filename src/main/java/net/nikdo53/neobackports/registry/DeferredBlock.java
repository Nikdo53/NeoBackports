package net.nikdo53.neobackports.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DeferredBlock <T extends Block> extends DeferredHolder<Block, T> implements ItemLike {
    public DeferredBlock(RegistryObject<T> registryObject) {
        super(registryObject);
    }
    /**
     * Creates a new {@link ItemStack} with a default size of 1 from this {@link Block}
     */
    public ItemStack toStack() {
        return toStack(1);
    }

    /**
     * Creates a new {@link ItemStack} with the given size from this {@link Block}
     *
     * @param count The size of the stack to create
     */
    public ItemStack toStack(int count) {
        ItemStack stack = asItem().getDefaultInstance();
        if (stack.isEmpty()) throw new IllegalStateException("Block does not have a corresponding item: " + getKey());
        stack.setCount(count);
        return stack;
    }

    public static <T extends Block> DeferredBlock<T> createBlock(ResourceLocation key) {
        return createBlock(ResourceKey.create(Registries.BLOCK, key));
    }

    /**
     * Creates a new {@link DeferredHolder} targeting the specified {@link Block}.
     *
     * @param <T> The type of the target {@link Block}.
     * @param key The resource key of the target {@link Block}.
     */
    public static <T extends Block> DeferredBlock<T> createBlock(ResourceKey<Block> key) {
        return new DeferredBlock<>(RegistryObject.create(key.location(), ForgeRegistries.BLOCKS));
    }

    @Override
    public Item asItem() {
        return get().asItem();
    }
}
