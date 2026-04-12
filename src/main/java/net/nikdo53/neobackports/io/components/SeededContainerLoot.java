package net.nikdo53.neobackports.io.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

public record SeededContainerLoot(ResourceLocation lootTable, long seed) {
    public static final Codec<SeededContainerLoot> CODEC = RecordCodecBuilder.create(
            (p_337951_) -> p_337951_.group(
                    ResourceLocation.CODEC.fieldOf("loot_table").forGetter(SeededContainerLoot::lootTable),
                    Codec.LONG.optionalFieldOf("seed", 0L).forGetter(SeededContainerLoot::seed)
            ).apply(p_337951_, SeededContainerLoot::new));
}
