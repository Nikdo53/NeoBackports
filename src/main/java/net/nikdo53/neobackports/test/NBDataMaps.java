package net.nikdo53.neobackports.test;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.datamaps.DataMapType;

public interface NBDataMaps {
    DataMapType<Item, Integer> TEST_DATA_MAP = DataMapType.builder(NeoBackports.loc("test"), Registries.ITEM, Codec.INT).synced(Codec.INT, true).build();

}
