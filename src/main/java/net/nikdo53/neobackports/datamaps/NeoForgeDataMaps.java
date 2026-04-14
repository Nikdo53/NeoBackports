package net.nikdo53.neobackports.datamaps;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.nikdo53.neobackports.datamaps.builtin.Compostable;
import net.nikdo53.neobackports.datamaps.builtin.FurnaceFuel;
import org.jetbrains.annotations.Nullable;

public class NeoForgeDataMaps {
    public static @Nullable ImmutableMap<ItemLike, Float> ORIGINAL_COMPOSTABLES = null;

    public static final DataMapType<Item, Compostable> COMPOSTABLES = DataMapType.builder(
            id("compostables"), Registries.ITEM, Compostable.CODEC).synced(Compostable.CHANCE_CODEC, false).build();

    public static final DataMapType<Item, FurnaceFuel> FURNACE_FUELS = DataMapType.builder(
            id("furnace_fuels"), Registries.ITEM, FurnaceFuel.CODEC).synced(FurnaceFuel.BURN_TIME_CODEC, false).build();


    private static ResourceLocation id(final String name) {
        return new ResourceLocation("neoforge", name);
    }
}
