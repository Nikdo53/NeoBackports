package net.nikdo53.neobackports.registry;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredRegisterTyped {
    /**
     * Factory for a specialized {@link DeferredRegister} for {@link Item Items}.
     *
     * @param modid The namespace for all objects registered to this {@link DeferredRegister}
     * @see #createBlocks(String)
     */
    static DeferredRegisterTyped.Items createItems(String modid) {
        return new Items(modid);
    }

    /**
     * Factory for a specialized DeferredRegister for {@link Block Blocks}.
     *
     * @param modid The namespace for all objects registered to this DeferredRegister
     * @see #createItems(String)
     */
    static DeferredRegisterTyped.Blocks createBlocks(String modid) {
        return new Blocks(modid);
    }

    public static class Items extends DeferredRegisterWrapper<Item> {
        public Items(String modid) {
            super(DeferredRegister.create(Registries.ITEM, modid));
        }

        /**
         * Adds a new item to the list of entries to be registered and returns a {@link DeferredItem} that will be populated with the created item automatically.
         *
         * @param name The new item's name. It will automatically have the mod id prefixed.
         * @param func A factory for the new item. The factory should not cache the created item.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #register(String, Supplier)
         */
        @SuppressWarnings("unchecked")
        @Override
        public <I extends Item> DeferredItem<I> register(String name, Function<ResourceLocation, ? extends I> func) {
            return (DeferredItem<I>) super.register(name, func);
        }

        /**
         * Adds a new item to the list of entries to be registered and returns a {@link DeferredItem} that will be populated with the created item automatically.
         *
         * @param name The new item's name. It will automatically have the mod id prefixed.
         * @param sup  A factory for the new item. The factory should not cache the created item.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #register(String, Function)
         */
        @Override
        public <I extends Item> DeferredItem<I> register(String name, Supplier<? extends I> sup) {
            return this.register(name, key -> sup.get());
        }

        /**
         * Adds a new simple {@link BlockItem} for the given {@link Block} to the list of entries to be registered and
         * returns a {@link DeferredItem} that will be populated with the created item automatically.
         *
         * @param name       The new item's name. It will automatically have the mod id prefixed.
         * @param block      The supplier for the block to create a {@link BlockItem} for.
         * @param properties The properties for the created {@link BlockItem}.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #registerSimpleBlockItem(String, Supplier)
         * @see #registerSimpleBlockItem(Holder, Item.Properties)
         * @see #registerSimpleBlockItem(Holder)
         */
        public DeferredItem<BlockItem> registerSimpleBlockItem(String name, Supplier<? extends Block> block, Item.Properties properties) {
            return this.register(name, key -> new BlockItem(block.get(), properties));
        }

        /**
         * Adds a new simple {@link BlockItem} for the given {@link Block} to the list of entries to be registered and
         * returns a {@link DeferredItem} that will be populated with the created item automatically.
         * This method uses the default {@link Item.Properties}.
         *
         * @param name  The new item's name. It will automatically have the mod id prefixed.
         * @param block The supplier for the block to create a {@link BlockItem} for.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #registerSimpleBlockItem(String, Supplier, Item.Properties)
         * @see #registerSimpleBlockItem(Holder, Item.Properties)
         * @see #registerSimpleBlockItem(Holder)
         */
        public DeferredItem<BlockItem> registerSimpleBlockItem(String name, Supplier<? extends Block> block) {
            return this.registerSimpleBlockItem(name, block, new Item.Properties());
        }

        /**
         * Adds a new simple {@link BlockItem} for the given {@link Block} to the list of entries to be registered and
         * returns a {@link DeferredItem} that will be populated with the created item automatically.
         * Where the name is determined by the name of the given block.
         *
         * @param block      The {@link DeferredHolder} of the {@link Block} for the {@link BlockItem}.
         * @param properties The properties for the created {@link BlockItem}.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #registerSimpleBlockItem(String, Supplier, Item.Properties)
         * @see #registerSimpleBlockItem(String, Supplier)
         * @see #registerSimpleBlockItem(Holder)
         */
        public DeferredItem<BlockItem> registerSimpleBlockItem(Holder<Block> block, Item.Properties properties) {
            return this.registerSimpleBlockItem(block.unwrapKey().orElseThrow().location().getPath(), block::value, properties);
        }

        /**
         * Adds a new simple {@link BlockItem} for the given {@link Block} to the list of entries to be registered and
         * returns a {@link DeferredItem} that will be populated with the created item automatically.
         * Where the name is determined by the name of the given block and uses the default {@link Item.Properties}.
         *
         * @param block The {@link DeferredHolder} of the {@link Block} for the {@link BlockItem}.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #registerSimpleBlockItem(String, Supplier, Item.Properties)
         * @see #registerSimpleBlockItem(String, Supplier)
         * @see #registerSimpleBlockItem(Holder, Item.Properties)
         */
        public DeferredItem<BlockItem> registerSimpleBlockItem(Holder<Block> block) {
            return this.registerSimpleBlockItem(block, new Item.Properties());
        }

        /**
         * Adds a new item to the list of entries to be registered and returns a {@link DeferredItem} that will be populated with the created item automatically.
         *
         * @param name  The new item's name. It will automatically have the mod id prefixed.
         * @param func  A factory for the new item. The factory should not cache the created item.
         * @param props The properties for the created item.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #registerItem(String, Function)
         * @see #registerSimpleItem(String, Item.Properties)
         * @see #registerSimpleItem(String)
         */
        public <I extends Item> DeferredItem<I> registerItem(String name, Function<Item.Properties, ? extends I> func, Item.Properties props) {
            return this.register(name, () -> func.apply(props));
        }

        /**
         * Adds a new item to the list of entries to be registered and returns a {@link DeferredItem} that will be populated with the created item automatically.
         * This method uses the default {@link Item.Properties}.
         *
         * @param name The new item's name. It will automatically have the mod id prefixed.
         * @param func A factory for the new item. The factory should not cache the created item.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #registerItem(String, Function, Item.Properties)
         * @see #registerSimpleItem(String, Item.Properties)
         * @see #registerSimpleItem(String)
         */
        public <I extends Item> DeferredItem<I> registerItem(String name, Function<Item.Properties, ? extends I> func) {
            return this.registerItem(name, func, new Item.Properties());
        }

        /**
         * Adds a new simple {@link Item} with the given {@link Item.Properties properties} to the list of entries to be registered and
         * returns a {@link DeferredItem} that will be populated with the created item automatically.
         *
         * @param name  The new item's name. It will automatically have the mod id prefixed.
         * @param props A factory for the new item. The factory should not cache the created item.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #registerItem(String, Function, Item.Properties)
         * @see #registerItem(String, Function)
         * @see #registerSimpleItem(String)
         */
        public DeferredItem<Item> registerSimpleItem(String name, Item.Properties props) {
            return this.registerItem(name, Item::new, props);
        }

        /**
         * Adds a new simple {@link Item} with the default {@link Item.Properties properties} to the list of entries to be registered and
         * returns a {@link DeferredItem} that will be populated with the created item automatically.
         *
         * @param name The new item's name. It will automatically have the mod id prefixed.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #registerItem(String, Function, Item.Properties)
         * @see #registerItem(String, Function)
         * @see #registerSimpleItem(String, Item.Properties)
         */
        public DeferredItem<Item> registerSimpleItem(String name) {
            return this.registerItem(name, Item::new, new Item.Properties());
        }

        /**
         * Create a {@link DeferredHolder} or an inheriting type to be stored.
         * @return The new instance of {@link DeferredHolder} or an inheriting type.
         */
        @Override
        protected <I extends Item> DeferredItem<I> createHolder(RegistryObject<I> registryObject) {
            return new DeferredItem<>(registryObject);
        }
    }

    public static class Blocks extends DeferredRegisterWrapper<Block> {
        public Blocks(String modid) {
            super(DeferredRegister.create(Registries.BLOCK, modid));
        }

        /**
         * Adds a new block to the list of entries to be registered and returns a {@link DeferredHolder} that will be populated with the created block automatically.
         *
         * @param name The new block's name. It will automatically have the mod id prefixed.
         * @param func A factory for the new block. The factory should not cache the created block.
         * @return A {@link DeferredHolder} that will track updates from the registry for this block.
         */
        @SuppressWarnings("unchecked")
        @Override
        public <B extends Block> DeferredBlock<B> register(String name, Function<ResourceLocation, ? extends B> func) {
            return (DeferredBlock<B>) super.register(name, func);
        }

        /**
         * Adds a new block to the list of entries to be registered and returns a {@link DeferredHolder} that will be populated with the created block automatically.
         *
         * @param name The new block's name. It will automatically have the mod id prefixed.
         * @param sup  A factory for the new block. The factory should not cache the created block.
         * @return A {@link DeferredHolder} that will track updates from the registry for this block.
         */
        @Override
        public <B extends Block> DeferredBlock<B> register(String name, Supplier<? extends B> sup) {
            return this.register(name, key -> sup.get());
        }

        /**
         * Adds a new block to the list of entries to be registered and returns a {@link DeferredHolder} that will be populated with the created block automatically.
         *
         * @param name  The new block's name. It will automatically have the mod id prefixed.
         * @param func  A factory for the new block. The factory should not cache the created block.
         * @param props The properties for the created block.
         * @return A {@link DeferredHolder} that will track updates from the registry for this block.
         * @see #registerBlock(String, Function)
         * @see #registerSimpleBlock(String, BlockBehaviour.Properties)
         * @see #registerSimpleBlock(String)
         */
        public <B extends Block> DeferredBlock<B> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> func, BlockBehaviour.Properties props) {
            return this.register(name, () -> func.apply(props));
        }

        /**
         * Adds a new block to the list of entries to be registered and returns a {@link DeferredHolder} that will be populated with the created block automatically.
         * This method uses the default {@link BlockBehaviour.Properties}.
         *
         * @param name The new block's name. It will automatically have the mod id prefixed.
         * @param func A factory for the new block. The factory should not cache the created block.
         * @return A {@link DeferredHolder} that will track updates from the registry for this block.
         * @see #registerBlock(String, Function, BlockBehaviour.Properties)
         * @see #registerSimpleBlock(String, BlockBehaviour.Properties)
         * @see #registerSimpleBlock(String)
         */
        public <B extends Block> DeferredBlock<B> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> func) {
            return this.registerBlock(name, func, BlockBehaviour.Properties.of());
        }

        /**
         * Adds a new simple {@link Block} with the given {@link BlockBehaviour.Properties properties} to the list of entries to be registered and returns a {@link DeferredHolder} that will be populated with the created block automatically.
         *
         * @param name  The new block's name. It will automatically have the mod id prefixed.
         * @param props The properties for the created block.
         * @return A {@link DeferredHolder} that will track updates from the registry for this block.
         * @see #registerBlock(String, Function, BlockBehaviour.Properties)
         * @see #registerBlock(String, Function)
         * @see #registerSimpleBlock(String)
         */
        public DeferredBlock<Block> registerSimpleBlock(String name, BlockBehaviour.Properties props) {
            return this.registerBlock(name, Block::new, props);
        }

        /**
         * Adds a new simple {@link Block} with the default {@link BlockBehaviour.Properties properties} to the list of entries to be registered and returns a {@link DeferredHolder} that will be populated with the created block automatically.
         *
         * @param name The new block's name. It will automatically have the mod id prefixed.
         * @return A {@link DeferredHolder} that will track updates from the registry for this block.
         * @see #registerBlock(String, Function, BlockBehaviour.Properties)
         * @see #registerBlock(String, Function)
         * @see #registerSimpleBlock(String, BlockBehaviour.Properties)
         */
        public DeferredBlock<Block> registerSimpleBlock(String name) {
            return this.registerSimpleBlock(name, BlockBehaviour.Properties.of());
        }

        /**
         * Create a {@link DeferredHolder} or an inheriting type to be stored.
         * @return The new instance of {@link DeferredHolder} or an inheriting type.
         */
        @Override
        protected <I extends Block> DeferredBlock<I> createHolder(RegistryObject<I> registryObject) {
            return new DeferredBlock<>(registryObject);
        }
    }
}
