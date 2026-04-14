package net.nikdo53.neobackports.datamaps.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.nikdo53.neobackports.io.utils.NeoForgeExtraCodecs;

public record FurnaceFuel(int burnTime) {
    public static final Codec<FurnaceFuel> BURN_TIME_CODEC = ExtraCodecs.POSITIVE_INT
            .xmap(FurnaceFuel::new, FurnaceFuel::burnTime);
    public static final Codec<FurnaceFuel> CODEC = NeoForgeExtraCodecs.withAlternative(
            RecordCodecBuilder.create(in -> in.group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("burn_time").forGetter(FurnaceFuel::burnTime)).apply(in, FurnaceFuel::new)),
            BURN_TIME_CODEC);
}
