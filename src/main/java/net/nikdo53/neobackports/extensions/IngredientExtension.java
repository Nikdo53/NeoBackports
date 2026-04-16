package net.nikdo53.neobackports.extensions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.utils.BackportCodecs;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

import java.util.List;

public interface IngredientExtension {
    default boolean hasNoItems() {
        throw new RuntimeException("not implemented");
    }

    StreamCodec<Ingredient> CONTENTS_STREAM_CODEC = ByteBufCodecs.INGREDIENT;

    Codec<Ingredient> CODEC = BackportCodecs.IngredientCodecs.CODEC;

    Codec<Ingredient> CODEC_NONEMPTY = BackportCodecs.IngredientCodecs.CODEC_NONEMPTY;
    Codec<List<Ingredient>> LIST_CODEC = BackportCodecs.IngredientCodecs.CODEC.listOf();
    Codec<List<Ingredient>> LIST_CODEC_NONEMPTY = BackportCodecs.IngredientCodecs.CODEC_NONEMPTY.listOf();

}
