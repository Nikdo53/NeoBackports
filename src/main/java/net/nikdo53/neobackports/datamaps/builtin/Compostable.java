package net.nikdo53.neobackports.datamaps.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.nikdo53.neobackports.io.utils.NeoForgeExtraCodecs;

public record Compostable(float chance, boolean canVillagerCompost) {
    public static final Codec<Compostable> CHANCE_CODEC = Codec.floatRange(0f, 1f)
            .xmap(Compostable::new, Compostable::chance);
    public static final Codec<Compostable> CODEC = NeoForgeExtraCodecs.withAlternative(
            RecordCodecBuilder.create(in -> in.group(
                    Codec.floatRange(0f, 1f).fieldOf("chance").forGetter(Compostable::chance),
                    Codec.BOOL.optionalFieldOf("can_villager_compost", false).forGetter(Compostable::canVillagerCompost)).apply(in, Compostable::new)),
            CHANCE_CODEC);

    /**
     * Constructs a {@link Compostable} that cannot be composted by farmer villagers.
     *
     * @param chance the chance that a compost is successful
     */
    public Compostable(float chance) {
        this(chance, false);
    }
}
