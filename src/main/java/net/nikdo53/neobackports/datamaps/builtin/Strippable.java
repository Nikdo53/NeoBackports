package net.nikdo53.neobackports.datamaps.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.nikdo53.neobackports.io.utils.BackportCodecs;

public record Strippable(Block strippedBlock) {
    public static final Codec<Strippable> STRIPPED_BLOCK_CODEC = BuiltInRegistries.BLOCK.byNameCodec()
            .xmap(Strippable::new, Strippable::strippedBlock);

    public static final Codec<Strippable> CODEC = BackportCodecs.withAlternative(
            RecordCodecBuilder.create(inst -> inst.group(
                            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("stripped_block")
                                    .forGetter(Strippable::strippedBlock))
                    .apply(inst, Strippable::new)),
            STRIPPED_BLOCK_CODEC);
}