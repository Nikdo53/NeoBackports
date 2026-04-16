package net.nikdo53.neobackports.mixin.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.nikdo53.neobackports.extensions.ItemModelProviderExtension;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Objects;
import java.util.function.Function;

@Mixin(ItemModelProvider.class)
public abstract class ItemModelProviderMixin extends ModelProvider<ItemModelBuilder> implements ItemModelProviderExtension {
    public ItemModelProviderMixin(PackOutput output, String modid, String folder, Function<ResourceLocation, ItemModelBuilder> factory, ExistingFileHelper existingFileHelper) {
        super(output, modid, folder, factory, existingFileHelper);
    }

    @Override
    public ItemModelBuilder handheldItem(Item item) {
        return handheldItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
    }

    @Override
    public ItemModelBuilder handheldItem(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()));
    }

    @Override
    public ItemModelBuilder spawnEggItem(Item item) {
        return spawnEggItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
    }

    @Override
    public ItemModelBuilder spawnEggItem(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
    }

    @Override
    public ItemModelBuilder simpleBlockItem(Block block) {
        return simpleBlockItem(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)));
    }

    @Override
    public ItemModelBuilder simpleBlockItem(ResourceLocation block) {
        return withExistingParent(block.toString(), new ResourceLocation(block.getNamespace(), "block/" + block.getPath()));
    }
}
