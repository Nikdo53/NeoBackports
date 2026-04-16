package net.nikdo53.neobackports.utils.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.io.StreamCodec;
import org.jetbrains.annotations.Nullable;

public interface RecipeSerializerNeo<T extends Recipe<?>> extends RecipeSerializer<T> {

    MapCodec<T> codec();

    StreamCodec<T> streamCodec();

    @Override
    default T fromJson(ResourceLocation recipeId, JsonObject serializedRecipe){
        return codec().codec().parse(JsonOps.INSTANCE, serializedRecipe).getOrThrow(false, NeoBackports.LOGGER::error);
    }

    @Override
    default @Nullable T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer){
        return streamCodec().decode(buffer);
    }

    @Override
    default void toNetwork(FriendlyByteBuf buffer, T recipe){
        streamCodec().encode(buffer, recipe);
    }
}
