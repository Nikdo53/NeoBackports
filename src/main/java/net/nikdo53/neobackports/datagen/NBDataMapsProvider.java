package net.nikdo53.neobackports.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.model.CompositeModel;
import net.nikdo53.neobackports.test.NBDataMaps;

import java.util.concurrent.CompletableFuture;

public class NBDataMapsProvider extends DataMapProvider {

    protected NBDataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        super.gather(provider);

        var map = this.builder(NBDataMaps.TEST_DATA_MAP);
        map.add(ItemTags.AXES, 2, false);
        map.add(Items.LAPIS_LAZULI.builtInRegistryHolder(), 10, false);

    }
}
