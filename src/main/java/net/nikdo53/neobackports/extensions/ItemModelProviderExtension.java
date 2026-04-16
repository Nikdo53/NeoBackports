package net.nikdo53.neobackports.extensions;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.Objects;

public interface ItemModelProviderExtension {

    default ItemModelBuilder handheldItem(Item item) {
        throw new IllegalStateException("Not implemented");
    }

    default ItemModelBuilder handheldItem(ResourceLocation item) {
        throw new IllegalStateException("Not implemented");
    }

    default ItemModelBuilder spawnEggItem(Item item) {
        throw new IllegalStateException("Not implemented");
    }

    default ItemModelBuilder spawnEggItem(ResourceLocation item) {
        throw new IllegalStateException("Not implemented");
    }

    default ItemModelBuilder simpleBlockItem(Block block) {
        throw new IllegalStateException("Not implemented");
    }

    default ItemModelBuilder simpleBlockItem(ResourceLocation block) {
        throw new IllegalStateException("Not implemented");
    }

}
